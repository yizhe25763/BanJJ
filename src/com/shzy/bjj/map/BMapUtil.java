package com.shzy.bjj.map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;

@SuppressLint("NewApi")
public class BMapUtil {
    	
	/**
	 * ��view �õ�ͼƬ
	 * @param view
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}
}

