package com.shzy.bjj.tools;

import android.content.Context;
import android.widget.Toast;

public class ToastTool {

	public static void toastMessage(Context context, String message) {
		if (StringTool.isNoBlankAndNoNull(message)) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	public static void toastMessage(Context context, int message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void toastMessage(Context context, String message, int time) {
		if (StringTool.isNoBlankAndNoNull(message)) {
			Toast.makeText(context, message, time).show();
		}
	}

}
