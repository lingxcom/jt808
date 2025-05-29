package com.tracbds.core.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

import io.netty.channel.Channel;
/**
 * 设备上线且定位事件
 * @author lingx
 *
 */
public class JT808OnlineAndLoctionEvent extends ApplicationContextEvent {

	private static final long serialVersionUID = 5743242627311106466L;

	public JT808OnlineAndLoctionEvent(ApplicationContext source,String tid,Channel channel) {
		super(source);
		this.tid=tid;
		this.channel=channel;
	}
	
	private String tid;
	private Channel channel;

	public String getTid() {
		return tid;
	}
	public Channel getChannel() {
		return channel;
	}

}
