package com.tracbds.database.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tracbds.core.service.JT808CommonService;
import com.tracbds.server.service.JT808ServerConfigService;

@Component
public class JT808DatabaseTableJob {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommonService jt808CommonService;
	@Autowired
	private JT808ServerConfigService configService;
	
	@PostConstruct
	public void init() {
		this.autoCreateGpsDataTable();
		//autoDropTable();
		clearInvalidData();
	}

	@Scheduled(cron = "0/20 0 0 * * ?")
	public void autoCreateGpsDataTable() {
		StringBuilder sb = new StringBuilder();

		sb.append("CREATE TABLE `%s` (");
		sb.append(" `id` int NOT NULL AUTO_INCREMENT,");
		sb.append(" `car_id` int NOT NULL,");
		sb.append(" `alarm` bigint NOT NULL,");
		sb.append(" `status` bigint NOT NULL,");
		sb.append(" `acc` char(1) NOT NULL DEFAULT '0',");
		sb.append(" `location` char(1) NOT NULL DEFAULT '0',");
		sb.append(" `lat` decimal(10,7) NOT NULL,");
		sb.append(" `lng` decimal(10,7) NOT NULL,");
		sb.append(" `height` int NOT NULL,");
		sb.append(" `speed` decimal(6,1) NOT NULL,");
		sb.append(" `direction` smallint NOT NULL,");
		sb.append(" `gpstime` char(14) NOT NULL,");
		sb.append(" `systime` char(14) NOT NULL,");
		sb.append(" `mileage` decimal(10,1) NOT NULL,");
		sb.append(" `oil` decimal(6,1) DEFAULT '0.0',");
		sb.append(" `txxh` int NOT NULL,");
		sb.append(" `wxxh` int NOT NULL,");
		sb.append(" `status_str` varchar(200) DEFAULT NULL,");
		sb.append(" `alarm_str` varchar(200) DEFAULT NULL,");
		sb.append(" PRIMARY KEY (`id`)");
		sb.append(", KEY `IDX_TID_GPSTIME` (`car_id`,`gpstime`)");
		sb.append(")");
		sb.append(" ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		Date date = new Date();
		try {
			String tableName = this.jt808CommonService.get0x0200TableName(date);
			this.jdbcTemplate.update(String.format(sb.toString(), tableName, tableName, tableName));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		try {
			date.setDate(date.getDate() + 1);
			String tableName = this.jt808CommonService.get0x0200TableName(date);
			this.jdbcTemplate.update(String.format(sb.toString(), tableName, tableName, tableName));
		} catch (Exception e) {
		}

	}
	
	@Scheduled(cron = "0/20 0 0 * * ?")
	public void autoDropTable() {
		if ("true".equals(this.configService.getAutoDropTable())) {
			int days = Integer.parseInt(this.configService.getStorageDays());
			Date date = new Date();
			try {
				date.setDate(date.getDate() - days);
				this.jdbcTemplate.update("drop table " + this.jt808CommonService.get0x0200TableName(date));

				date.setDate(date.getDate() - 1);
				this.jdbcTemplate.update("drop table " + this.jt808CommonService.get0x0200TableName(date));

				date.setDate(date.getDate() - 1);
				this.jdbcTemplate.update("drop table " + this.jt808CommonService.get0x0200TableName(date));
			} catch (Exception e) {
				// e.printStackTrace();
			}
			date = new Date();
			date.setDate(date.getDate() - days);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			String time=sdf.format(date);
			String dateStr=sdf2.format(date);
			this.jdbcTemplate.update("delete from tgps_car_alarm where gpstime<?",time);//清除报警记录
			this.jdbcTemplate.update("delete from tgps_car_cmd where ts<?",time);
			this.jdbcTemplate.update("delete from tgps_event where ts<?",time);

			date = new Date();
			date.setDate(date.getDate() - days);
			 time=sdf.format(date);
			 dateStr=sdf2.format(date);
			this.jdbcTemplate.update("delete from tgps_mileage where create_time<?",dateStr);
			
			
			this.jdbcTemplate.update("truncate tlingx_message");
		}
	}
	public void clearInvalidData() {
		this.jdbcTemplate.update("delete from tgps_group_car where group_id not in(select id from tgps_group)");
		this.jdbcTemplate.update("delete from tgps_group_car where car_id not in(select id from tgps_car)");
		
		this.jdbcTemplate.update("delete from tgps_group_user where group_id not in(select id from tgps_group)");
		this.jdbcTemplate.update("delete from tgps_group_user where user_id not in(select id from tlingx_user)");

		this.jdbcTemplate.update("delete from tgps_cmd_user where cmd_id not in(select id from tgps_cmd)");
		this.jdbcTemplate.update("delete from tgps_cmd_user where user_id not in(select id from tlingx_user)");
	}
}
