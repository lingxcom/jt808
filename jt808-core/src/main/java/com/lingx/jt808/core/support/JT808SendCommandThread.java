package com.lingx.jt808.core.support;

import com.lingx.jt808.core.IJT808Command;
import com.lingx.jt808.core.service.JT808CommandService;
/**
 * 指令下发线程
 * @author lingx
 *
 */
public class JT808SendCommandThread implements Runnable {
	public JT808SendCommandThread(IJT808Command command,String carid,String userid,JT808CommandService commandService) {
		this.carId=carid;
		this.userid=userid;
		this.command=command;
		this.commandService=commandService;
	}
	private String userid;
	private String carId;
	private IJT808Command command;
	private JT808CommandService commandService;
	@Override
	public void run() {

		this.commandService.sendCommand(this.command,this.carId, this.userid,false);
		
		this.command=null;
		this.userid=null;
	}

}
