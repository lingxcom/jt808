package com.lingx.jt808.core;

public interface JT808ForwardHandler {

	public byte[] format(byte[] data);
	public boolean isHttp();
	public void httpPost(byte[] data);
}
