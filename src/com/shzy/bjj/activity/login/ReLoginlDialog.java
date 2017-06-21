package com.shzy.bjj.activity.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.tools.PreferencesTool;

/**
 * 重新登录
 * 
 * @author Administrator
 * 
 */
public class ReLoginlDialog extends BaseActivity implements OnClickListener {
	private TextView titleTextView;
	private EditText numnerEditText;
	private TextView toastTextView;
	private TextView cancelBt;
	private TextView okBt;
	private String cdkNumber;
	private LinearLayout layout;
	private View line_View;
	private View line_hView;

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, ReLoginlDialog.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_voucher_dialog;
	}

	@Override
	public void initView(View view) {
		titleTextView = $(R.id.title);
		numnerEditText = $(R.id.number);
		toastTextView = $(R.id.toast);
		cancelBt = $(R.id.cancel);
		okBt = $(R.id.ok);
		layout = $(R.id.layout);
		line_hView = $(R.id.line_h);
		line_View = $(R.id.line_v);
		loading.setVisibility(View.GONE);
		numnerEditText.setVisibility(View.GONE);
		toastTextView.setVisibility(View.VISIBLE);
		cancelBt.setVisibility(View.GONE);
		line_View.setVisibility(View.GONE);
		titleTextView.setVisibility(View.GONE);
		toastTextView.setText(R.string.toast_relogin);
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
			LoginActivity.startActivity(getContext());
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}

}
