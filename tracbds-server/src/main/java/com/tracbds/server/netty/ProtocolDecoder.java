package com.tracbds.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


/**
 */
public class ProtocolDecoder extends ByteToMessageDecoder {
	private static final int MIN_HEADER_SIZE = 10;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if (in == null) {
			return;
		}

		if (in.readableBytes() < MIN_HEADER_SIZE) {
			return;
		}
		in.markReaderIndex();
		while (in.isReadable()) {
			in.markReaderIndex();
			int packetBeginIndex = in.readerIndex();
			byte tag = in.readByte();
			if (tag == 0x7E && in.isReadable()) {
				tag = in.readByte();
				while (tag != 0x7E) {
					if (in.isReadable() == false) {
						in.resetReaderIndex(); 
						return;
					}
					tag = in.readByte();
				}
				int pos = in.readerIndex();
				int packetLength = pos - packetBeginIndex;
				if (packetLength > 1) {
					byte[] tmp = new byte[packetLength];
					in.resetReaderIndex();
					in.readBytes(tmp);
					out.add(tmp); 
				} else {
					
					in.resetReaderIndex();
					in.readByte(); 
				}
			}
		}

		return;
	}


}
