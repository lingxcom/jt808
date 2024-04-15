package com.lingx.jt808.html.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

	public static void main(String[] args) {
		File dir=new File("D:\\workspace_jt808vue\\jt808");
		
		listFile(dir);
	}

	
	public static void listFile(File dir) {

			
			File[] list=dir.listFiles();
			for(File file:list) {
				if(file.isDirectory()) {
					listFile(file);
				}else {
					try {
						if(!file.getName().endsWith(".java"))continue;
						String temp=org.apache.commons.io.FileUtils.readFileToString(file);
						if(temp.contains("@Resource")) {
							System.out.println(file.getAbsolutePath());
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
	}
}
