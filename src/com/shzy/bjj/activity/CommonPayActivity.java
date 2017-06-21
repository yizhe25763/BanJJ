package com.shzy.bjj.activity;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.arnx.jsonic.JSON;
import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.MD5;
import net.sourceforge.simcpux.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.AppointmentPaySuccessActivity;
import com.shzy.bjj.alipay.PayResult;
import com.shzy.bjj.alipay.SignUtils;
import com.shzy.bjj.bean.OrderBean;
import com.shzy.bjj.bean.ResumePayInfoBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class CommonPayActivity extends BaseActivity {

	private ImageButton mWechatBtn;
	private ImageButton mALiPayBtn;
	private int mTypeID;
	private Button okBtn;
	private OrderBean bean;
	private int payTypeID;
	private String mLoginKey;
	private String payNumber;
	private String notifyUrl;
	private StringBuffer sb;
	private Map<String, String> resultunifiedorder;
	private PayReq req;
	private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private ImageView backBtn;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DataConst.SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				if (TextUtils.equals(resultStatus, DataConst.ALIPAYSUCCESSCODE)) {
					Intent intent = new Intent();
					intent.setClass(getContext(),
							AppointmentPaySuccessActivity.class);
					okBtn.setClickable(true);
					okBtn.setEnabled(true);
					AppManager.getAppManager().finishActivity();
				} else {
					if (TextUtils
							.equals(resultStatus, DataConst.ALIPAYFAILCODE)) {
						Toast.makeText(getContext(), R.string.pay_loding,
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getContext(), R.string.pay_fail,
								Toast.LENGTH_SHORT).show();
					}
					okBtn.setClickable(true);
					okBtn.setEnabled(true);
				}
				break;
			}
			case DataConst.SDK_CHECK_FLAG: {
				okBtn.setClickable(true);
				okBtn.setEnabled(true);
				Toast.makeText(getContext(), "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	public int bindLayout() {
		return R.layout.activity_common_pay;
	}

	@Override
	public void initView(View view) {
		mWechatBtn = $(R.id.weixin_pay_bt);
		mALiPayBtn = $(R.id.ali_pay_bt);
		okBtn = $(R.id.ok_btn);
		backBtn = $(R.id.close_btn);
	}

	@Override
	public void initData(Context mContext) {
		bean = (OrderBean) getIntent().getSerializableExtra("bean");
		req = new PayReq();
		sb = new StringBuffer();
		msgApi.registerApp(Constants.APP_ID);
	}

	@SuppressLint("NewApi")
	@Override
	public void initListener() {
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AppManager.getAppManager().finishActivity();

			}
		});
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				if (bean != null) {
					okBtn.setClickable(false);
					okBtn.setEnabled(false);
					if (StringTool.isNoBlankAndNoNull(bean.getOrder_number())
							&& bean.getTotal_price() != 0 && payTypeID != 0) {
						loading.setVisibility(View.VISIBLE);
						Double price = (double) bean.getTotal_price();
						resumePayInfo(bean.getOrder_number(), payTypeID, price);
					} else {
						if (payTypeID == 0) {
							ToastTool.toastMessage(getContext(), "请选择付款方式");
						}
					}
				}

			}
		});

		mWechatBtn.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				if (!msgApi.isWXAppInstalled()) {
					ToastTool.toastMessage(getContext(), "未安装微信，请选择其他支付方式");
					return;
				}
				if (!msgApi.isWXAppSupportAPI()) {
					ToastTool.toastMessage(getContext(), "当前版本不支持支付功能");
					return;
				}
				payTypeID = 2;
				mWechatBtn.setBackgroundResource(
						R.drawable.choose_address_down);
				mALiPayBtn.setBackgroundResource(
						R.drawable.choose_address_up);
				mALiPayBtn.setEnabled(true);
				mWechatBtn.setEnabled(false);
			}

		});

		mALiPayBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mWechatBtn.setBackgroundResource(
						R.drawable.choose_address_up);
				mALiPayBtn.setBackgroundResource(
						R.drawable.choose_address_down);
				payTypeID = 1;
				mALiPayBtn.setEnabled(false);
				mWechatBtn.setEnabled(true);

			}
		});

	}

	private void resumePayInfo(String orderNumber, final int payType,
			final Double totalPrice) {

		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, mLoginKey);
		maps.put(DataTag.ORDERNUMBER, orderNumber);
		maps.put(DataTag.PAYTYPE, payType);

		HttpTool.post(getContext(),Apis.GET_RESUME_PAY_INFO,loading, maps, new StringHandler(null) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					ResumePayInfoBean bean = JSON.decode(response,
							ResumePayInfoBean.class);
					if (bean != null) {
						if (bean.getResult() == 0) {
							payNumber = bean.getPay_number();
							notifyUrl = bean.getNotifyUrl();
							if (StringTool.isNoBlankAndNoNull(payNumber)
									&& StringTool.isNoBlankAndNoNull(notifyUrl)) {
								if (payType == 1) {
									pay(notifyUrl, payNumber, totalPrice);
									loading.setVisibility(View.GONE);
								} else if (payType == 2) {
									wechatPay(notifyUrl, payNumber, totalPrice);
									loading.setVisibility(View.GONE);
								}
							} else {
							}

						}else{
							okBtn.setClickable(true);
							okBtn.setEnabled(true);

						}
					}
				}
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				okBtn.setClickable(true);
				okBtn.setEnabled(true);

				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});

	}

	// ******************************微信支付******************************//
	private void wechatPay(String notifyUrl, String payNumber, Double totalPrice) {
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask(notifyUrl, payNumber,
				totalPrice);
		getPrepayId.execute();

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(
				random.nextInt(DataConst.TIMESTAMP)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / DataConst.TIMESTAMP;
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

	private void sendPayReq() {
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
		okBtn.setClickable(true);
		okBtn.setEnabled(true);
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

	private String genProductArgs(Double totalPrice, String notifyUrl,
			String payNumber) {
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
			packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
			packageParams
					.add(new BasicNameValuePair("out_trade_no", payNumber));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", String
					.valueOf(totalPrice.intValue())));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring = toXml(packageParams);
			return new String(xmlstring.toString().getBytes(), "ISO8859-1");
		} catch (Exception e) {
			return null;
		}

	}

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

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;
		private Double totalPrice;
		private String notifyUrl;
		private String payNumber;

		public GetPrepayIdTask(String notifyUrl, String payNumber,
				Double totalPrice) {
			this.totalPrice = totalPrice;
			this.notifyUrl = notifyUrl;
			this.payNumber = payNumber;
		}

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
			String entity = genProductArgs(totalPrice, notifyUrl, payNumber);
			byte[] buf = Util.httpPost(url, entity);
			String content = new String(buf);
			Map<String, String> xml = decodeXml(content);
			return xml;
		}
	}

	// ******************************支付宝******************************//
	private void pay(String notifyUrl, String payNumber, Double totalPrice) {
		String orderInfo = getOrderInfo(
				getString(R.string.pay_content_message),
				getString(R.string.pay_content_message),
				String.valueOf(totalPrice / 100), notifyUrl, payNumber);
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
				PayTask alipay = new PayTask(CommonPayActivity.this);
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

	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	public String sign(String content) {
		return SignUtils.sign(content, DataConst.RSA_PRIVATE);
	}

	public String getOrderInfo(String subject, String body, String price,
			String notifyUrl, String payNumber) {
		String orderInfo = "partner=" + "\"" + DataConst.PARTNER + "\"";
		orderInfo += "&seller_id=" + "\"" + DataConst.SELLER + "\"";
		orderInfo += "&out_trade_no=" + "\"" + payNumber + "\"";
		orderInfo += "&subject=" + "\"" + subject + "\"";
		orderInfo += "&body=" + "\"" + body + "\"";
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		orderInfo += "&notify_url=" + "\"" + notifyUrl + "\"";
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		orderInfo += "&payment_type=\"1\"";
		orderInfo += "&_input_charset=\"utf-8\"";
		orderInfo += "&it_b_pay=\"30m\"";
		orderInfo += "&return_url=\"m.alipay.com\"";
		return orderInfo;
	}

	@Override
	public void resume() {
		if (mApplication.isLogin()) {
			mLoginKey = mApplication.getLOGIN_KEY();
		}

	}

	@Override
	public void destroy() {

	}

}
