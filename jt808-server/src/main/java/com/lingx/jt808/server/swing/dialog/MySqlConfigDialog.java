package com.lingx.jt808.server.swing.dialog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.Utils;
import com.lingx.swing.FlexLayout;
import com.lingx.utils.PropUtils;
import com.lingx.jt808.server.swing.MJTextField;
import com.lingx.jt808.server.swing.TracSeekFrame;

public class MySqlConfigDialog extends JDialog{
	private final static Logger log = LoggerFactory.getLogger(MySqlConfigDialog.class);

    public static JButton button=new JButton("保存设置");
	public MySqlConfigDialog() {
		super();
		MySqlConfigDialog _this=this;
        ImageIcon icon3 = new ImageIcon(TracSeekFrame.class.getResource("/images/disk.png"));
		this.setTitle("设置MySQL");
		this.setSize(500,300);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.decode("#dfe9f6"));
		this.setBackground(Color.decode("#dfe9f6"));
		this.setLayout(new FlexLayout(24,5,10));

        MJTextField text1 = new MJTextField(PropUtils.getProp("tracseek.database.ip","127.0.0.1"));
        MJTextField text2 = new MJTextField(PropUtils.getProp("tracseek.database.port","3306"));
        MJTextField text3 = new MJTextField(PropUtils.getProp("tracseek.database.username","root"));
        MJTextField text4 = new MJTextField(PropUtils.getProp("tracseek.database.password",""));
        this.add(new JLabel("说明:",JLabel.RIGHT),"flex:2;");
        this.add(new JLabel("建议使用MySQL8.0.x；数据库tracseek不存在时，自动创建数据库。"),"flex:10;");
		this.add(new JLabel("IP地址:",JLabel.RIGHT),"flex:2");
		this.add(text1,"flex:8;wrap;");
		this.add(new JLabel("端口:",JLabel.RIGHT),"flex:2");
		this.add(text2,"flex:8;wrap;");

		this.add(new JLabel("用户名:",JLabel.RIGHT),"flex:2");
		this.add(text3,"flex:8;wrap;");
		this.add(new JLabel("密码:",JLabel.RIGHT),"flex:2");
		this.add(text4,"flex:8;wrap;");
		this.add(new JLabel("数据库:",JLabel.RIGHT),"flex:2");
		this.add(new JLabel("tracseek"),"flex:8;wrap;");
		this.add(new JLabel(),"flex:4;");
		this.add(button,"flex:4");
		button.setIcon(icon3);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MySqlConfigDialog.button.setEnabled(false);
				String ip=text1.getText().trim();
				String port=text2.getText().trim();
				String username=text3.getText().trim();
				String password=text4.getText().trim();
				PropUtils.setProp("tracseek.database.ip", ip);
				PropUtils.setProp("tracseek.database.port", port);
				PropUtils.setProp("tracseek.database.username", username);
				PropUtils.setProp("tracseek.database.password", password);
	            PropUtils.save();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						boolean isConnection=isConnection(ip,port,username,password);
						if(!isConnection) {
							JOptionPane.showMessageDialog(null, "数据库连接失败(Database connection failed)");
							MySqlConfigDialog.button.setEnabled(true);
							return;
						}
						boolean isCreate=createDatabase(ip,port,username,password);
						if(isCreate) {
							boolean isSuccess=runSQLFile(ip,port,username,password);
							if(isSuccess) {
								JOptionPane.showMessageDialog(null, "创建数据库成功(Success)");
							}else {
								JOptionPane.showMessageDialog(null, "执行SQL失败(SQL execution failed)");
								MySqlConfigDialog.button.setEnabled(true);
								return;
							}
						}else {

							JOptionPane.showMessageDialog(null, "连接数据库成功(Success)");
						}
						MySqlConfigDialog.button.setEnabled(true);
						_this.setVisible(false);
					}});
		}});

		this.setModal(true);
	}
	public static boolean isConnection(String ip,String port,String username,String password) {
        String jdbcUrl = "jdbc:mysql://%s:%s/?useSSL=false&serverTimezone=UTC";
		try (Connection conn = DriverManager.getConnection(String.format(jdbcUrl, ip,port), username, password);
	             Statement stmt = conn.createStatement()) {
            handleDbCfg(ip,Integer.parseInt(port),username,password);
            conn.close();
	           return true;
	        } catch (SQLException e) {
	        	e.printStackTrace();
	           return false;
	        }
	}
	
	public static boolean  createDatabase(String ip,String port,String username,String password) {
        // 连接到MySQL服务器（不指定具体数据库）
        String jdbcUrl = "jdbc:mysql://%s:%s/?useSSL=false&serverTimezone=UTC";
        String dbName = "tracseek";  // 要创建的数据库名称

        try (Connection conn = DriverManager.getConnection(String.format(jdbcUrl, ip,port), username, password);
             Statement stmt = conn.createStatement()) {
            // 执行创建数据库的SQL
            String sql = "CREATE DATABASE " + dbName 
                        + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
            stmt.executeUpdate(sql);
            conn.close();
           return true;
        } catch (SQLException e) {
           return false;
        }
    }
	
	 public static boolean runSQLFile(String ip,String port,String username,String password) {
	        String jdbcUrl = "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC";
	        File sqlFile = new File(System.getProperty("user.dir") + "/conf/tracseek.sql");
	        try (Connection conn = DriverManager.getConnection(String.format(jdbcUrl, ip,port,"tracseek"), username, password);
	             Statement stmt = conn.createStatement()) {
	        	String temp = FileUtils.readFileToString(sqlFile, StandardCharsets.UTF_8);
	        	// 兼容 \n 和 \r\n，避免 Windows 下语句切分失败
	            String[] queries = temp.split(";\\s*(?:\\r?\\n|$)");
	            for (String query : queries) {
	            	String sql = trimLeadingSqlComments(query);
	            	if (sql.isEmpty()) {
	            		continue;
	            	}
	            	// execute 可同时处理查询和更新语句，避免 executeUpdate 遇到结果集时报错
	            	stmt.execute(sql);
	            }
	            log.info("SQL文件导入成功: {}", sqlFile.getAbsolutePath());
	            return true;
	        } catch (Exception e) {
	        	log.error("执行SQL文件失败: {}", sqlFile.getAbsolutePath(), e);
	            return false;
	        }
	    }
	 
	 private static String trimLeadingSqlComments(String query) {
	 	String sql = query == null ? "" : query.trim();
	 	boolean hasLeadingComments = true;
	 	while (hasLeadingComments && !sql.isEmpty()) {
	 		hasLeadingComments = false;
	 		if (sql.startsWith("--") || sql.startsWith("#")) {
	 			int lineBreakIndex = sql.indexOf('\n');
	 			sql = lineBreakIndex >= 0 ? sql.substring(lineBreakIndex + 1).trim() : "";
	 			hasLeadingComments = true;
	 			continue;
	 		}
	 		if (sql.startsWith("/*") && !sql.startsWith("/*!")) {
	 			int commentEnd = sql.indexOf("*/");
	 			if (commentEnd >= 0) {
	 				sql = sql.substring(commentEnd + 2).trim();
	 				hasLeadingComments = true;
	 			}
	 		}
	 	}
	 	return sql;
	 }
	 
	 public static void handleDbCfg(String ip,int port,String account,String password) {
			String basePath=System.getProperty("user.dir");
			List<String> list=new ArrayList<>();
			list.add("/conf/config/db.properties");
			try {
				String temp= Utils.readFromResource("/template/db.properties");
				temp=String.format(temp, ip,port,account,password);
				for(String str:list) {
					FileUtils.write(new File(basePath+str), temp,"UTF-8");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
