package com.shzy.bjj.activity.teacher;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.shzy.bjj.R;
import com.shzy.bjj.tools.PopupWindowUtil;

public class PopupWindowDialog extends PopupWindowUtil {

	public static PopupWindow createPopupWindowFullScreen(
			final Activity activity, int layoutResId) {
		View root = LayoutInflater.from(activity).inflate(layoutResId, null);
		final PopupWindow window = new PopupWindow(root,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, true);
		window.setBackgroundDrawable(new ColorDrawable());
		window.setOutsideTouchable(true);
		root.findViewById(R.id.dismiss).setOnTouchListener(
				new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (window.isShowing()) {
							window.dismiss();
						}
						return false;
					}
				});
		return window;
	}

	public static PopupWindow createPopupWindow(final Activity activity,
			int layoutResId) {
		View root = LayoutInflater.from(activity).inflate(layoutResId, null);
		final PopupWindow window = new PopupWindow(root,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT, true);
		window.setBackgroundDrawable(new ColorDrawable());
		window.setOutsideTouchable(true);
		return window;
	}

}
