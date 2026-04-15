package com.lingx.jt808.core.support;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

public class SendMail extends Thread {
    // 发送邮件的邮箱
    private String from = "";
    // 授权码
    private String password = "";
    // 发送邮件的服务器地址
    private String host = "";

    private String mail;
    private String title;
    private String content;
    public SendMail(String mail,String title,String content,String host,String from,String password) {
    	this.mail=mail;
    	this.title=title;
    	this.content=content;
    	
    	this.host=host;
    	this.from=from;
    	this.password=password;
    }

    @Override
    public void run() {
        try {
            Properties prop = new Properties();
            prop.setProperty("mail.host", host); // 设置qq邮件服务器
            prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
            prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码

            // 关于qq邮箱, 还要设置SSL加密
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);

            // 使用 JavaMail 发送邮件的5个步骤

            // 1. 定义整个应用程序所需要的环境信息的 Session 对象
            // 这一步是qq邮箱才有, 其他邮箱不用
            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    // 发件人邮箱 用户名和授权码
                    return new PasswordAuthentication(from, password);
                }
            });

            // 开启 Session debugger 模式, 可以看到邮件发送的运行状态
            session.setDebug(true);

            // 2. 通过 Session得到 transport 对象
            Transport transport = session.getTransport();

            // 3. 使用邮箱用户名和授权码连上邮件服务器 (登陆)
            transport.connect(host, from, password);

            // 4. 创建邮件: 写邮件
            MimeMessage message = new MimeMessage(session);


            // ======== 写邮件 ========
            // 设置邮件的发件人
            message.setFrom(new InternetAddress(from));
            // 设置邮件的收件人
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(this.mail));

            // 邮件标题
            message.setSubject(this.title);
            // 邮件文本内容
            message.setContent(this.content, "text/html; charset=UTF-8");

            // 5. 发送邮件
            transport.sendMessage(message, message.getAllRecipients());

            // 关闭连接
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    
}