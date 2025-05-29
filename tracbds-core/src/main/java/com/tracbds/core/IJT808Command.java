package com.tracbds.core;

import io.netty.buffer.ByteBuf;

public interface IJT808Command {

	public byte[] toMessageBytes();

	public String toMessageHexstring();

	public ByteBuf toMessageByteBuf();
	
	public String getTid();

	public int getMsgId();

	public int getMsgSn();
}
