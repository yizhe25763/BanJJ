package com.shzy.bjj.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.location.BDLocation;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.AppointmentTimeActivity;
import com.shzy.bjj.activity.home.LocationActivity;
import com.shzy.bjj.activity.login.LoginActivity;
import com.shzy.bjj.share.SharePopupWindow;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

/**
 * 蹭福利
 * 
 * @author Administrator
 * 
 */
public class WebViewActivity extends BaseActivity {
	private WebView webView;
	private String webViewUrl;
	private String title;

	public static void startActivity(Context context, String url, int type) {
		context.startActivity(new Intent(context, WebViewActivity.class)
				.putExtra("url", url).putExtra("type", type));
	}

	public static void startActivity(Context context, String url, String title) {
		context.startActivity(new Intent(context, WebViewActivity.class)
				.putExtra("url", url).putExtra("title", title));
	}

	public static void startActivity(Context context, String url, int type,
			String title) {
		context.startActivity(new Intent(context, WebViewActivity.class)
				.putExtra("url", url).putExtra("type", type)
				.putExtra("title", title));
	}

	@Override
	public int bindLayout() {
		return R.layout.layout_webview;
	}

	@Override
	public void initView(View view) {
		loading.setVisibility(View.VISIBLE);
		this.webView = (WebView) view.findViewById(R.id.webView);
	}

	@Override
	public void initData(Context mContext) {
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", 0);
		String url = intent.getStringExtra("url");
		title = intent.getStringExtra("title");
		if (StringTool.isNoBlankAndNoNull(title)) {
			if (title.equals("关于我们")) {
				action_right.setWidth(150);
			}
		}
		if (!StringTool.isNoBlankAndNoNull(title)) {
			action_title.setText("伴家家");
		} else {
			action_title.setText(title);
		}
		if (url.startsWith("file")) {
			webViewUrl = url;
		}
		if (StringTool.isNoBlankAndNoNull(url) && !url.startsWith("http")) {
			url = "http://" + url;
		}

		switch (type) {
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;

		default:
			break;
		}

		setWebSettings();
		LoginActivity.URLS = url;
		webViewUrl = url;
		if (StringTool.isNoBlankAndNoNull(webViewUrl)) {
			if (webViewUrl.contains("?")) {
				webViewUrl = webViewUrl + "&" + getExtrUrl();
			} else {
				webViewUrl = webViewUrl + "?" + getExtrUrl();
			}
		}
		webView.loadUrl(webViewUrl);
	}

	@Override
	public void initListener() {
		action_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LocationActivity.isDebug = true;
				ToastTool.toastMessage(getContext(), "切换至debug模式");

			}
		});
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AppManager.getAppManager().finishActivity();
			}
		});
	}

	@Override
	public void resume() {
	}

	@Override
	public void destroy() {
	}

	public String getExtrUrl() {
		StringBuilder builder = new StringBuilder();
		if (mApplication.isLogin()) {
			builder.append("login_key=" + mApplication.getLOGIN_KEY());
		} else {
			builder.append("login_key=");
		}
		BDLocation location = mApplication.getLocation();
		if (location != null) {
			builder.append("&lng=" + location.getLatitude());
			builder.append("&lat=" + location.getLongitude());
		} else {
			builder.append("&lng=");
			builder.append("&lat=");
		}
		builder.append("&plat=android");
		return builder.toString().trim();
	}

	private void setWebSettings() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存
		webSettings.setDefaultTextEncodingName("utf-8");
		webSettings.setJavaScriptEnabled(true);
		webSettings
				.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webView.addJavascriptInterface(this, "bjj");

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				super.onReceivedSslError(view, handler, error);
				loading.setVisibility(View.GONE);
				AppManager.getAppManager().finishActivity();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				loading.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

		});

	}

	/**
	 * js页面调用java本地方法
	 * 
	 * @param type
	 * @param url
	 * @param lowClient
	 * @param description
	 * @param message
	 * @param param1
	 * @param param2
	 * @param param3
	 * @param param4
	 */
	@JavascriptInterface
	public void huoDongClicked(int type, String url, String lowClient,
			String description, String message, String param1, String param2,
			String param3, String param4) {

		switch (type) {
		case 0:
			WebViewActivity.startActivity(getContext(), url, 0);
			break;
		case 1:
			// 去预约
			getIntentTool().forward(AppointmentTimeActivity.class);
			break;
		case 2:
			LoginActivity.startActivity(getContext(), "WebViewActivity", url);
			break;
		case 3:

			// 第三方分享
			SharePopupWindow sharePopupWindow = new SharePopupWindow(this,
					message, url, param1, param2);
			sharePopupWindow.showAtLocation(this.webView, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case 4:

			// 跳外链
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			break;
		case 5:
			WebViewActivity.startActivity(getContext(), url, 0);
			break;
		default:
			break;

		}
	}
}
