package com.lingx.jt808.server.swing;

import com.lingx.jt808.server.swing.dialog.MySqlConfigDialog;

public class CreateDatabase {

	public static void main(String[] args) {
		String ip=args[0];
		String port=args[1];
		String username=args[2];
		String password=args[3];
		if(MySqlConfigDialog.isConnection(ip, port, username, password)) {
			if(MySqlConfigDialog.createDatabase(ip, port, username, password)) {
				if(MySqlConfigDialog.runSQLFile(ip, port, username, password)) {
					System.out.println("创建数据库成功(Success)");
				}else {
					System.out.println("(SQL execution failed)");
				}
			}else {
				System.out.println("数据库tracbds已存在，如果要重新创建，请手工删除数据库tracbds！");
			}
		}else {
			System.out.println("数据库连接失败(Database connection failed)");
		}
	}

}
