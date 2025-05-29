package com.tracbds.core.support;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MyByteBuf  {

	private ByteBuf bytebuf;
	
	public MyByteBuf(ByteBuf bytebuf){
		this.bytebuf=bytebuf;
	}
	public MyByteBuf(byte[] array){
		this.bytebuf=Unpooled.wrappedBuffer(array);
	}
	public ByteBuf readBytes(byte[] array) {
		return this.bytebuf.readBytes(array);
	}
	public String readStringBCD(int length){
		byte[] buff=new byte[length];
		this.bytebuf.readBytes(buff);
		return bytesToHex(buff);
	}

	public String readString(int length){
		byte[] buff=new byte[length];
		this.bytebuf.readBytes(buff);
		//额外处理
		for(int i=0;i<buff.length;i++) {
			if(buff[i]==0) {
				byte[] buffNew=new byte[i];
				System.arraycopy(buff, 0, buffNew, 0, i);
				buff=buffNew;
			}
		}
		//额外处理 end
		return new String(buff).replace("\u0000", "");
	}
	public String readStringGBK(int length)throws UnsupportedEncodingException{
		byte[] buff=new byte[length];
		this.bytebuf.readBytes(buff);
		return new String(buff,"GBK").replace("\u0000", "");
	}

	public String readStringASCII(int length){
		byte[] buff=new byte[length];
		this.bytebuf.readBytes(buff);
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<buff.length;i++){
			if(buff[i]!=0)
			sb.append((char)buff[i]);
		}
		return sb.toString();
	}
	public String readBinary(int length){
		byte[] buff=new byte[length];
		this.bytebuf.readBytes(buff);
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<buff.length;i++){
			sb.append(leftAdd0(Integer.toBinaryString(buff[i]), 4));
		}
		return sb.toString();
	}
	public static final String leftAdd0(String s,int bit){
		StringBuilder sb=new StringBuilder();
		
		for(int i=s.length();i<bit;i++){
			sb.append("0");
		}
		sb.append(s);
		return sb.toString();
	}
	public short readShort(){
		return this.bytebuf.readShort();
	}
	public int readUnsignedShort(){
		return this.bytebuf.readUnsignedShort();
	}
	public byte readByte(){
		return this.bytebuf.readByte();
	}
	public short readUnsignedByte() {
		return this.bytebuf.readUnsignedByte();
	}
	public long readUnsignedInt(){
		return this.bytebuf.readUnsignedInt();
	}
	public int readInt(){
		return this.bytebuf.readInt();
	}
	public byte[] readBytes(int length){
		byte[] buff=new byte[length];
		this.bytebuf.readBytes(buff);
		return buff;
	}
	public ByteBuf readByteBuf(int length){
		byte[] buff=new byte[length];
		this.bytebuf.readBytes(buff);
		return Unpooled.wrappedBuffer(buff);
	}
	public ByteBuf getBytebuf() {
		return bytebuf;
	}
	public int readableBytes(){
		return this.bytebuf.readableBytes();
	}
	public boolean release(){
		return this.bytebuf.release();
	}
	public static final String bytesToHex(byte[] b) {
		int len=b.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(toHex(b[i]));
		}
		b=null;
		return sb.toString();
	}
	private static final String toHex(byte b) {
		return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF"
				.charAt(b & 0xf));
	}
	
	public static ByteBuf getNewByteBuf() {
		return Unpooled.buffer();
	}
}
