package com.tracbds.core.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 立即拍照
 * @author lingx.com
 *
 */
public class Cmd8801 extends AbstractJT808Command {
	public Cmd8801(String tid,int tdh,boolean isVersion) {
		this(tid,tdh,1,3,0,2,5,0,0,0,0,isVersion);
	}
	/**
	 * 起始字节 字段 数据类型 描述及要求 
0 通道 ID BYTE >0 
1 拍摄命令 WORD 0 表示停止拍摄；0xFFFF 表示录像；其它表示拍 照张数 
3 拍照间隔/录像时间 WORD 秒，0 表示按最小间隔拍照或一直录像 
5 保存标志 BYTE 1：保存；  0：实时上传 
6 分辨率 a
 BYTE 
0x01:320*240； 
0x02:640*480； 
0x03:800*600； 
0x04:1024*768; 
0x05:176*144;[Qcif]; 
0x06:352*288;[Cif]; 
0x07:704*288;[HALF D1]; 
0x08:704*576;[D1]; 
7 图像/视频质量 BYTE 1-10，1 代表质量损失最小，10 表示压缩比最大
8 亮度 BYTE 0-255 
9 对比度 BYTE 0-127 
10 饱和度 BYTE 0-127 
11 色度 BYTE 0-255 
a
终端若不支持系统要求的分辨率，则取最接近的分辨率拍摄并上
	 * @param tid
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 * @param p5
	 * @param p6
	 * @param p7
	 * @param p8
	 * @param p9
	 * @param p10
	 */
	public Cmd8801(String tid,int p1,int p2,int p3,int p4,int p5,int p6,int p7,int p8,int p9,int p10,boolean isVersion) {
		super(0x8801, tid,getBody(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10),isVersion);
	}
	public static byte[] getBody(int p1,int p2,int p3,int p4,int p5,int p6,int p7,int p8,int p9,int p10) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(p1);
		buff.writeShort(p2);
		buff.writeShort(p3);
		buff.writeByte(p4);
		buff.writeByte(p5);
		buff.writeByte(p6);
		buff.writeByte(p7);
		buff.writeByte(p8);
		buff.writeByte(p9);
		buff.writeByte(p10);
		
		return returnByteBuf(buff);
	}
}
