package com.shzy.bjj.activity.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.tools.ButtonTool;

/**
 * 
 * 
 * @brief 公共弹出框
 * @author Fanhao Yi
 * @data 2015年7月7日下午6:15:40
 * @version V1.0
 */
public class CommonDialogActivity extends BaseActivity {
	private Button okBtn = null;
	TextView title, descOne;
	Context instance;
	String titles, descOnes;

	@Override
	public void initListener() {

		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				AppManager.getAppManager().finishActivity();
			}
		});
	}

	@Override
	public int bindLayout() {
		return R.layout.activity_common_dialog;
	}

	@Override
	public void initView(View view) {
		okBtn = (Button) findViewById(R.id.ok_btn);
		title = $(R.id.title);
		descOne = $(R.id.desc_one);

	}

	@Override
	public void initData(Context mContext) {
		instance = this;
		Intent intent = getIntent();
		titles = intent.getStringExtra("title");
		descOnes = intent.getStringExtra("content");
		title.setText(titles);
		descOne.setText(descOnes);
	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

}
