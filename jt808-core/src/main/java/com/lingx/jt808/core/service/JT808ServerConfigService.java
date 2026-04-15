package com.lingx.jt808.core.service;

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

	public String getJt808ServerPort() {
		return jt808ServerPort;
	}

	public String getJt808ServerId() {
		return jt808ServerId;
	}
	public String getSavePhotoPath() {
		return this.databaseConfigService.getConfigValue("lingx.jt808.photo.path", "C:/");
	}

	public String getSaveOriginalHexstring() {
		return this.databaseConfigService.getConfigValue("lingx.jt808.save.original", "false");
	}
	public String getStorageDays() {
		return this.databaseConfigService.getConfigValue("lingx.jt808.data.storagedays", "100");
	}

	public String getAutoDropTable() {
		return this.databaseConfigService.getConfigValue("lingx.jt808.data.autodroptable", "true");
	}

	public void setJt808ServerPort(String jt808ServerPort) {
		this.jt808ServerPort = jt808ServerPort;
	}

	
}
