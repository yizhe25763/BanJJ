package com.shzy.bjj.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.shzy.bjj.R;

public class DialogUtils {
	// 获取系统图片方式（相机，相册，剪裁）
	public static final int REQUEST_CODE_CAMERA = 0x101;
	public static final int REQUEST_CODE_IMAGE = 0x102;
	public static final int REQUEST_CODE_CROP = 0x103;
	public static final int REQUEST_CODE_COMMENT_LOGIN = 0x104;

	public static Dialog createBottomDialog(Context context, int layoutId) {
		return setBottomDialog(new Dialog(context,
				R.style.style_dialog_full_screen), context, layoutId);
	}

	public static Dialog setBottomDialog(Dialog dialog, Context context,
			int layoutId) {
		View layout = LayoutInflater.from(context).inflate(layoutId, null);
		layout.setMinimumWidth(10000);

		Window w = dialog.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		lp.y = -1000;
		lp.gravity = Gravity.BOTTOM;

		dialog.onWindowAttributesChanged(lp);
		dialog.setContentView(layout);
		dialog.setCanceledOnTouchOutside(true);

		return dialog;
	}

	public static File showImageDialog(final Activity context) {
		final File file = new File(Environment.getExternalStorageDirectory(),
				"jpg");
		// final File file = AndroidUtil.getApplicationTempFile(context, ".tmp",
		// "jpg");
		final Dialog dialog = createBottomDialog(context,
				R.layout.setting_dialog_bottom);
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				if (id == R.id.dialog_btn_top) {
					takePicture(context, file);
				} else if (id == R.id.dialog_btn_bottom) {
					selectPicture(context);
				}
				dialog.dismiss();
			}
		};
		dialog.findViewById(R.id.dialog_btn_top).setOnClickListener(listener);
		((Button) (dialog.findViewById(R.id.dialog_btn_top))).setText("拍照");
		dialog.findViewById(R.id.dialog_btn_bottom)
				.setOnClickListener(listener);
		((Button) (dialog.findViewById(R.id.dialog_btn_bottom))).setText("图片");
		dialog.findViewById(R.id.dialog_btn_cancer)
				.setOnClickListener(listener);
		((TextView) dialog.findViewById(R.id.dialog_tv_title)).setText("选则图片");
		dialog.show();
		return file;
	}

	/**
	 * 拍照
	 */
	public static void takePicture(Activity activity, File file) {
		if (!isExitsSdcard()) {
			ToastTool.toastMessage(activity, "SD卡不存在，不能拍照");
			return;
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}

	/**
	 * 图库选择
	 */
	public static void selectPicture(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		activity.startActivityForResult(intent, REQUEST_CODE_IMAGE);
	}

	/**
	 * 裁剪图片
	 */
	public static void startPhotoCrop(Activity activity, Uri uri, int size,
			File file) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("noFaceDetection", true);
		// 部分机型携带bitmap的数据时会出现java.lang.SecurityException，所以这里改用url传递
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("output", Uri.fromFile(file));
		// intent.putExtra("return-data", false);
		activity.startActivityForResult(intent, REQUEST_CODE_CROP);
	}

	/**
	 * 图片转换字符串
	 */
	public static String imgToBase64(Bitmap bitmap) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 检测Sdcard是否存在
	 */
	public static boolean isExitsSdcard() {
		return android.os.Environment.MEDIA_MOUNTED
				.equals(android.os.Environment.getExternalStorageState());
	}
}
