package com.lingx.jt808.server.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.formdev.flatlaf.FlatLightLaf;
import com.lingx.jt808.server.swing.dialog.MySqlConfigDialog;
import com.lingx.jt808.server.swing.dialog.RedisConfigDialog;
import com.lingx.service.StartupService;
import com.lingx.swing.FlexLayout;
import com.lingx.utils.PropUtils;

public class TracSeekFrame extends JFrame {


	public static String configFile="spring.xml" ;
	private ConfigurableApplicationContext context ;
	public TracSeekFrame() {
		super("领新北斗(TracSeek) - http://www.lingx.com");
    	this.setResizable(false);
    	System.out.println("注意：关闭这个黑色窗口，就是关闭整个领新北斗-车辆动态监控系统(TracSeek)程序。");
    	System.out.println("注意：关闭这个黑色窗口，就是关闭整个领新北斗-车辆动态监控系统(TracSeek)程序。");
    	System.out.println("注意：关闭这个黑色窗口，就是关闭整个领新北斗-车辆动态监控系统(TracSeek)程序。");
    	Image iconImage = Toolkit.getDefaultToolkit().createImage(TracSeekFrame.class.getResource("/images/logo-108x108.png"));
        ImageIcon icon1 = new ImageIcon(TracSeekFrame.class.getResource("/images/play_green.png"));
        ImageIcon icon2 = new ImageIcon(TracSeekFrame.class.getResource("/images/stop_red.png"));
        MySqlConfigDialog dialog1=new MySqlConfigDialog();
        RedisConfigDialog dialog2=new RedisConfigDialog();
        this.setIconImage(iconImage);
    	this.getContentPane().setBackground(Color.decode("#dfe9f6"));
    	this.setLayout(new FlexLayout(28,5,10));
    	
    	MJTextField textField2=new MJTextField(PropUtils.getProp("tracseek.port1","8808"));
    	MJTextField textField3=new MJTextField(PropUtils.getProp("tracseek.port2","8800"));
    	
    	JButton startButton = new JButton("启动(Start)");
        JButton  stopButton = new JButton("停止(Stop)");
        JButton  dbButton = new JButton("设置MySQL");
        JButton  getButton = new JButton("设置Redis");
    	startButton.setIcon(icon1);
        stopButton.setIcon(icon2);

       // this.add(new JLabel("说明:",JLabel.RIGHT),"flex:3");
       // this.add(new JLabel("第一次使用请先连接数据库。"),"flex:9;wrap;");
        this.add(new JLabel("",JLabel.RIGHT),"flex:12;height:12px");
        
        this.add(new JLabel("Http访问端口:",JLabel.RIGHT),"flex:3");
        this.add(textField3,"flex:5;wrap;");

        this.add(new JLabel("JT808接入端口:",JLabel.RIGHT),"flex:3");
        this.add(textField2,"flex:5;wrap;");
        
        this.add(new JLabel(),"flex:3");
        this.add(startButton,"flex:3");
        
        this.add(stopButton,"flex:3;wrap;");
        this.add(new JLabel(),"flex:8");
        this.add(dbButton,"flex:4;wrap;");
        this.add(new JLabel(),"flex:8");
       this.add(getButton,"flex:4;wrap;");
       
        stopButton.setEnabled(false);
        getButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog2.setVisible(true);
			}});
        startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 startButton.setEnabled(false);
				 if(!verifyMySQL()) {
					 startButton.setEnabled(true);
					JOptionPane.showMessageDialog(null, "MySQL连接失败!");
					 return;
				 }
				 if(!verifyRedis()) {
					 startButton.setEnabled(true);
					JOptionPane.showMessageDialog(null, "Redis连接失败!");
					 return;
				 }
		    	 PropUtils.setProp("tracseek.port1",textField2.getText().trim());
		    	 PropUtils.setProp("tracseek.port2", textField3.getText().trim());
		    	 PropUtils.save();
		    	 handleCfg(Integer.parseInt(textField2.getText().trim()),Integer.parseInt(textField3.getText().trim()));
				// context = new ClassPathXmlApplicationContext(new String[] {configFile });
				 context = new FileSystemXmlApplicationContext("file:conf/spring.xml");
				 StartupService bean=context.getBean(StartupService.class);
				 bean.startup();
				 stopButton.setEnabled(true);
				
			}});
        stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 stopButton.setEnabled(false);
				StartupService bean=context.getBean(StartupService.class);
				bean.shutdown();
				 context.close();		
				 startButton.setEnabled(true);
			}});
        
        dbButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog1.setVisible(true);
			}});
        this.add(new JLabel(),"wrap;");

        // 组件添加完成后再 pack，避免按空容器计算出过小窗口
        this.pack();
        this.setMinimumSize(new Dimension(500, 300));
    	this.setLocationRelativeTo(null);

        this.setVisible(true);
        // 关闭窗口并退出程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TracSeekTrayIcon.createTrayIcon(this);
	}
	
	private  boolean verifyMySQL() {
        String ip=PropUtils.getProp("tracseek.database.ip","127.0.0.1");
        String port=PropUtils.getProp("tracseek.database.port","3306");
        String username=PropUtils.getProp("tracseek.database.username","root");
        String password=PropUtils.getProp("tracseek.database.password","123456");
        return MySqlConfigDialog.isConnection(ip, port, username, password);
         
	}
	private  boolean verifyRedis() {
		String ip=PropUtils.getProp("tracseek.redis.ip","127.0.0.1");
        String port=PropUtils.getProp("tracseek.redis.port","3306");
        String username=PropUtils.getProp("tracseek.redis.username","root");
        String password=PropUtils.getProp("tracseek.redis.password","123456");
        return RedisConfigDialog.isConnection(ip, port, password);
	}

	private  void handleCfg(int port1,int port2) {
			String basePath=System.getProperty("user.dir");
			List<String> list=new ArrayList<>();
			list.add("/conf/config/tracseek.properties");
			try {
				String temp= com.alibaba.druid.util.Utils.readFromResource("/template/tracseek.properties");
				temp=String.format(temp, port2,port1);
				for(String str:list) {
					FileUtils.write(new File(basePath+str), temp,"UTF-8");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	public static void main(String[] args) {
    	FlatLightLaf.setup();
		new TracSeekFrame();
	}
}
