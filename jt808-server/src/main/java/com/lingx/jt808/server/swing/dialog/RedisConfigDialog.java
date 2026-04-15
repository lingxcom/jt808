package com.lingx.jt808.server.swing.dialog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.alibaba.druid.util.Utils;
import com.lingx.swing.FlexLayout;
import com.lingx.utils.PropUtils;
import com.lingx.jt808.server.swing.MJTextField;
import com.lingx.jt808.server.swing.TracSeekFrame;

import redis.clients.jedis.Jedis;

public class RedisConfigDialog extends JDialog{

    public static JButton button=new JButton("保存设置");
	public RedisConfigDialog() {
		super();
        ImageIcon icon3 = new ImageIcon(TracSeekFrame.class.getResource("/images/disk.png"));
		RedisConfigDialog _this=this;
		this.setTitle("设置Redis");
		this.setSize(500,300);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.decode("#dfe9f6"));
		this.setBackground(Color.decode("#dfe9f6"));
		this.setLayout(new FlexLayout(24,5,10));

        MJTextField text1 = new MJTextField(PropUtils.getProp("tracseek.redis.ip","127.0.0.1"));
        MJTextField text2 = new MJTextField(PropUtils.getProp("tracseek.redis.port","6379"));
        MJTextField text3 = new MJTextField(PropUtils.getProp("tracseek.redis.database","1"));
        MJTextField text4 = new MJTextField(PropUtils.getProp("tracseek.redis.password",""));
        this.add(new JLabel("说明:",JLabel.RIGHT),"flex:2;");
        this.add(new JLabel("建议使用Redis 3.2.x，或更高版本。"),"flex:10;");
		this.add(new JLabel("IP地址:",JLabel.RIGHT),"flex:2");
		this.add(text1,"flex:8;wrap;");
		this.add(new JLabel("端口:",JLabel.RIGHT),"flex:2");
		this.add(text2,"flex:8;wrap;");
		this.add(new JLabel("密码:",JLabel.RIGHT),"flex:2");
		this.add(text4,"flex:8;wrap;");
		this.add(new JLabel("数据库:",JLabel.RIGHT),"flex:2");
		this.add(text3,"flex:8;wrap;");
		this.add(new JLabel(),"flex:4;");
		this.add(button,"flex:4");
		button.setIcon(icon3);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ip=text1.getText().trim();
				String port=text2.getText().trim();
				String database=text3.getText().trim();
				String password=text4.getText().trim();
				if(isConnection(ip,port,password)) {
					PropUtils.setProp("tracseek.redis.ip", ip);
					PropUtils.setProp("tracseek.redis.port", port);
					PropUtils.setProp("tracseek.redis.database", database);
					PropUtils.setProp("tracseek.redis.password", password);
		            PropUtils.save();
		            handleRedisCfg(ip,Integer.parseInt(port),database,password);
		            JOptionPane.showMessageDialog(null, "Redis连接成功(Success)");
		            _this.setVisible(false);
				}else {
		            JOptionPane.showMessageDialog(null, "Redis连接失败");
				}
		}});

		this.setModal(true);
	}
	 public static void handleRedisCfg(String ip,int port,String database,String password) {
			String basePath=System.getProperty("user.dir");
			List<String> list=new ArrayList<>();
			list.add("/conf/config/redis.properties");
			try {
				String temp= Utils.readFromResource("/template/redis.properties");
				temp=String.format(temp, ip,port,password,database);
				for(String str:list) {
					FileUtils.write(new File(basePath+str), temp,"UTF-8");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	 public static boolean isConnection(String ip,String port,String password) {
		 boolean b=false;
		 Jedis jedis = null;
	        try {
	            // 连接 Redis
	            jedis = new Jedis("127.0.0.1", Integer.parseInt(port));
	            jedis.auth(password);
	            // 发送 ping
	            String response = jedis.ping();

	            if ("PONG".equals(response)) {
	              //  System.out.println("Redis 连接成功");
	                b=true;
	            } else {
	                System.out.println("Redis 连接失败");
	                b=false;
	            }

	        } catch (Exception e) {
	            System.out.println("Redis 连接异常: " + e.getMessage());
	        } finally {
	            if (jedis != null) {
	                jedis.close();
	            }
	        }
	        return b;
	    }
}
