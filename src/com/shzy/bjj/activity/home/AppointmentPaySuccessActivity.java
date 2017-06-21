package com.shzy.bjj.activity.home;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.TabFragmentActivity;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.teacher.ChooseTeacherActivity;
import com.shzy.bjj.tools.ButtonTool;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 
 * @brief 订单支付成功页面
 * @author Fanhao Yi
 * @data 2015年7月8日上午10:30:16
 * @version V1.0
 */
public class AppointmentPaySuccessActivity extends BaseActivity {
	/**
	 * 查看订单
	 */
	private Button mOrderOkBtn;

	@Override
	public int bindLayout() {
		return R.layout.activity_appoinment_pay_success;
	}

	@Override
	public void initView(View view) {
		mOrderOkBtn = $(R.id.look_btn);

	}

	@Override
	public void initData(Context mContext) {
		action_back.setVisibility(View.GONE);

	}

	@Override
	public void initListener() {
		mOrderOkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				TabFragmentActivity.startActivity(getContext(), 2);
				AppManager.getAppManager().finishActivity();
			}
		});
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				AppManager.getAppManager().finishActivity();
			}
		});
	}

	@Override
	public void resume() {
		MobclickAgent.onEvent(getContext(), "B0007");

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			TabFragmentActivity.startActivity(getContext(), 2);
			AppManager.getAppManager().finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		TabFragmentActivity.startActivity(getContext(), 2);
		AppManager.getAppManager().finishActivity();
	}

	@Override
	public void destroy() {

	}
}
