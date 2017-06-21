package com.shzy.bjj.tools;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.shzy.bjj.MyApplication;
import com.shzy.bjj.tools.AlertTool.ILoadingOnKeyListener;

/**
 * 
 * @brief activity跳转工具
 * @author Fanhao.Yi
 * @date 2015年4月23日下午7:03:49
 * @version V1.0
 */
public class IntentTool {
	/** 激活Activity组件意图 **/
	private Intent mIntent = new Intent();
	/** 上下文 **/
	private Activity mContext = null;
	/** 整个应用Applicaiton **/
	private MyApplication mApplication = null;

	public IntentTool(Activity mContext) {
		this.mContext = mContext;
		mApplication = (MyApplication) this.mContext.getApplicationContext();
	}

	/**
	 * 跳转Activity
	 * 
	 * @param activity
	 *            需要跳转至的Activity
	 */
	public void forward(Class<? extends Activity> activity) {
		mIntent.setClass(mContext, activity);
		mContext.startActivity(mIntent);
	}

	/**
	 * 设置传递参数
	 * 
	 * @param key
	 *            参数key
	 * @param value
	 *            数据传输对象
	 */
	public void addParameter(String key, Bundle value) {
		mIntent.putExtra(key, value);
	}

	/**
	 * 设置传递参数
	 * 
	 * @param key
	 *            参数key
	 * @param value
	 *            数据传输对象
	 */
	public void addParameter(String key, Serializable value) {
		mIntent.putExtra(key, value);
	}

	/**
	 * 设置传递参数
	 * 
	 * @param key
	 *            参数key
	 * @param value
	 *            数据传输对象
	 */
	public void addParameter(String key, String value) {
		mIntent.putExtra(key, value);
	}

	/**
	 * 设置全局Application传递参数
	 * 
	 * @param strKey
	 *            参数key
	 * @param value
	 *            数据传输对象
	 */
	public void addGloableAttribute(String strKey, Object value) {
		mApplication.assignData(strKey, value);
	}

	/**
	 * 获取跳转时设置的参数
	 * 
	 * @param strKey
	 * @return
	 */
	public Object getGloableAttribute(String strKey) {
		return mApplication.gainData(strKey);
	}

	/**
	 * 弹出等待对话框
	 * 
	 * @param message
	 *            提示信息
	 */
	public void showLoading(String message) {
		AlertTool.loading(mContext, message);
	}

	/**
	 * 弹出等待对话框
	 * 
	 * @param message
	 *            提示信息
	 * @param listener
	 *            按键监听器
	 */
	public void showLoading(String message, ILoadingOnKeyListener listener) {
		AlertTool.loading(mContext, message, listener);
	}

	/**
	 * 更新等待对话框显示文本
	 * 
	 * @param message
	 *            需要更新的文本内容
	 */
	public void updateLoadingText(String message) {
		AlertTool.updateProgressText(message);
	}

	/**
	 * 关闭等待对话框
	 */
	public void closeLoading() {
		AlertTool.closeLoading();
	}

}
