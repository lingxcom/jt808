package com.lingx.jt808.core.cmd.ext;

import java.util.Map;

import com.lingx.jt808.core.cmd.Cmd8105;

public class ExtCmd8105_2 extends Cmd8105{

	public static void main(String args[]) {
		System.out.println("7e8105006a003061 1463240006016674 703a2f2f7362683a 3132334064657669 63652e6964726976 6572676f2e636f6d 3a31323030302f75 7067726164652f48 5943582f7362682d 6d6476722d686933 353231612d545032 3330363239313248 5943582d4d353135 582e7a69703b3b3b 3b3b3b3b3b3b3ba3 7e".replace(" ", "").toUpperCase());
	}
	public ExtCmd8105_2(String tid,Map<String,Object> params) {
		super(tid, 2, params.get("p1").toString());
	}

}
