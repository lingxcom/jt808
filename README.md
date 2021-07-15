
![](https://www.lingx.com/wp-content/uploads/2021/07/QQ图片20210701174626.png)

> 流媒体服务源码引用于 https://gitee.com/matrixy/jtt1078-video-server ；这里只是为方便整合

> 需要功能定制或分布式JT808，联系QQ：283853318，微信或电话:15060620800

> 该项目由商业版简化而来，所以是成熟代码，放心使用；商业版地址：http://gps.lingx.com
## 项目介绍
 - 后台基于Netty开发，支持分包，同时支持JT808-2013，JT808-2019
 - WEB采用灵犀模驱低代码框架，框架地址： https://github.com/lingxcom/lingx
 
## 项目特色
 - 源码真正100%开源，无封装包，纯自主研发
 - 完整的前后端，可直接部署使用
 - 兼容2011、2013、2019部标协议版本，支持分包，支持版本标识；
 - 面向接口编程，易于扩展
## 主要功能
### JT808服务端
 - 接收终端数据、解码、编码、拆包、分包、数据入库
 - 缓存最新定位数据供WEB调用
 - JT808指令构造与下发
 - JT1078相关控制指令实现
 
### WEB后台
 - 实时定位
 - 历史轨迹
 - 实时视频
 - 历史视频
 - 车辆列表
 - 分队管理
 - 用户组织
 - 权限管理
 
## 运行环境
 - 操作系统：Windows系列/Linux系列/MacOS 32位或64位都可以
 - Java环境：JDK 1.8 32位或64位都可以
 - WEB容器：Tomcat 8.0 是我的开发环境，Jetty、JBoss等也是可以的，标准Servlet 2.5工程
 - 数据库：MySQL8.0
 - 浏览器：谷歌chrome、火狐Firefox
 > 我的开发环境是：win7(64位)+eclipse jee+JDK 1.8+Tomcat 8.0+ MySQL8.0

## 源码结构

![](https://gitee.com/lingxcom/jt808/raw/master/QQ20210420155232.png)

## 核心源码展示
```java
@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		//System.out.println(Utils.bytesToHex(msg));
		try {
			MyByteBuf buff = new MyByteBuf(JT808Utils.decode(msg));
			buff.readByte();

			int msgId = buff.readUnsignedShort();
			int length = buff.readUnsignedShort();
			String tid = "";

			boolean isFB = (length & 0b0010000000000000) > 0;// 是否分包
			boolean isVersion = (length & 0b0100000000000000) > 0;// 是否版本标识
			if (isVersion) {
				buff.readByte();
				tid = buff.readStringBCD(10);
			} else {
				tid = buff.readStringBCD(6);
			}
			
			if(ctx!=null)IJT808Cache.SESSIONS.put(tid, ctx);
			int msgSn = buff.readUnsignedShort();// 消息流水号
			
			if(!nores8001.contains(msgId)) {//不回复
			Cmd8001 cmd=new Cmd8001(tid,msgId,msgSn);
			if(ctx!=null)ctx.writeAndFlush(cmd.toMessageByteBuf());
			}
			
			length = length & 0x3ff;
			ByteBuf content = null;
			if (isFB) {//分包处理
				int max = buff.readUnsignedShort();
				int ind = buff.readUnsignedShort();
				content = buff.readByteBuf(length);
				String key = tid + "_" + msgId;
				if (ind == 1) {
					ByteBuf bigBuff=Unpooled.buffer(1024, 1024*1000);//最大1M
					bigBuff.writeBytes(content);
					IJT808Cache.FB_CACHE.put(key, bigBuff);
					return;
				} else if (ind == max) {
					IJT808Cache.FB_CACHE.getIfPresent(key).writeBytes(content);
					content = IJT808Cache.FB_CACHE.getIfPresent(key);
					IJT808Cache.FB_CACHE.invalidate(key);
				} else {
					IJT808Cache.FB_CACHE.getIfPresent(key).writeBytes(content);
					return;
				}
			} else {
				content = buff.readByteBuf(length);
			}

			byte check = buff.readByte();
			buff.readByte();

			if (JT808Utils.check(msg, check, length, isFB, isVersion)) {
				IJT808MsgHandler msgHandler=null;
				for(IJT808MsgHandler tmp:this.listMsgHandler) {
					if(tmp.getMsgId()==msgId) {
						msgHandler=tmp;
					}
				}
				if(msgHandler!=null) {
					try {
						msgHandler.handle(content, tid, msgId, msgSn, ctx,isVersion);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					System.out.println(String.format("没有对应的处理器：%04X,%s,%s",msgId,tid,Utils.bytesToHex(msg)));
				}
			}else {
				System.out.println("验证码有误:"+Utils.bytesToHex(msg));
			}
		} catch (Exception e) {
			System.out.println(System.currentTimeMillis()+":"+Utils.bytesToHex(msg));
			e.printStackTrace();
		}

	}


```
