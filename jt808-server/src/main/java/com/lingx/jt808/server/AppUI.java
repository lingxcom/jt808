package com.lingx.jt808.server;

import com.lingx.jt808.server.swing.TracSeekFrame;
import com.lingx.utils.PropUtils;

public class AppUI {
	public static void main(String[] args) {

    	PropUtils.init("/conf/config.properties");
		TracSeekFrame.main(args);
	}

}
