package com.shzy.bjj.tools;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.activity.home.LocationActivity;
import com.shzy.bjj.constant.DataConst;

/**
 * 
 * @brief APP工具类
 * @author Fanhao.Yi
 * @date 2015年4月27日下午4:57:15
 * @version V1.0
 */
public class AppTool {

	private AppTool() {
		throw new AssertionError();
	}

	/**
	 * 判断当前进程
	 * 
	 * @param context
	 * @param processName
	 * @return
	 */
	public static boolean isNamedProcess(Context context, String processName) {
		if (context == null) {
			return false;
		}

		int pid = android.os.Process.myPid();
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processInfoList = manager
				.getRunningAppProcesses();
		if (ListTool.isEmpty(processInfoList)) {
			return false;
		}

		for (RunningAppProcessInfo processInfo : processInfoList) {
			if (processInfo != null
					&& processInfo.pid == pid
					&& ObjectTool
							.isEquals(processName, processInfo.processName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断当前应用是否切换至后台
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isApplicationInBackground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskList = am.getRunningTasks(1);
		if (taskList != null && !taskList.isEmpty()) {
			ComponentName topActivity = taskList.get(0).topActivity;
			if (topActivity != null
					&& !topActivity.getPackageName().equals(
							context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 双击退出功能
	 * 
	 * @param activity
	 *            当前视图
	 * @param instance
	 *            当前上下文对象
	 */
	public static void exitBy2Click(Activity activity, Context instance) {
		Timer tExit = null;
		if (DataConst.isExit == false) {
			DataConst.isExit = true; // 准备退出
			Toast.makeText(instance, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					DataConst.isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			PreferencesTool.putBoolean(activity, "forcedUpdating", false);
			LocationActivity.isDebug = false;
			AppManager.getAppManager().AppExit(instance);
		}
	}

}
