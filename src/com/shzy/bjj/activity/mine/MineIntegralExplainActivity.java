package com.shzy.bjj.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.WebViewActivity;
import com.shzy.bjj.activity.base.BaseActivity;

public class MineIntegralExplainActivity extends BaseActivity {
	public static void startActivity(Context context) {
		context.startActivity(new Intent(context,
				MineIntegralExplainActivity.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.activity_mine_intregral_explain;
	}

	@Override
	public void initView(View view) {

	}

	@Override
	public void initData(Context mContext) {

	}

	@Override
	public void initListener() {
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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

}
