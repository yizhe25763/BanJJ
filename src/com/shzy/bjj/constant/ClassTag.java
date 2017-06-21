package com.shzy.bjj.constant;

import android.app.Activity;

import com.shzy.bjj.activity.GuideActivity;
import com.shzy.bjj.activity.LauncherActivity;
import com.shzy.bjj.activity.home.AppointmentListActivity;
import com.shzy.bjj.activity.login.LoginActivity;
import com.shzy.bjj.activity.login.PerfectInformationActivity;

/**
 * 
 * @brief 类名常量
 * @author Fanhao Yi
 * @data 2015年6月10日下午3:42:05
 * @version V1.0
 */
public class ClassTag {
	/**
	 * 引导界面
	 */
	public static Class<? extends Activity> GUIDEACTIVITY = GuideActivity.class;
	/**
	 * 程序启动页面
	 */
	public static Class<? extends Activity> LAUNCHERACTIVITY = LauncherActivity.class;

	/**
	 * 登录界面
	 */
	public static Class<? extends Activity> LOGINACTIVITY = LoginActivity.class;

	/**
	 * 完善资料界面
	 */
	public static Class<? extends Activity> PERFECTINFORMATIONACTIVITY = PerfectInformationActivity.class;

	/**
	 * 订单地址界面
	 */
	// public static Class<? extends Activity> ADDRESSACTIVITY =
	// AppointmentAddressActivity.class;

	/**
	 * 订单列表
	 */
	public static Class<? extends Activity> ORDERLISTACTIVITY = AppointmentListActivity.class;

}
