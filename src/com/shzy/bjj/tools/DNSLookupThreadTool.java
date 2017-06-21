package com.shzy.bjj.tools;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.util.Log;


/**
 * 
 * 
 * @brief  判断连接上网络后是否有有效的网络连接
 * @author Fanhao Yi
 * @data 2015年8月18日下午1:47:16
 * @version V1.0
 */
public class DNSLookupThreadTool extends Thread {
	private boolean reachAble = false;
	private String hostname;

	public DNSLookupThreadTool(String hostname) {
		this.hostname = hostname;
	}

	public void run() {
		try {
			InetAddress add = InetAddress.getByName(hostname);
			this.reachAble = true;
		} catch (UnknownHostException e) {
		}
	}

	public synchronized boolean isReachAble() {
		return this.reachAble;
	}

	public static final boolean CheckInternet(Context mContext) {
		DNSLookupThreadTool thread = new DNSLookupThreadTool("www.baidu.com");
		try {
			thread.start();
			thread.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (thread.isReachAble()) {
			return true;
		}
		return false;
	}

}
