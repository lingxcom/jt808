package com.tracbds.server.swing;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TracbdsTrayIcon {

	public static void createTrayIcon(JFrame frame) {
		// 检查系统托盘支持
        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null, "系统不支持托盘图标");
            return;
        }

        // 创建系统托盘图标
        SystemTray tray = SystemTray.getSystemTray();
        
        try {
            // 加载图标（替换为你的图标路径）
            BufferedImage trayIconImage = ImageIO.read(
            		TracbdsTrayIcon.class.getResource("/images/108_108.png"));
            int trayIconWidth = new TrayIcon(trayIconImage).getSize().width;
            Image scaledImage = trayIconImage.getScaledInstance(
                trayIconWidth, -1, Image.SCALE_SMOOTH);
            
            TrayIcon trayIcon = new TrayIcon(scaledImage, "Gps31");
            
            // 创建弹出菜单
            PopupMenu popup = new PopupMenu();
            MenuItem openItem = new MenuItem("Open");
            MenuItem exitItem = new MenuItem("Exit");
            
            // 添加菜单项事件
            openItem.addActionListener(e -> {
                frame.setVisible(true);
                frame.setExtendedState(JFrame.NORMAL);
            });
            
            exitItem.addActionListener(e -> {
                tray.remove(trayIcon);
                System.exit(0);
            });
            
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            
            trayIcon.setPopupMenu(popup);
            
            // 添加双击事件
            trayIcon.addActionListener(e -> {
                frame.setVisible(true);
                frame.setExtendedState(JFrame.NORMAL);
            });
            
            // 添加托盘图标
            tray.add(trayIcon);
            
            // 窗口最小化时隐藏到托盘
            frame.addWindowListener(new WindowAdapter() {
                public void windowIconified(WindowEvent e) {
                    frame.setVisible(false);
                }
            });
            
            // 设置窗口关闭时隐藏到托盘
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            
            frame.setVisible(true);
            
        } catch (IOException | AWTException e) {
            JOptionPane.showMessageDialog(null, "创建托盘图标失败: " + e.getMessage());
        }
	}
}
