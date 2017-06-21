package com.shzy.bjj.activity.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.PhoneTool;

public class CallPhoneActivity extends BaseActivity implements OnClickListener {
	private TextView cancelBt;
	private TextView okBt;

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, CallPhoneActivity.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.phone_call_dialog;
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
			if (ButtonTool.isFastClick()) {
				return;
			}
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.ok:
			if (ButtonTool.isFastClick()) {
				return;
			}
			PhoneTool.callPhone(getContext(), "4009-626-199");
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}

}
