package com.lingx.jt808.server;

import com.lingx.jt808.server.swing.TracSeekFrame;
import com.lingx.utils.PropUtils;

public class DevAppUI {
	public static void main(String[] args) {

    	PropUtils.init("/conf/config.properties");
		TracSeekFrame.configFile="spring_dev.xml";
		TracSeekFrame.main(args);
	}

}
