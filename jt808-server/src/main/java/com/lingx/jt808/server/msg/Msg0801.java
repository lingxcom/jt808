package com.lingx.jt808.server.msg;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.cmd.Cmd8800;
import com.lingx.jt808.core.support.MyByteBuf;
import com.lingx.jt808.core.utils.Utils;
import com.lingx.jt808.server.service.JT808ServerConfigService;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Msg0801 extends AbstrctMsgHandler implements IJT808MsgHandler {
	@Autowired
	private JT808ServerConfigService configService;
	@Override
	public int getMsgId() {
		return 0x0801;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx, boolean isVersion,byte[] bytes1)throws Exception {
		MyByteBuf mbuff = new MyByteBuf(data);
		long p1 = mbuff.readUnsignedInt();
		byte p2 = mbuff.readByte();
		byte p3 = mbuff.readByte();
		byte p4 = mbuff.readByte();
		byte p5 = mbuff.readByte();

		//Cmd8800 cmd=new Cmd8800(tid,p1);//用于重传分包
		//ctx.writeAndFlush(cmd.toMessageByteBuf());
		
		Map<String, Object> map = new HashMap<>();
		map.put("tid", tid);
		map.put("p1", p1);
		map.put("p2", p2);
		map.put("p3", p3);
		map.put("p4", p4);
		map.put("p5", p5);
		
		long bj=data.readUnsignedInt();
		long zt=data.readUnsignedInt();
		double lat=data.readUnsignedInt()/1000000f;
		double lng=data.readUnsignedInt()/1000000f;
		int height = data.readUnsignedShort();
		int speed = data.readUnsignedShort();
		int fx = data.readUnsignedShort();
		byte[] bytes=new byte[6];
		data.readBytes(bytes);
		String gpstime = "20" +Utils.bytesToHex(bytes);
		map.put("alarm", bj);
		map.put("status", zt);
		map.put("acc", (zt&0b01)>0?"1":"0");
		map.put("lat", lat);
		map.put("lng", lng);
		map.put("height", height);
		map.put("speed", speed/10f);
		map.put("direction", fx);
		map.put("gpstime", gpstime);
		String extName=".jpg";
		switch(p3) {//0: JPEG: 1: TIF: 2: MP3: 3: WAV: 4: WMV:
		case 0:
			extName=".jpg";
			break;
		case 1:
			extName=".tif";
			break;
		case 2:
			extName=".mp3";
			break;
		case 3:
			extName=".wav";
			break;
		case 4:
			extName=".wmv";
			break;
		
		}
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
		String filepath="/photo/"+tid+"/"+sdf.format(date)+"/"+System.currentTimeMillis()+extName;
		map.put("filepath", filepath);
		byte[] filedata=mbuff.readBytes(mbuff.readableBytes());
		System.out.println("照片总字节数:"+filedata.length);
		try {
			//System.out.println("照片写入地址:"+this.configService.getSavePhotoPath()+filepath);
			FileUtils.writeByteArrayToFile(new File(this.configService.getSavePhotoPath()+filepath), filedata);
		} catch (IOException e) {
			System.out.println("文件写入出错："+this.configService.getSavePhotoPath()+filepath);
			e.printStackTrace();
		}
		
		this.redisService.push(Constants.JT808_0801_DATA, JSON.toJSONString(map));
		filedata=null;mbuff.release();
		map.clear();map=null;
	}

}
