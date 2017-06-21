package com.shzy.bjj.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.shzy.bjj.constant.DataConst;

/**
 * 
 * @brief
 * @author Fanhao Yi
 * @data 2015年6月16日上午10:17:53
 * @version V1.0
 */
public final class Utils {
	private static String sAppName = "";

	public static File getAppCacheDir(Context context, String subName) {
		if (!sdAvailible()) {
			return null;
		}
		File sd = Environment.getExternalStorageDirectory();
		File dir = new File(sd, getAppName(context));
		File sub = new File(dir, subName);
		sub.mkdirs();
		return sub;
	}

	public static boolean sdAvailible() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		} else {
			return false;
		}
	}

	public static String encrypt(String str) {
		return str;
	}

	public static String buildSystemInfo(Context context) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("\n");
		buffer.append("#-------system info-------");
		buffer.append("\n");
		buffer.append("version-name:");
		buffer.append(Utils.getVersionName(context));
		buffer.append("\n");
		buffer.append("version-code:");
		buffer.append(Utils.getVersionCode(context));
		buffer.append("\n");
		buffer.append("system-version:");
		buffer.append(Utils.getSystemVersion(context));
		buffer.append("\n");
		buffer.append("model:");
		buffer.append(Utils.getModel(context));
		buffer.append("\n");
		buffer.append("density:");
		buffer.append(Utils.getDensity(context));
		buffer.append("\n");
		buffer.append("imei:");
		buffer.append(Utils.getIMEI(context));
		buffer.append("\n");
		buffer.append("screen-height:");
		buffer.append(Utils.getScreenHeight(context));
		buffer.append("\n");
		buffer.append("screen-width:");
		buffer.append(Utils.getScreenWidth(context));
		buffer.append("\n");
		buffer.append("unique-code:");
		buffer.append(Utils.getUniqueCode(context));
		buffer.append("\n");
		buffer.append("mobile:");
		buffer.append(Utils.getMobile(context));
		buffer.append("\n");
		buffer.append("imsi:");
		buffer.append(Utils.getProvider(context));
		buffer.append("\n");
		buffer.append("isWifi:");
		buffer.append(Utils.isWifi(context));
		buffer.append("\n");
		return buffer.toString();
	}

	public static String getUniqueCode(Context context) {
		if (context == null)
			return null;
		String imei = getIMEI(context);
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String mUniqueCode = imei + "_" + info.getMacAddress();
		return mUniqueCode;
	}

	public static boolean isWifi(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	public static String getMobile(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getLine1Number();
	}

	public static String getProvider(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSubscriberId();
	}

	public static final String getIMEI(final Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getDeviceId();
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.heightPixels;
	}

	public static String getSystemVersion(Context context) {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getModel(Context context) {
		return android.os.Build.MODEL != null ? android.os.Build.MODEL.replace(
				" ", "") : "unknown";
	}

	public static float getDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public static String getVersionName(Context context) {
		try {
			PackageInfo pinfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(),
							PackageManager.GET_CONFIGURATIONS);
			return pinfo.versionName;
		} catch (NameNotFoundException e) {
		}

		return "";
	}

	public static String getAppName(Context context) {
		if (TextUtils.isEmpty(sAppName)) {
			sAppName = "com_forlong401_log";
			try {
				PackageInfo pinfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(),
						PackageManager.GET_CONFIGURATIONS);
				String packageName = pinfo.packageName;
				if (!TextUtils.isEmpty(packageName)) {
					sAppName = packageName.replaceAll("\\.", "_");
				}
			} catch (NameNotFoundException e) {
			}
		}

		return sAppName;
	}

	public static int getVersionCode(Context context) {
		try {
			PackageInfo pinfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(),
							PackageManager.GET_CONFIGURATIONS);
			return pinfo.versionCode;
		} catch (NameNotFoundException e) {
		}

		return 1;
	}

	/**
	 * 
	 * @Title: checkPhotoHasSDcard
	 * @Description: 检测是否存在sd中
	 * @param fileName
	 * @return
	 */
	public static boolean checkPhotoHasSDcard(String fileName) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File directory = new File(DataConst.PATH);
			File file = new File(DataConst.PATH + fileName);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			if (file.exists()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: getPhotoFromSDcard
	 * @Description:根据文件名称获得文件从sd卡
	 * @param fileName
	 * @return
	 */
	public static Bitmap getPhotoFromSDcard(String fileName) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File directory = new File(DataConst.PATH);
			File file = new File(DataConst.PATH + fileName);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			if (file.exists()) {
				Bitmap bitmap = BitmapFactory
						.decodeFile(file.getAbsolutePath());
				return bitmap;
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: savePhotoToSDcard
	 * @Description: 保持图片到ＳＤ卡中
	 * @param fileName
	 * @param mBitmap
	 */
	public static void savePhotoToSDcard(String fileName, Bitmap mBitmap) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File directory = new File(DataConst.PATH);
			File file = new File(DataConst.PATH + fileName);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				FileOutputStream outStream = new FileOutputStream(file);
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
				outStream.flush();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
