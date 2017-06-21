package com.shzy.bjj.activity.mine;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.WebViewActivity;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.bean.CdkResponse;
import com.shzy.bjj.bean.RequestFailBean;
import com.shzy.bjj.bean.VoucherBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.zxing.CaptureActivity;

/**
 * 兑换代金券
 * 
 * @author Administrator
 * 
 */
public class MineVoucherDialog extends BaseActivity implements OnClickListener {
	private TextView titleTextView;
	private EditText numnerEditText;
	private TextView toastTextView;
	private TextView cancelBt;
	private TextView okBt;
	private String cdkNumber;
	private LinearLayout layout;
	private View line_View;
	private View line_hView;

	private VoucherBean voucherBean;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			CdkResponse response = (CdkResponse) msg.obj;
			if (response != null) {
				StringBuffer buffer = new StringBuffer("恭喜您，您获得了");
				buffer.append(response.getGoods_amount() + "张");
				buffer.append("“" + response.getGoods_name() + "”");
				buffer.append("请在“我的代金券”中查看");
				titleTextView.setText(R.string.voucher_exchange_success);
				numnerEditText.setVisibility(View.GONE);

				toastTextView.setText(buffer.toString().trim());
				toastTextView.setVisibility(View.VISIBLE);
				cancelBt.setVisibility(View.GONE);
				line_View.setVisibility(View.GONE);
				okBt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						if (mApplication.isLogin()) {
							loading.setVisibility(View.VISIBLE);
							exchang(mApplication.getLOGIN_KEY(), cdkNumber);
						} else {
							loading.setVisibility(View.GONE);
						}
					}
				});
			}
		}

	};

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, MineVoucherDialog.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_voucher_dialog;
	}

	@Override
	public void initView(View view) {
		// voucherBean = (VoucherBean) getIntent().getSerializableExtra("bean");
		titleTextView = $(R.id.title);
		numnerEditText = $(R.id.number);
		toastTextView = $(R.id.toast);
		cancelBt = $(R.id.cancel);
		okBt = $(R.id.ok);
		layout = $(R.id.layout);
		line_hView = $(R.id.line_h);
		line_View = $(R.id.line_v);
		loading.setVisibility(View.GONE);
	}

	@Override
	public void initData(Context mContext) {
		// if (voucherBean != null) {
		// numnerEditText.setText(voucherBean.getId());
		// } else {
		// AppManager.getAppManager().finishActivity();
		// }

	}

	@Override
	public void initListener() {
		cancelBt.setOnClickListener(this);
		okBt.setOnClickListener(this);
	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.cancel:
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.ok:
			String number = numnerEditText.getText().toString().trim();

			
			
			
			if (StringTool.isNoBlankAndNoNull(number) && 2 < number.length()
					&& number.length() < 12) {
				exchanngeCash(number);
			} else {
				ToastTool.toastMessage(MineVoucherDialog.this, "兑换码错误");

			}

			// if (!StringTool.isNoBlankAndNoNull(number) || number.length() !=
			// 12) {
			// ToastTool.toastMessage(this, R.string.voucher_exchange_fail);
			// return;
			// }
			// cdkNumber = number;
			// if (mApplication.isLogin()) {
			// loading.setVisibility(View.VISIBLE);
			// exchangConfirm(mApplication.getLOGIN_KEY(), cdkNumber);
			// } else {
			// loading.setVisibility(View.GONE);
			// }
			// Message message = new Message();
			// message.what = 1;
			// message.obj = new CdkResponse();
			// handler.sendMessage(message);
			break;

		default:
			break;
		}
	}

	private void exchanngeCash(String content) {

		if (content.substring(0, 4).equals("http")
				&& content.contains("banjiajia.cn")) {// 跳链接
			WebViewActivity.startActivity(MineVoucherDialog.this, content, 0);
		} else if (content.substring(0, 3).equals("bjj")) {// 兑换
			content = content.substring(3, content.length());
			cashCoupon(content);
		} else {
			ToastTool.toastMessage(MineVoucherDialog.this, "兑换码错误");
		}
//		if (content.substring(0, 3).equals("bjj")) {// 兑换
//			content = content.substring(3, content.length());
//		}else{
//			
//		}
//		cashCoupon(content);
	}

	private void cashCoupon(String content) {
		MyApplication mApplication = (MyApplication) getApplicationContext();
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, mApplication.getLOGIN_KEY());
		maps.put(DataTag.EXCHANGEID, content);
		maps.put(DataTag.TYPE, 1);
		HttpTool.post(MineVoucherDialog.this, Apis.CASHCOUPON, loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							ToastTool.toastMessage(MineVoucherDialog.this,
									"兑换成功");
							MineVoucherActivity
									.startActivity(MineVoucherDialog.this);
							AppManager.getAppManager().finishActivity();
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						if (StringTool.isNoBlankAndNoNull(responseBody)) {
							RequestFailBean result = JSON.decode(responseBody,
									RequestFailBean.class);
							String str = null;
							switch (result.getError_code()) {
							case 0:
								str = "优惠活动不存在";
								break;
							case -1:
								str = "用户不存在";
								break;
							case -2:
								str = "用户资格已满";
								break;
							case -3:
								str = "优惠券已经发完";
								break;
							default:
								str = "兑换码错误！";
								break;
							}
							ToastTool.toastMessage(MineVoucherDialog.this, str);
							finish();
						}
					}
				});

	}

	private void exchangConfirm(String loginKey, String cdk) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put("cdk", cdk);
		HttpTool.post(getContext(), Apis.CDK_VOUCHE_EXCHANGE_CONFIRM, loading,
				maps, new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							CdkResponse cdkResponse = JSON.decode(response,
									CdkResponse.class);
							if (cdkResponse != null) {
								Message message = new Message();
								message.what = 1;
								message.obj = cdkResponse;
								handler.sendMessage(message);
							}
						}
						loading.setVisibility(View.GONE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});
	}

	private void exchang(String loginKey, String cdk) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put("cdk", cdk);
		HttpTool.post(getContext(), Apis.CDK_VOUCHE_EXCHANGE_SUBMIT, loading,
				maps, new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {

						}
						loading.setVisibility(View.GONE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});
	}

}
