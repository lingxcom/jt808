package com.lingx.jt808.server.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lingx.jt808.core.service.DatabaseConfigService;

@Component
public class JT808ServerConfigService {
	@Autowired
	private DatabaseConfigService databaseConfigService;
	
	@Value("#{configs['jt808.server.id']}")
	private String jt808ServerId="JT808ServerId_1";

	@Value("#{configs['jt808.server.port']}")
	private String jt808ServerPort="8808";

	@Value("#{configs['jt808.websocket.port']}")
	private String websocketPort="8803";

	@Value("#{configs['jt808.http.port']}")
	private String httpPort="8802";

	@Value("#{configs['jt808.command.threadsleepms']}")
	private String jt808CommandTreadSleepMS="100";

	public String getHttpPort() {
		return httpPort;
	}

	public String getJt808ServerId() {
		return jt808ServerId;
	}

	public String getJt808ServerPort() {
		return jt808ServerPort;
	}

	public String getSavePhotoPath() {
		return this.databaseConfigService.getConfigValue("lingx.jt808.photo.path", "C:/");
	}


	public String getSaveOriginalHexstring() {
		return this.databaseConfigService.getConfigValue("lingx.jt808.save.original", "false");
	}

	public String getJt808CommandTreadSleepMS() {
		return jt808CommandTreadSleepMS;
	}
	public String getWebsocketPort() {
		return websocketPort;
	}
	public String getTicket() {
		 return this.databaseConfigService.getConfigValue("lingx.jt808.api.ticket", "43b2f84b-6e2d-4c4a-87d6-ab67e8c883ae");
	}
	
}
