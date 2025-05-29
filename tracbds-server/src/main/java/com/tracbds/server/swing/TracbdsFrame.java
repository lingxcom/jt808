package com.tracbds.server.swing;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.formdev.flatlaf.FlatLightLaf;
import com.lingx.service.ConfigService;
import com.lingx.service.StartupService;
import com.lingx.swing.FlexLayout;
import com.lingx.utils.PropUtils;
import com.tracbds.core.utils.Utils;
import com.tracbds.server.service.JT808ServerConfigService;
import com.tracbds.server.swing.dialog.DialogInitializeDatabase;

public class TracbdsFrame extends JFrame {


	public static String configFile="spring.xml" ;
	private ConfigurableApplicationContext context ;
	public TracbdsFrame() {
		super("Tracbds - http://www.lingx.com");
    	this.setResizable(false);

    	Image iconImage = Toolkit.getDefaultToolkit().createImage(TracbdsFrame.class.getResource("/images/108_108.png"));
        ImageIcon icon1 = new ImageIcon(TracbdsFrame.class.getResource("/images/play_green.png"));
        ImageIcon icon2 = new ImageIcon(TracbdsFrame.class.getResource("/images/stop_red.png"));
        ImageIcon icon3 = new ImageIcon(TracbdsFrame.class.getResource("/images/disk.png"));
        DialogInitializeDatabase dialog1=new DialogInitializeDatabase();
        this.setIconImage(iconImage);
        // 设置窗口大小和位置
    	this.setSize(600, 400);
    	this.setLocationRelativeTo(null);
    	this.getContentPane().setBackground(Color.decode("#dfe9f6"));
    	this.setLayout(new FlexLayout(24,5,10));

    	PropUtils.init("/config.properties");
    	if(Utils.isNull(PropUtils.getProp("tracbds.ip"))) {
    		PropUtils.setProp("tracbds.ip", "127.0.0.1");
    		PropUtils.setProp("tracbds.port1", "8808");
    		PropUtils.setProp("tracbds.port2", "8802");
    		PropUtils.setProp("tracbds.port3", "8803");
    	}
    	
    	MJTextField textField1=new MJTextField(PropUtils.getProp("tracbds.ip"));
    	MJTextField textField2=new MJTextField(PropUtils.getProp("tracbds.port1"));
    	MJTextField textField3=new MJTextField(PropUtils.getProp("tracbds.port2"));
    	MJTextField textField4=new MJTextField(PropUtils.getProp("tracbds.port3"));
    	
    	JButton startButton = new JButton("启动(Start)");
        JButton  stopButton = new JButton("停止(Stop)");
        JButton  dbButton = new JButton("连接数据库(Init Database)");
        JButton  getButton = new JButton("商业版(Ultimate Edition)");
    	startButton.setIcon(icon1);
        stopButton.setIcon(icon2);

        this.add(new JLabel("说明:",JLabel.RIGHT),"flex:3");
        this.add(new JLabel("第一次使用请先连接数据库。"),"flex:9;wrap;");

        this.add(new JLabel("Info:",JLabel.RIGHT),"flex:3");
        this.add(new JLabel("Please init database before using it for the first time."),"flex:9;wrap;");
        
        this.add(new JLabel("外网地址(IP):",JLabel.RIGHT),"flex:3");
        this.add(textField1,"flex:5;wrap;");

        this.add(new JLabel("WEB端口(Web Port):",JLabel.RIGHT),"flex:3");
        this.add(textField3,"flex:5;wrap;");
        //this.add(new JLabel("Websocket端口(Port):",JLabel.RIGHT),"flex:3");
        //this.add(textField4,"flex:5;wrap;");

        this.add(new JLabel("JT808端口(JT808 Port):",JLabel.RIGHT),"flex:3");
        this.add(textField2,"flex:5;wrap;");
        
        this.add(new JLabel(),"flex:3");
        this.add(startButton,"flex:3");
        
        this.add(stopButton,"flex:3;wrap;");
        this.add(new JLabel(),"flex:8");
        this.add(dbButton,"flex:4;wrap;");
        this.add(new JLabel(),"flex:8");
        this.add(getButton,"flex:4;wrap;");
        JLabel label1=new JLabel("网址(URL):",JLabel.RIGHT);
        JLabel label2=new JLabel("默认(Default)",JLabel.RIGHT);
        JLabel Info=new JLabel();
        JLabel Info2=new JLabel();
        this.add(label1,"flex:3");
        this.add(Info,"flex:8;wrap;");
        this.add(label2,"flex:3");
        this.add(Info2,"flex:8;wrap;");
        label1.setVisible(false);
        label2.setVisible(false);
        stopButton.setEnabled(false);
        getButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(
							new java.net.URI("http://www.lingx.com"));
				} catch (Exception e1) {
					e1.printStackTrace();
				} 		
			}});
        startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 startButton.setEnabled(false);
				 if(!verify()) {
					 startButton.setEnabled(true);
					JOptionPane.showMessageDialog(null, "数据库连接失败(Database connection failed)");
					 return;
				 }
				 PropUtils.setProp("tracbds.ip", textField1.getText());
		    	 PropUtils.setProp("tracbds.port1",textField2.getText());
		    	 PropUtils.setProp("tracbds.port2", textField3.getText());
		    	 PropUtils.setProp("tracbds.port3", textField4.getText());
		    	 PropUtils.save();
				 context = new ClassPathXmlApplicationContext(
						new String[] {configFile });
				 
				 ConfigService configService=context.getBean(ConfigService.class);
				 JT808ServerConfigService jt808ServerConfigService=context.getBean(JT808ServerConfigService.class);
				 StartupService bean=context.getBean(StartupService.class);
				 configService.setWebHttpPort(textField3.getText());
				 jt808ServerConfigService.setJt808ServerPort(textField2.getText());
				 jt808ServerConfigService.setWebsocketPort(textField4.getText());
				 bean.startup();
				 stopButton.setEnabled(true);
				 String url="http://"+textField1.getText()+":"+textField3.getText();
				 Info.setText(url);
				 Info2.setText("账号(Account):admin,密码(Password):123456");
			     label1.setVisible(true);
			     label2.setVisible(true);
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
        
        this.setVisible(true);
        // 关闭窗口并退出程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TracbdsTrayIcon.createTrayIcon(this);
	}
	
	private static boolean verify() {
        String ip=PropUtils.getProp("tracbds.database.ip","127.0.0.1");
        String port=PropUtils.getProp("tracbds.database.port","3306");
        String username=PropUtils.getProp("tracbds.database.username","root");
        String password=PropUtils.getProp("tracbds.database.password","123456");
        return DialogInitializeDatabase.isConnection(ip, port, username, password);
         
	}

	public static void main(String[] args) {
    	FlatLightLaf.setup();
		new TracbdsFrame();
	}
}
