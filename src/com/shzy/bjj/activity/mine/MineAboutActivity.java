package com.shzy.bjj.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.LocationActivity;

public class MineAboutActivity extends BaseActivity implements OnClickListener {

	private TextView debugTxt;
	private ImageButton action_back;

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, MineAboutActivity.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_about;
	}

	@Override
	public void initView(View view) {
		debugTxt = $(R.id.action_right);
		action_back = $(R.id.action_back);

	}

	@Override
	public void initData(Context mContext) {

	}

	@Override
	public void initListener() {
		action_back.setOnClickListener(this);
		debugTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LocationActivity.isDebug = true;

			}
		});
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
		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}

}
