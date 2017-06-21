package com.shzy.bjj.activity.order;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.CallPhoneActivity;
import com.shzy.bjj.bean.TaskDayOrderBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.PhoneTool;
import com.shzy.bjj.tools.StringTool;

public class ComplainOrderActivity extends BaseActivity implements
		OnClickListener {
	private TextView cancelBt;
	private TextView okBt;
	TaskDayOrderBean bean;

	public static void startActivity(Context context, TaskDayOrderBean bean) {
		context.startActivity(new Intent(context, ComplainOrderActivity.class)
				.putExtra("bean", bean));
	}

	@Override
	public int bindLayout() {
		return R.layout.complainorder_phone_call_dialog;
	}

	@Override
	public void initView(View view) {
		cancelBt = $(R.id.cancel);
		okBt = $(R.id.ok);
	}

	@Override
	public void initData(Context mContext) {
		bean = (TaskDayOrderBean) getIntent().getSerializableExtra("bean");
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
			comPlainOrder(mApplication.getLOGIN_KEY(), bean.getOrderBean()
					.getOrder_number(), bean.getOrderDetailCountBean()
					.getOrder_detail_number());
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}

	/**
	 * 用户投诉订单
	 * 
	 * @param login_KEY
	 *            身份识别key
	 * @param order_number
	 *            订单编号
	 * @param order_detail_number
	 *            订单明细编号
	 */
	private void comPlainOrder(String login_KEY, String order_number,
			String order_detail_number) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_KEY);
		maps.put(DataTag.ORDERNUMBER, order_number);
		maps.put(DataTag.ORDERDETAILNUMBER, order_detail_number);
		HttpTool.post(getContext(),Apis.COMPLAINORDER,loading, maps, new StringHandler(loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					loading.setVisibility(View.GONE);
					PhoneTool.callPhone(getContext(), "4009-626-199");
				}
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}

		});

	}

}
