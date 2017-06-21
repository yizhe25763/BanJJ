package com.shzy.bjj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shzy.bjj.activity.login.ReLoginlDialog;
import com.shzy.bjj.bean.InfoBean;
import com.shzy.bjj.bean.InfoResponse;
import com.shzy.bjj.bean.RequestFailBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.map.MapApplication;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.NetworkTool;
import com.shzy.bjj.tools.PreferencesTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.view.MainBottomTabLayout;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 
 * @brief 整个应用程序Applicaiton
 * @author Fanhao.Yi
 * @date 2015年4月23日下午7:01:20
 * @version V1.0
 */
public class MyApplication extends Application {

	/** 对外提供整个应用生命周期的Context **/
	private static Context instance;
	/** 整个应用全局可访问数据集合 **/
	private static Map<String, Object> gloableData = new HashMap<String, Object>();

	protected static String LOGIN_KEY = null;
	protected static Long USER_ID = null;
	protected static boolean isLogin = false;
	MapView mMapView = null;
	// BaiduMap mBaiduMap;
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private static BDLocation location;

	public static Bundle getMetaData(Context context) {
		try {
			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (applicationInfo != null) {
				return applicationInfo.metaData;
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return new Bundle();
	}

	public String getCity() {

		return getLocation().getCity();

	}

	public BDLocation getLocation() {
		return location;
	}

	public void setLocation(BDLocation location) {
		MyApplication.location = location;
	}

	public String getLOGIN_KEY() {
		return LOGIN_KEY;
	}

	public static void setLOGIN_KEY(String lOGIN_KEY) {
		LOGIN_KEY = lOGIN_KEY;
	}

	public static Long getUSER_ID() {
		return USER_ID;
	}

	public static void setUSER_ID(Long uSER_ID) {
		USER_ID = uSER_ID;
	}

	public void initLocationData() {
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setAddrType("all");// 获取详细地址需添加此行代码
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			if (location != null) {
				setLocation(location);
				mLocClient.stop();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	public boolean isLogin() {
		String login_key = PreferencesTool
				.getString(instance, DataTag.LOGINKEY);
		if (StringTool.isNoBlankAndNoNull(login_key)) {
			setLOGIN_KEY(login_key);
			setLogin(true);
		} else {
			setLOGIN_KEY(null);
			setLogin(false);

		}
		return isLogin;
	}

	public static void setLogin(boolean isLogin) {
		MyApplication.isLogin = isLogin;
	}

	/**
	 * 对外提供Application Context
	 * 
	 * @return
	 */
	public static Context gainContext() {
		return instance;
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		initImageLoader(getApplicationContext());
		// SDKInitializer.initialize(getApplicationContext());
		// 设备编号
		setDeviceId();
		// 获取设备详细信息
		setDeviceInfo();
		// 定位
		// initLocationData();
		initEngineManager(this);
		// 收集错误日志
		CrashReport
				.initCrashReport(getApplicationContext(), "900006779", false);
		// if (isLogin()) {
		// getInformationData(getLOGIN_KEY(), 1, 100, "");
		// }else{
		// MainBottomTabLayout.hasMineMessage = false;
		// }

	}

	// private void getInformationData(String loginKey, int page, int size,
	// String ids) {
	// Map maps = new HashMap<String, String>();
	// maps.put(DataTag.LOGINKEY, loginKey);
	// maps.put(DataTag.PAGE, page);
	// maps.put(DataTag.SIZE, size);
	// maps.put(DataTag.IDS, ids);
	// HttpTool.post(getApplicationContext(), Apis.GET_USER_INFO, maps,
	// new StringHandler(null) {
	//
	// @Override
	// public void success(String response) {
	// if (StringTool.isNoBlankAndNoNull(response)) {
	// InfoResponse infoResponse = JSON.decode(response,
	// InfoResponse.class);
	// int count = infoResponse.getCount();
	// if (count > 0) {
	// List<InfoBean> list = infoResponse.getList();
	// MainBottomTabLayout.hasMineMessage = true;
	// }
	// }
	// }
	//
	// @Override
	// public void failure(int statusCode, String responseBody,
	// Throwable e) {
	//
	// }
	// });
	//
	// }

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	// 解析assets目录下的XML文件
	private List<Map> readAssetsXML(Context context, String fileName) {
		XmlPullParser parser = Xml.newPullParser();

		try {
			InputStream inStream = context.getAssets().open(fileName);
			parser.setInput(context.getAssets().open(fileName), "UTF-8");
			int eventType = parser.getEventType();

			Map currentMap = null;
			List<Map> list = null;

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
					list = new ArrayList<Map>();
					break;

				case XmlPullParser.START_TAG:// 开始元素事件
					currentMap = new HashMap();
					int count = parser.getAttributeCount();
					for (int i = 0; i < count; i++) {
						currentMap.put(parser.getAttributeName(i),
								parser.getAttributeValue(i));
					}
					break;

				case XmlPullParser.END_TAG:// 结束元素事件
					if (currentMap != null) {
						list.add(currentMap);
						currentMap = null;
					}
					break;
				}
				eventType = parser.next();
			}
			inStream.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

	}

	/**
	 * 获取设备唯一编号
	 */
	private void setDeviceId() {
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Activity.TELEPHONY_SERVICE);
		HttpTool.UDID_VALUES = tm.getDeviceId();
		if (HttpTool.UDID_VALUES == null) {
			// android pad
			HttpTool.UDID_VALUES = Secure.getString(this.getContentResolver(),
					Secure.ANDROID_ID);
		}

	}

	/**
	 * 获取设备详细信息
	 */
	private void setDeviceInfo() {
		HttpTool.UADETAIL_VALUES = Build.MODEL + Build.VERSION.RELEASE;
	}

	/**
	 * 获取网络是否已连接
	 * 
	 * @return
	 */
	public static boolean isNetworkReady() {
		return NetworkTool.getInstance().init(instance).isConnected();
	}

	/******************************************************* Application数据操作API（开始） ********************************************************/

	/**
	 * 往Application放置数据（最大不允许超过5个）
	 * 
	 * @param strKey
	 *            存放属性Key
	 * @param strValue
	 *            数据对象
	 */
	public static void assignData(String strKey, Object strValue) {
		if (gloableData.size() > 5) {
			throw new RuntimeException("超过允许最大数");
		}
		gloableData.put(strKey, strValue);
	}

	/**
	 * 从Applcaiton中取数据
	 * 
	 * @param strKey
	 *            存放数据Key
	 * @return 对应Key的数据对象
	 */
	public static Object gainData(String strKey) {
		return gloableData.get(strKey);
	}

	/*
	 * 从Application中移除数据
	 */
	public static void removeData(String key) {
		if (gloableData.containsKey(key))
			gloableData.remove(key);
	}

	/******************************************************* Application数据操作API（结束） ********************************************************/

	/**
	 * 根据对应的服务端错误编码返回对应的错误信息
	 * 
	 * @param errorCode
	 *            服务端错误编码
	 * @return 对应的错误信息提示
	 */
	public static String showErrorMsg(Context context, int errorCode) {
		switch (errorCode) {
		case DataConst.ERROR_ONE:
			return instance.getResources().getString(R.string.ERROR_ONE_VALUES);
		case DataConst.ERROR_TWO:
			return instance.getResources().getString(R.string.ERROR_TWO_VALUES);
		case DataConst.ERROR_THREE:
			return instance.getResources().getString(
					R.string.ERROR_THREE_VALUES);
		case DataConst.ERROR_FOUR:
			return instance.getResources()
					.getString(R.string.ERROR_FOUR_VALUES);
		case DataConst.ERROR_FIVE:
			return instance.getResources()
					.getString(R.string.ERROR_FIVE_VALUES);
		case DataConst.ERROR_SIX:
			return instance.getResources().getString(R.string.ERROR_SIX_VALUES);
		case DataConst.ERROR_SEVEN:
			return instance.getResources().getString(
					R.string.ERROR_SEVEN_VALUES);
		case DataConst.ERROR_EIGHT:
			return instance.getResources().getString(
					R.string.ERROR_EIGHT_VALUES);
		case DataConst.ERROR_NINE:
			return instance.getResources()
					.getString(R.string.ERROR_NINE_VALUES);
		case DataConst.ERROR_TEN:
			return instance.getResources().getString(R.string.ERROR_TEN_VALUES);
		case DataConst.ERROR_EVEVEN:
			return instance.getResources().getString(
					R.string.ERROR_EVEVEN_VALUES);
		case DataConst.ERROR_TWELVE:
			return instance.getResources().getString(
					R.string.ERROR_TWELVE_VALUES);
		case DataConst.ERROR_THIRTEEN:
			return instance.getResources().getString(
					R.string.ERROR_THIRTEEN_VALUES);
		case DataConst.ERROR_FOURTEEN:
			return instance.getResources().getString(
					R.string.ERROR_FOURTEEN_VALUES);
		case DataConst.ERROR_FIFTEEN:
			ReLoginlDialog.startActivity(AppManager.getAppManager()
					.currentActivity());
			setLogin(false);
			setLOGIN_KEY("");
			PreferencesTool.putString(context, DataTag.LOGINKEY, "");
			return instance.getResources().getString(
					R.string.ERROR_FIFTEEN_VALUES);
		case DataConst.ERROR_SIXTEEN:

			return instance.getResources().getString(
					R.string.ERROR_SIXTEEN_VALUES);
		case DataConst.ERROR_SEVENTEEN:

			return instance.getResources().getString(
					R.string.ERROR_SEVENTEEN_VALUES);
		case DataConst.ERROR_EIGHTEEN:

			return instance.getResources().getString(
					R.string.ERROR_EIGHTEEN_VALUES);
		case DataConst.ERROR_NINETEEN:

			return instance.getResources().getString(
					R.string.ERROR_NINETEEN_VALUES);
		case DataConst.ERROR_TWENTY:

			return instance.getResources().getString(
					R.string.ERROR_TWENTY_VALUES);
		case DataConst.ERROR_TWENTY_ONE:

			return instance.getResources().getString(
					R.string.ERROR_TWENTY_ONE_VALUES);
		case DataConst.ERROR_TWENTY_TWO:

			return instance.getResources().getString(
					R.string.ERROR_TWENTY_TWO_VALUES);
		case DataConst.ERROR_TWENTY_THREE:

			return instance.getResources().getString(
					R.string.ERROR_TWENTY_THREE_VALUES);
		case DataConst.ERROR_TWENTY_FOUR:

			return instance.getResources().getString(
					R.string.ERROR_TWENTY_FOUR_VALUES);
		case DataConst.ERROR_TWENTY_FIVE:

			return instance.getResources().getString(
					R.string.ERROR_TWENTY_FIVE_VALUES);
		case DataConst.ERROR_TWENTY_SIX:

			return instance.getResources().getString(
					R.string.ERROR_TWENTY_SIX_VALUES);
		case DataConst.ERROR_TWENTY_SEVEN:

			return instance.getResources().getString(
					R.string.ERROR_TWENTY_SEVEN_VALUES);
		case DataConst.ERROR_TWENTY_EIGHT:

			return instance.getResources().getString(
					R.string.ERROR_TWENTY_EIGHT_VALUES);

		}
		return "";

	}

	/**
	 * 通用服务端错误信息提示
	 * 
	 * @param context
	 *            上下文对象
	 * @param responseBody
	 *            错误数据
	 */
	public static void ShowFailMessage(Context context, String responseBody) {
		if (StringTool.isNoBlankAndNoNull(responseBody)) {
			RequestFailBean result = JSON.decode(responseBody,
					RequestFailBean.class);
			Toast.makeText(
					context,
					MyApplication.showErrorMsg(context, result.getError_code()),
					Toast.LENGTH_LONG).show();

		}

	}

	private static MapApplication mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(new MyGeneralListener())) {
		}
	}

	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(
						MapApplication.getInstance().getApplicationContext(),
						"", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(
						MapApplication.getInstance().getApplicationContext(),
						"", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError != 0) {
				// Toast.makeText(
				// MapApplication.getInstance().getApplicationContext(),
				// " " + iError, Toast.LENGTH_LONG).show();
				// MapApplication.getInstance().m_bKeyRight = false;
			} else {
				// MapApplication.getInstance().m_bKeyRight = true;
				// Toast.makeText(
				// MapApplication.getInstance().getApplicationContext(),
				// "", Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * -------------------------------对象缓存--------------------------------------
	 * -
	 */
	public boolean isReadDataCache(String cachefile) {
		return readObject(cachefile) != null;
	}

	private boolean isExistDataCache(String cachefile) {
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public Serializable readObject(String file) {
		if (!isExistDataCache(file)) {
			return null;
		}
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();

			if (e instanceof InvalidClassException) {
				File data = getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public void deleteObject(String file) {
		if (!isExistDataCache(file)) {
			return;
		}
		File data = getFileStreamPath(file);
		data.delete();
	}
	/**
	 * ---------------------------------对象缓存结束----------------------------------
	 */
}
