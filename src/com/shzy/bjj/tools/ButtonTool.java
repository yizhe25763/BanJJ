package com.shzy.bjj.tools;

public class ButtonTool {
	private static long lastClickTime;

	/**
	 * 判断重复点击
	 * @return
	 */
	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
