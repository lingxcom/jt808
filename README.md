
![](https://www.lingx.com/wp-content/uploads/2021/07/QQͼƬ20210701174626.png)

> ��Ҫ���ܶ��ƻ�ֲ�ʽJT808����ϵQQ��283853318

> ����Ŀ����ҵ��򻯶����������ǳ�����룬����ʹ�ã���ҵ���ַ��http://gps.lingx.com

> ��ý�����Դ�������� https://gitee.com/matrixy/jtt1078-video-server ������ֻ��Ϊ�������ϣ���ҵ����ý�������ȻҲ��Java���������˼·��һ��
## һ��������
https://pan.baidu.com/s/1PHMrDN5a0vo_swODDq-xxg
gshn

> ��ѹ��������jt808���ء�jt1078���ء�web��̨��mysql���ݿ⣻һ���������������á���windows 64 bit����������������������ѯ:283853318

## ��Ŀ����
 - ��Ŀ��Ҫ���ڸ���ѧϰʹ�ã���ҵʹ���빺����ҵ��
 - ��̨����Netty������֧�ְַ���ͬʱ֧��JT808-2013��JT808-2019
 - WEB������Ϭģ���ʹ����ܣ���ܵ�ַ�� https://github.com/lingxcom/lingx
 
## ��Ŀ��ɫ
 - Դ������100%��Դ���޷�װ�����������з�
 - ������ǰ��ˣ���ֱ�Ӳ���ʹ��
 - JT808����2011��2013��2019����Э��汾��֧�ְַ���֧�ְ汾��ʶ��
 - ����ӿڱ�̣�������չ
## ��Ҫ����
### JT808�����
 - �����ն����ݡ����롢���롢������ְ����������
 - �������¶�λ���ݹ�WEB����
 - JT808ָ������·�
 - JT1078��ؿ���ָ��ʵ��
 
### WEB��̨
 - ʵʱ��λ
 - ��ʷ�켣
 - ʵʱ��Ƶ
 - ��ʷ��Ƶ
 - �����б�
 - �ֶӹ���
 - �û���֯
 - Ȩ�޹���
 
## ���л���
 - ����ϵͳ��Windowsϵ��/Linuxϵ��/MacOS 32λ��64λ������
 - Java������JDK 1.8 32λ��64λ������
 - WEB������Tomcat 8.0 ���ҵĿ���������Jetty��JBoss��Ҳ�ǿ��Եģ���׼Servlet 2.5����
 - ���ݿ⣺MySQL8.0
 - ��������ȸ�chrome�����Firefox
 > �ҵĿ��������ǣ�win7(64λ)+eclipse jee+JDK 1.8+Tomcat 8.0+ MySQL8.0

## Դ��ṹ

![](https://gitee.com/lingxcom/jt808/raw/master/QQ20210420155232.png)

## ����Դ��չʾ
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

			boolean isFB = (length & 0b0010000000000000) > 0;// �Ƿ�ְ�
			boolean isVersion = (length & 0b0100000000000000) > 0;// �Ƿ�汾��ʶ
			if (isVersion) {
				buff.readByte();
				tid = buff.readStringBCD(10);
			} else {
				tid = buff.readStringBCD(6);
			}
			
			if(ctx!=null)IJT808Cache.SESSIONS.put(tid, ctx);
			int msgSn = buff.readUnsignedShort();// ��Ϣ��ˮ��
			
			if(!nores8001.contains(msgId)) {//���ظ�
			Cmd8001 cmd=new Cmd8001(tid,msgId,msgSn);
			if(ctx!=null)ctx.writeAndFlush(cmd.toMessageByteBuf());
			}
			
			length = length & 0x3ff;
			ByteBuf content = null;
			if (isFB) {//�ְ�����
				int max = buff.readUnsignedShort();
				int ind = buff.readUnsignedShort();
				content = buff.readByteBuf(length);
				String key = tid + "_" + msgId;
				if (ind == 1) {
					ByteBuf bigBuff=Unpooled.buffer(1024, 1024*1000);//���1M
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
					System.out.println(String.format("û�ж�Ӧ�Ĵ�������%04X,%s,%s",msgId,tid,Utils.bytesToHex(msg)));
				}
			}else {
				System.out.println("��֤������:"+Utils.bytesToHex(msg));
			}
		} catch (Exception e) {
			System.out.println(System.currentTimeMillis()+":"+Utils.bytesToHex(msg));
			e.printStackTrace();
		}

	}


```
