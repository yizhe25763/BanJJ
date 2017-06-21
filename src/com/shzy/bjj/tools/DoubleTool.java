package com.shzy.bjj.tools;

import java.text.DecimalFormat;

public class DoubleTool {

	static DecimalFormat df = new DecimalFormat("######0.00");

	public static String channelPrice(Double price) {
		return df.format(price);
	}
}
