package com.shzy.bjj.share;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class SharePopupWindow extends PopupWindow implements OnClickListener,
		PlatformActionListener, Handler.Callback {
	private Context context;
	private String shareContent;// 分享的显示文本
	private String shareUrl;// 分享的文本的拼接url
	private String title;
	private String content;
	private String imageUrl;

	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/BJJ/";
	private Bitmap mBitmap;
	private String mFileName;
	private String mSaveMessage;
	private Thread connectThread;
	private Thread saveThread;

	// public String filePath =
	// "http://imgsrc.baidu.com/forum/pic/item/b58acd628535e5dd73d2735e76c6a7efcf1b627a.jpg";
	public String fileName = "tests.jpg";

	public SharePopupWindow(Activity context, String shareContent,
			String shareUrl, String param1, String param2) {
		this.context = context;
		this.title = shareContent;
		this.shareUrl = shareUrl;
		this.content = param1;
		this.imageUrl = param2;
		saveThread = new Thread(saveFileRunnable);
		saveThread.start();
		showShare();
	}

	private Runnable saveFileRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				mBitmap = BitmapFactory.decodeStream(getImageStream(imageUrl));
				saveFile(mBitmap, fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/*
	 * 从网络获取图片
	 */
	protected InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(10 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	/*
	 * 保存文件
	 */
	protected void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(ALBUM_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(ALBUM_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	private void showShare() {
		ShareSDK.initSDK(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setTitle(title);
		oks.setTitleUrl(shareUrl);
		oks.setText(content + "  " + shareUrl);
		oks.setImagePath(ALBUM_PATH + "/tests.jpg");
		oks.setImageUrl(imageUrl);
		oks.setUrl(shareUrl);
		oks.setSite(content);
		oks.setSiteUrl(shareUrl);
		oks.setSilent(true);
		oks.show(context);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
