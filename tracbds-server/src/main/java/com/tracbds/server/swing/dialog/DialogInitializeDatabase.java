package com.tracbds.server.swing.dialog;

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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;

import com.alibaba.druid.util.Utils;
import com.tracbds.server.swing.MJTextField;
import com.lingx.swing.FlexLayout;
import com.lingx.utils.PropUtils;

public class DialogInitializeDatabase extends JDialog{

    public static JButton button=new JButton("连接数据库(Connect)");
	public DialogInitializeDatabase() {
		super();
		DialogInitializeDatabase _this=this;
		this.setTitle("连接数据库(Init Database)");
		this.setSize(600,400);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.decode("#dfe9f6"));
		this.setBackground(Color.decode("#dfe9f6"));
		this.setLayout(new FlexLayout(24,5,10));

        MJTextField text1 = new MJTextField(PropUtils.getProp("tracbds.database.ip","127.0.0.1"));
        MJTextField text2 = new MJTextField(PropUtils.getProp("tracbds.database.port","3306"));
        MJTextField text3 = new MJTextField(PropUtils.getProp("tracbds.database.username","root"));
        MJTextField text4 = new MJTextField(PropUtils.getProp("tracbds.database.password",""));
		this.add(new JLabel("数据库MySql8.0.x；数据库tracbds不存在时，创建数据库并执行SQL脚本。"),"flex:12;");
		this.add(new JLabel("Database MySql8.0.x; When database tracbds not exist,create and executes SQL scripts."),"flex:12;");
		this.add(new JLabel("IP地址(IP):",JLabel.RIGHT),"flex:3");
		this.add(text1,"flex:8;wrap;");
		this.add(new JLabel("端口(Port):",JLabel.RIGHT),"flex:3");
		this.add(text2,"flex:8;wrap;");

		this.add(new JLabel("用户名(Username):",JLabel.RIGHT),"flex:3");
		this.add(text3,"flex:8;wrap;");
		this.add(new JLabel("密码(Password):",JLabel.RIGHT),"flex:3");
		this.add(text4,"flex:8;wrap;");
		this.add(new JLabel("数据库(Database):",JLabel.RIGHT),"flex:3");
		this.add(new JLabel("tracbds"),"flex:8;wrap;");
		this.add(new JLabel(),"flex:4;wrap;");
		this.add(new JLabel(),"flex:4;");
		this.add(button,"flex:4");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogInitializeDatabase.button.setEnabled(false);
				DialogInitializeDatabase.button.setText("正在连接中(Connect)...");
				String ip=text1.getText();
				String port=text2.getText();
				String username=text3.getText();
				String password=text4.getText();
				PropUtils.setProp("tracbds.database.ip", ip);
				PropUtils.setProp("tracbds.database.port", port);
				PropUtils.setProp("tracbds.database.username", username);
				PropUtils.setProp("tracbds.database.password", password);
	            PropUtils.save();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						boolean isConnection=isConnection(ip,port,username,password);
						if(!isConnection) {
							JOptionPane.showMessageDialog(null, "数据库连接失败(Database connection failed)");
							DialogInitializeDatabase.button.setEnabled(true);
							DialogInitializeDatabase.button.setText("连接数据库(Connect)");
							return;
						}
						boolean isCreate=createDatabase(ip,port,username,password);
						if(isCreate) {
							boolean isSuccess=runSQLFile(ip,port,username,password);
							if(isSuccess) {
								JOptionPane.showMessageDialog(null, "创建数据库成功(Success)");
							}else {
								JOptionPane.showMessageDialog(null, "执行SQL失败(SQL execution failed)");
								DialogInitializeDatabase.button.setEnabled(true);
								DialogInitializeDatabase.button.setText("连接数据库(Connect)");
								return;
							}
						}else {

							JOptionPane.showMessageDialog(null, "连接数据库成功(Success)");
						}
						_this.setVisible(false);
					}});
		}});

		this.setModal(true);
	}
	public static boolean isConnection(String ip,String port,String username,String password) {
        String jdbcUrl = "jdbc:mysql://%s:%s/?useSSL=false&serverTimezone=UTC";
		try (Connection conn = DriverManager.getConnection(String.format(jdbcUrl, ip,port), username, password);
	             Statement stmt = conn.createStatement()) {
           // handleDbCfg(ip,Integer.parseInt(port),username,password);//不覆盖了，直接修改启动参数
            conn.close();
	           return true;
	        } catch (SQLException e) {
	           return false;
	        }
	}
	
	public static boolean  createDatabase(String ip,String port,String username,String password) {
        // 连接到MySQL服务器（不指定具体数据库）
        String jdbcUrl = "jdbc:mysql://%s:%s/?useSSL=false&serverTimezone=UTC";
        String dbName = "tracbds";  // 要创建的数据库名称

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
	        try {
	        	Connection conn = DriverManager.getConnection(String.format(jdbcUrl, ip,port,"tracbds"), username, password);
	        	String temp=com.alibaba.druid.util.Utils.readFromResource("tracbds.sql");//FileUtils.readFileToString(new File(sqlFilePath), StandardCharsets.UTF_8);
	               // 分割语句（按分号和换行）
	               String[] queries = temp.split(";(\\s)*\\n");

	               Statement stmt = conn.createStatement();
	               {
	                   for (String query : queries) {
	                       if (!query.trim().isEmpty()) {
	                           stmt.addBatch(query.trim());
	                       }
	                   }
	                   stmt.executeBatch(); // 批量执行
	               }
	               conn.close();
	               System.out.println("SQL文件导入成功");
	               return true;
	           } catch (Exception e) {
	             //  e.printStackTrace();
	               return false;
	           }
	    }
	 public static void handleDbCfg(String ip,int port,String account,String password) {
			String basePath=System.getProperty("user.dir");
			List<String> list=new ArrayList<>();
			list.add("/conf/config/db.properties");
			try {
				String temp= Utils.readFromResource("/template/db.properties");
				temp=String.format(temp, ip,port,account,password);
				System.out.println(temp);
				for(String str:list) {
					System.out.println(basePath+str);
					FileUtils.write(new File(basePath+str), temp,"UTF-8");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
