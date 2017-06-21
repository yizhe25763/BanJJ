package com.shzy.bjj.tools;

import android.view.View;
import android.widget.PopupWindow;

public class PopupWindowUtil {
	public PopupWindowUtil() {
	}

	public static boolean toggle(PopupWindow dialog, View parent, int g, int x,
			int y) {
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				return false;
			} else {
				dialog.showAtLocation(parent, g, x, y);
				return true;
			}
		} else {
			return false;
		}
	}

	public static boolean toggleAsDropDown(PopupWindow dialog, View parent,
			int x, int y) {
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				return false;
			} else {
				dialog.showAsDropDown(parent, x, y);
				return true;
			}
		} else {
			return false;
		}
	}

	public static boolean toggleAsDropDown(PopupWindow dialog, View parent) {
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				return false;
			} else {
				dialog.showAsDropDown(parent);
				return true;
			}
		} else {
			return false;
		}
	}

	public static void dismiss(PopupWindow dialog) {
		if (dialog != null && dialog.isShowing()) {
			try {
				dialog.dismiss();
			} catch (Throwable var2) {
				var2.printStackTrace();
			}
		}

	}
}
