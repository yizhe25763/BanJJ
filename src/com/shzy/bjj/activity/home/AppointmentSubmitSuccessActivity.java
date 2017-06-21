package com.shzy.bjj.activity.home;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.MD5;
import net.sourceforge.simcpux.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.TabFragmentActivity;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.teacher.ChooseTeacherActivity;
import com.shzy.bjj.alipay.PayResult;
import com.shzy.bjj.alipay.SignUtils;
import com.shzy.bjj.bean.AppointmentPayRespone;
import com.shzy.bjj.bean.OrderSubmitRequest;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.ToastTool;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 
 * @brief 订单提交成功页面
 * @author Fanhao Yi
 * @data 2015年7月8日上午10:30:35
 * @version V1.0
 */
public class AppointmentSubmitSuccessActivity extends BaseActivity implements
		OnClickListener, IWXAPIEventHandler {
	/**
	 * 查看订单
	 */
	private Button mOrderOkBtn;
	/**
	 * 去支付
	 */
	private Button mPayBtn;
	private AppointmentPayRespone order;
	private OrderSubmitRequest request;

	// wechat
	private PayReq req;
	private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private TextView show;
	private Map<String, String> resultunifiedorder;
	private StringBuffer sb;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DataConst.SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				if (TextUtils.equals(resultStatus, DataConst.ALIPAYSUCCESSCODE)) {
					getIntentTool()
							.forward(AppointmentPaySuccessActivity.class);
					mPayBtn.setClickable(true);
					mPayBtn.setEnabled(true);
					AppManager.getAppManager().finishActivity();
				} else {

					if (TextUtils
							.equals(resultStatus, DataConst.ALIPAYFAILCODE)) {
						Toast.makeText(AppointmentSubmitSuccessActivity.this,
								R.string.pay_loding, Toast.LENGTH_SHORT).show();
					} else {
						MobclickAgent.onEvent(getContext(), "B0008");
						Toast.makeText(AppointmentSubmitSuccessActivity.this,
								R.string.pay_fail, Toast.LENGTH_SHORT).show();
					}
					mPayBtn.setClickable(true);
					mPayBtn.setEnabled(true);
				}
				break;
			}
			case DataConst.SDK_CHECK_FLAG: {
				mPayBtn.setClickable(true);
				mPayBtn.setEnabled(true);
				Toast.makeText(AppointmentSubmitSuccessActivity.this,
						"检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	public int bindLayout() {
		return R.layout.activity_appoinment_submit_success;
	}

	@Override
	public void initView(View view) {
		mOrderOkBtn = $(R.id.look_btn);
		mPayBtn = $(R.id.pay_btn);
	}

	@Override
	public void initData(Context mContext) {
		action_back.setVisibility(View.GONE);
		order = (AppointmentPayRespone) getIntent().getSerializableExtra(
				"order");
		request = (OrderSubmitRequest) getIntent().getSerializableExtra(
				"request");
		req = new PayReq();
		sb = new StringBuffer();
		msgApi.registerApp(Constants.APP_ID);
	}

	private void wechatPay() {
		if (!msgApi.isWXAppInstalled()) {
			ToastTool.toastMessage(getContext(), "没有安装微信");
			return;
		}
		if (!msgApi.isWXAppSupportAPI()) {
			ToastTool.toastMessage(getContext(), "当前版本不支持支付功能");
			return;
		}
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		getPrepayId.execute();
	}

	@Override
	public void initListener() {
		mPayBtn.setOnClickListener(this);
		mOrderOkBtn.setOnClickListener(this);
		action_back.setOnClickListener(this);
	}

	// 微信支付

	private void sendPayReq() {
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
		mPayBtn.setClickable(true);
		mPayBtn.setEnabled(true);
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / DataConst.TIMESTAMP;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return appSign;
	}

	private void genPayReq() {
		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get(Apis.PREPAY_ID);
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair(Apis.APPID, req.appId));
		signParams.add(new BasicNameValuePair(Apis.CONCESTR, req.nonceStr));
		signParams.add(new BasicNameValuePair(Apis.PACKAGE, req.packageValue));
		signParams.add(new BasicNameValuePair(Apis.PARTNERID, req.partnerId));
		signParams.add(new BasicNameValuePair(Apis.PREPAYID, req.prepayId));
		signParams.add(new BasicNameValuePair(Apis.TIMESTAMP, req.timeStamp));
		req.sign = genAppSign(signParams);
		sb.append("sign\n" + req.sign + "\n\n");
		sendPayReq();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			sb.append("prepay_id\n" + result.get(Apis.PREPAY_ID) + "\n\n");
			resultunifiedorder = result;
			genPayReq();

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String.format(DataConst.wechatPayUrl);
			String entity = genProductArgs();
			byte[] buf = Util.httpPost(url, entity);
			String content = new String(buf);
			Map<String, String> xml = decodeXml(content);
			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(
				random.nextInt(DataConst.TIMESTAMP)).getBytes());
	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();
			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", getResources()
					.getString(R.string.pay_content_message)));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", order
					.getNotify_url()));
			packageParams.add(new BasicNameValuePair("out_trade_no", order
					.getPay_number()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			Double price = (double) order.getTotal_price();
			packageParams.add(new BasicNameValuePair("total_fee", String
					.valueOf(price.intValue())));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring = toXml(packageParams);
			return new String(xmlstring.toString().getBytes(), "ISO8859-1");
		} catch (Exception e) {
			return null;
		}

	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		return sb.toString();
	}

	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return packageSign;
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		Double price = (double) order.getTotal_price();
		String orderInfo = getOrderInfo(
				getString(R.string.pay_content_message),
				getString(R.string.pay_content_message),
				String.valueOf(price / 100));
		String sign = sign(orderInfo);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(
						AppointmentSubmitSuccessActivity.this);
				String result = alipay.pay(payInfo);
				Message msg = new Message();
				msg.what = DataConst.SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask payTask = new PayTask(
						AppointmentSubmitSuccessActivity.this);
				boolean isExist = payTask.checkAccountIfExist();
				Message msg = new Message();
				msg.what = DataConst.SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		String orderInfo = "partner=" + "\"" + DataConst.PARTNER + "\"";
		orderInfo += "&seller_id=" + "\"" + DataConst.SELLER + "\"";
		orderInfo += "&out_trade_no=" + "\"" + order.getPay_number() + "\"";
		orderInfo += "&subject=" + "\"" + subject + "\"";
		orderInfo += "&body=" + "\"" + body + "\"";
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		orderInfo += "&notify_url=" + "\"" + order.getNotify_url() + "\"";
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		orderInfo += "&payment_type=\"1\"";
		orderInfo += "&_input_charset=\"utf-8\"";
		orderInfo += "&it_b_pay=\"30m\"";
		orderInfo += "&return_url=\"m.alipay.com\"";
		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, DataConst.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@Override
	public void resume() {
		MobclickAgent.onEvent(getContext(), "B0006");
	}

	@Override
	public void destroy() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.action_back:
			if (ButtonTool.isFastClick()) {
				return;
			}
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.look_btn:
			if (ButtonTool.isFastClick()) {
				return;
			}
			TabFragmentActivity.startActivity(getContext(), 2);
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.pay_btn:
			if (ButtonTool.isFastClick()) {
				return;
			}
			int mPayType = request.getPay_type();
			MobclickAgent.onEvent(getContext(), "B0005");
			if (mPayType == 1) {
				mPayBtn.setClickable(false);
				mPayBtn.setEnabled(false);
				pay(null);
			} else {
				mPayBtn.setClickable(false);
				mPayBtn.setEnabled(false);
				wechatPay();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onBackPressed() {
		TabFragmentActivity.startActivity(getContext(), 2);
		AppManager.getAppManager().finishActivity();
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
				Intent intent = new Intent();
				intent.setClass(getContext(),
						AppointmentPaySuccessActivity.class);
				startActivity(intent);
			}
		}
		finish();
	}
}
