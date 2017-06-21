package com.shzy.bjj.activity.order;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.bean.CannelOrderBean;
import com.shzy.bjj.bean.OrderBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

public class CancelOrderDialog extends BaseActivity implements OnClickListener {
	private TextView cancelBt;
	private TextView okBt;
	private TextView messageTextView;
	private TextView titleTextView;
	private LinearLayout layout;
	private OrderBean orderBean;

	public static void startActivity(Context context, OrderBean bean) {
		context.startActivity(new Intent(context, CancelOrderDialog.class)
				.putExtra("bean", bean));
	}

	@Override
	public int bindLayout() {
		return R.layout.phone_call_dialog;
	}

	@Override
	public void initView(View view) {
		orderBean = (OrderBean) getIntent().getSerializableExtra("bean");
		if (orderBean == null) {
			AppManager.getAppManager().finishActivity();
		}
		cancelBt = $(R.id.cancel);
		okBt = $(R.id.ok);
		okBt.setText(R.string.ok);
		layout = $(R.id.layout);
		messageTextView = $(R.id.message);
		titleTextView = $(R.id.title);
		titleTextView.setVisibility(View.GONE);
		messageTextView.setText(R.string.toast_delete_order);
	}

	@Override
	public void initData(Context mContext) {
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
			if (mApplication.isLogin() && orderBean != null) {
				layout.setVisibility(View.INVISIBLE);
				loading.setVisibility(View.VISIBLE);
				cancelOrder(mApplication.getLOGIN_KEY(),
						orderBean.getOrder_number());
			} else {
				AppManager.getAppManager().finishActivity();
			}
			break;

		default:
			break;
		}
	}

	private void cancelOrder(String loginKey, String number) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.ORDERNUMBER, number);
		HttpTool.post(getContext(),Apis.GET_USER_ORDER_CANCEL,loading, maps, new StringHandler(
				loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					loading.setVisibility(View.GONE);
					ToastTool.toastMessage(getContext(), "订单取消成功");
					AppManager.getAppManager().finishActivity();
				}
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				AppManager.getAppManager().finishActivity();
				CannelOrderBean bean = JSON.decode(responseBody,
						CannelOrderBean.class);
				if (bean != null) {
					if (bean.getResult() == 1) {
						ToastTool.toastMessage(getContext(),
								showFailMessage(bean.getFailue_reason_type()));
					} else {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				}
			}

			private String showFailMessage(int failue_reason_type) {
				switch (failue_reason_type) {
				case 0:
					return "用户和订单不匹配";
				case -1:

					return "订单不存在";
				case -2:

					return "订单已经不可取消";
				case -3:

					return "订单已经自动关闭";
				case -4:

					return "用户不存在";
				}
				return "";
			}
		});
	}

}
