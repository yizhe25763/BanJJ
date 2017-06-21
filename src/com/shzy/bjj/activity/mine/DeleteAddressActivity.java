package com.shzy.bjj.activity.mine;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.TabFragmentActivity;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.CallPhoneActivity;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.bean.TaskDayOrderBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.PhoneTool;
import com.shzy.bjj.tools.PreferencesTool;
import com.shzy.bjj.tools.StringTool;

/**
 * 
 * 
 * @brief 删除地址弹出框
 * @author Fanhao Yi
 * @data 2015年8月14日下午9:01:30
 * @version V1.0
 */
public class DeleteAddressActivity extends BaseActivity implements
		OnClickListener {
	private TextView cancelBt;
	private TextView okBt;
	AppointmentBean appointmentBean;

	public static void startActivity(Context context,
			AppointmentBean appointmentBean) {
		context.startActivity(new Intent(context, DeleteAddressActivity.class)
				.putExtra("appointmentBean", appointmentBean));
	}

	@Override
	public int bindLayout() {
		return R.layout.delete_address_dialog;
	}

	@Override
	public void initView(View view) {
		cancelBt = $(R.id.cancel);
		okBt = $(R.id.ok);
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
			setResult(1, getIntent());
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}


}
