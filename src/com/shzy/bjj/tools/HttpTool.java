package com.shzy.bjj.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import android.content.Context;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.constant.Apis;

/**
 * 
 * @brief HTTP客户端操作工具类
 * @author Fanhao.Yi
 * @date 2015年4月28日下午4:59:33
 * @version V1.0
 */
public abstract class HttpTool {

	/** 异步的HTTP客户端实例 **/
	protected static AsyncHttpClient client = new AsyncHttpClient();

	/** 默认字符集 **/
	public static String DEFAULT_CHARSET = "UTF-8";
	public static String UDID_VALUES;
	public static String UADETAIL_VALUES;
	private static String APPVERSION = "appversion";
	private static String UA = "ua";
	private static String UADETAIL = "uadetail";
	private static String UDID = "udid";
	private static String S = "s";
	private static String T = "t";
	private static int UA_VALUES = 4;
	public static String APPVERSION_VALUES = "1.0.0";
	private static String CHANNELID = "channelid";
	public static String channelID = "";

	public static final int DEFAULT_SOCKET_TIMEOUT = 30000;

	/** 广告 **/
	/**
	 * 客户端编号
	 */
	private static String APPID = "appid";
	/**
	 * 资源号
	 */
	private static String RESID = "resid";
	/**
	 * API 协议版本号(2.0)
	 */
	private static String V = "v";
	/**
	 * API 接口编号
	 */
	private static String METHOD = "method";

	/**
	 * 模拟GET表单（无参数）
	 * 
	 * @param url
	 *            请求URL
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	public static void get(Context context, String url,
			ResponseHandlerInterface responseHandler) {
		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			client.get(url, responseHandler);
		}
	}

	/**
	 * 模拟GET表单（有参数）
	 * 
	 * @param url
	 *            请求URL
	 * @param parmsMap
	 *            参数
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	// public static void get(Context context,String url, Map<String, ?>
	// parmsMap,
	// ResponseHandlerInterface responseHandler, String... charset) {
	// if (checkNetwork()) {
	// // 关闭过期连接.
	// client.getHttpClient().getConnectionManager()
	// .closeExpiredConnections();
	// if (null != charset && charset.length > 0) {
	// DEFAULT_CHARSET = charset[0];
	// }
	// client.get(url, fillParms(parmsMap, DEFAULT_CHARSET),
	// responseHandler);
	// }
	// }

	/**
	 * 模拟GET表单（有参数）
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            请求URL
	 * @param parmsMap
	 *            参数
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	public static void get(Context context, String url,
			Map<String, ?> parmsMap, ResponseHandlerInterface responseHandler,
			String... charset) {
		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			if (null != charset && charset.length > 0) {
				DEFAULT_CHARSET = charset[0];
			}
			Map request = getRequestMap();
			request.putAll(parmsMap);
			client.get(context, url, fillParms(request, DEFAULT_CHARSET),
					responseHandler);
		}
	}

	/**
	 * 模拟POST表单（无参数）
	 * 
	 * @param url
	 *            请求URL
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	public static void post(Context context, String url,
			ResponseHandlerInterface responseHandler) {
		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			client.post(url, responseHandler);
		}
	}

	public static void post(Context context, String url,
			ResponseHandlerInterface responseHandler, String... charset) {
		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			if (null != charset && charset.length > 0) {
				DEFAULT_CHARSET = charset[0];
			}
			Map request = getRequestMap();
			client.post(Apis.HOST + url, fillParms(request, DEFAULT_CHARSET),
					responseHandler);
		}
	}

	/**
	 * 模拟POST表单（有参数）
	 * 
	 * @param url
	 *            请求URL
	 * @param parmsMap
	 *            参数
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	public static void post(Context context, String url, View loading,
			Map<String, ?> parmsMap, ResponseHandlerInterface responseHandler,
			String... charset) {
		client.setTimeout(DEFAULT_SOCKET_TIMEOUT);
		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			if (null != charset && charset.length > 0) {
				DEFAULT_CHARSET = charset[0];
			}
			Map request = getRequestMap();
			if (parmsMap != null) {
				request.putAll(parmsMap);
			}
			client.post(Apis.HOST + url, fillParms(request, DEFAULT_CHARSET),
					responseHandler);
		} else {
			loading.setVisibility(View.GONE);
		}
	}

	public static void post(Context context, String url,
			Map<String, ?> parmsMap, ResponseHandlerInterface responseHandler,
			String... charset) {
		client.setTimeout(DEFAULT_SOCKET_TIMEOUT);
		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			if (null != charset && charset.length > 0) {
				DEFAULT_CHARSET = charset[0];
			}
			Map request = getRequestMap();
			if (parmsMap != null) {
				request.putAll(parmsMap);
			}
			client.post(Apis.HOST + url, fillParms(request, DEFAULT_CHARSET),
					responseHandler);
		}
	}

	/**
	 * 模拟POST表单（有参数）
	 * 
	 * @param url
	 *            请求URL
	 * @param parmsMap
	 *            参数
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	public static void imagePost(Context context, String url,
			Map<String, ?> parmsMap, ResponseHandlerInterface responseHandler,
			String... charset) {
		client.setTimeout(DEFAULT_SOCKET_TIMEOUT);
		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			if (null != charset && charset.length > 0) {
				DEFAULT_CHARSET = charset[0];
			}
			Map request = getRequestMap();
			if (parmsMap != null) {
				request.putAll(parmsMap);
			}
			client.post(Apis.ONLYHOST + url,
					fillParms(request, DEFAULT_CHARSET), responseHandler);
		}
	}

	/**
	 * 模拟POST表单（有参数）
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            请求URL
	 * @param parmsMap
	 *            参数
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	// public static void post(Context context, String url,
	// Map<String, ?> parmsMap, ResponseHandlerInterface responseHandler,
	// String... charset) {
	// if (checkNetwork(context)) {
	// // 关闭过期连接.
	// client.getHttpClient().getConnectionManager()
	// .closeExpiredConnections();
	// if (null != charset && charset.length > 0) {
	// DEFAULT_CHARSET = charset[0];
	// }
	// client.post(context, url, fillParms(parmsMap, DEFAULT_CHARSET),
	// responseHandler);
	// }
	// }
	/**
	 * 模拟POST表单（有参数）
	 * 
	 * @param url
	 *            请求URL
	 * @param parmsMap
	 *            参数
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	public static void post(Context context, String url,
			Map<String, ?> parmsMap, View loading,
			ResponseHandlerInterface responseHandler, String... charset) {
		client.setTimeout(DEFAULT_SOCKET_TIMEOUT);

		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			if (null != charset && charset.length > 0) {
				DEFAULT_CHARSET = charset[0];
			}
			Map request = getRequestMap();
			request.putAll(parmsMap);
			client.post(Apis.HOST + url, fillParms(request, DEFAULT_CHARSET),
					responseHandler);
		} else {
			loading.setVisibility(View.GONE);
		}
	}

	/**
	 * 模拟POST表单（有参数）
	 * 
	 * @param url
	 *            请求URL
	 * @param parmsMap
	 *            参数
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	public static void bannerPost(Context context, Map<String, ?> parmsMap,
			ResponseHandlerInterface responseHandler, String... charset) {
		client.setTimeout(DEFAULT_SOCKET_TIMEOUT);
		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			if (null != charset && charset.length > 0) {
				DEFAULT_CHARSET = charset[0];
			}
			Map request = getBannerRequestMap();
			request.putAll(parmsMap);
			client.post(Apis.BANNERHOST, fillParms(request, DEFAULT_CHARSET),
					responseHandler);
		}
	}

	/**
	 * 模拟提交POST表单（无参数）
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            请求URL
	 * @param entity
	 *            请求实体,可以null
	 * @param contentType
	 *            表单contentType （"multipart/form-data"）
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	public static void post(Context context, String url, HttpEntity entity,
			String contentType, ResponseHandlerInterface responseHandler) {
		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			client.post(context, url, entity, contentType, responseHandler);
		}
	}

	/**
	 * 模拟提交POST表单（有参数）
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            请求URL
	 * @param headers
	 *            请求Header
	 * @param parmsMap
	 *            参数
	 * @param contentType
	 *            表单contentType （"multipart/form-data"）
	 * @param responseHandler
	 *            回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/
	 *            TextHttpResponseHandler）
	 */
	public static void post(Context context, String url, Header[] headers,
			Map<String, ?> parmsMap, String contentType,
			ResponseHandlerInterface responseHandler, String... charset) {
		client.setTimeout(DEFAULT_SOCKET_TIMEOUT);
		if (checkNetwork(context)) {
			// 关闭过期连接.
			client.getHttpClient().getConnectionManager()
					.closeExpiredConnections();
			if (null != charset && charset.length > 0) {
				DEFAULT_CHARSET = charset[0];
			}
			client.post(context, url, headers,
					fillParms(parmsMap, DEFAULT_CHARSET), contentType,
					responseHandler);
		}
	}

	/**
	 * 封装公共数据
	 * 
	 * @return
	 */
	private static Map getRequestMap() {
		long time = System.currentTimeMillis();
		Map request = new HashMap();
		request.put(APPVERSION, APPVERSION_VALUES);
		request.put(UA, UA_VALUES);
		request.put(UADETAIL, UADETAIL_VALUES);
		request.put(UDID, UDID_VALUES);
		request.put(S, DigestTool.md5(UDID_VALUES + time + Apis.KEY));
		request.put(T, time);
		request.put(CHANNELID, channelID);
		return request;
	}

	/**
	 * 封装接口公共数据
	 * 
	 * @return
	 */
	private static Map getBannerRequestMap() {
		long time = System.currentTimeMillis();
		Map request = new HashMap();
		request.put(APPID, 1);
		request.put(APPVERSION, "1.0.2");
		request.put(UA, 4);
		request.put(UADETAIL, UADETAIL_VALUES);
		request.put(UDID, UDID_VALUES);
		request.put(S, DigestTool.md5(UDID_VALUES + time + Apis.BANNERKEY));
		request.put(T, time);
		request.put(V, "2.0.0");
		request.put(METHOD, "datas/ad_list_by_position");
		request.put(RESID, 3);
		return request;
	}

	/**
	 * 装填参数
	 * 
	 * @param map
	 *            参数
	 * @return
	 */
	public static RequestParams fillParms(Map<String, ?> map, String charset) {
		RequestParams params = new RequestParams();
		if (null != map && map.entrySet().size() > 0) {
			// 设置字符集,防止参数提交乱码
			if (!"".equals(charset)) {
				params.setContentEncoding(charset);
			}
			for (Iterator iterator = map.entrySet().iterator(); iterator
					.hasNext();) {
				Entry entity = (Entry) iterator.next();
				Object key = entity.getKey();
				Object value = entity.getValue();
				if (value instanceof File) {
					try {
						params.put((String) key, new FileInputStream(
								(File) value), ((File) value).getName());
					} catch (FileNotFoundException e) {
						throw new RuntimeException("文件不存在！", e);
					}
				} else if (value instanceof InputStream) {
					params.put((String) key, (InputStream) value);
				} else {

					params.put((String) key, value.toString());
				}
			}
		}
		return params;
	}

	/**
	 * 检测网络状态
	 * 
	 * @return 网络是否连接
	 */
	public static boolean checkNetwork(Context context) {
		boolean isConnected = MyApplication.isNetworkReady();
		if (isConnected) {
			if (DNSLookupThreadTool.CheckInternet(context)) {
				return true;
			} else {
				AlertTool.toastShort("网络不稳定，请检查网络！");
				return false;
			}
		} else {
			AlertTool.toastShort("网络连接失败，请检查网络！");
			return false;
		}
	}

	/**
	 * 停止请求
	 * 
	 * @param mContext
	 *            发起请求的上下文
	 */
	public static void stopRequest(Context mContext) {
		client.cancelRequests(mContext, true);
	}

	/**
	 * 停止全部请求
	 */
	public static void stopAllRequest() {
		client.cancelAllRequests(true);
	}

}
