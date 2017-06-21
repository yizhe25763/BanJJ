package com.shzy.bjj.activity.mine;

import java.util.HashMap;
import java.util.Map;

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
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

public class BabyDeleteActivity extends BaseActivity implements OnClickListener {
	private TextView cancelBt;
	private TextView okBt;
	private TextView messageTextView;
	private TextView titleTextView;
	private LinearLayout layout;
	private BabyBean babyBean;

	public static void startActivity(Context context, BabyBean baby) {
		context.startActivity(new Intent(context, BabyDeleteActivity.class)
				.putExtra("baby", baby));
	}

	@Override
	public int bindLayout() {
		return R.layout.phone_call_dialog;
	}

	@Override
	public void initView(View view) {
		babyBean = (BabyBean) getIntent().getSerializableExtra("baby");
		cancelBt = $(R.id.cancel);
		okBt = $(R.id.ok);
		okBt.setText(R.string.delete);
		layout=$(R.id.layout);
		messageTextView = $(R.id.message);
		titleTextView = $(R.id.title);
		titleTextView.setVisibility(View.GONE);
		messageTextView.setText(R.string.toast_delete_baby);
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
			if (mApplication.isLogin()) {
				layout.setVisibility(View.INVISIBLE);
				loading.setVisibility(View.VISIBLE);
				deleteBaby(mApplication.getLOGIN_KEY(), babyBean.getId());
			} else {
				AppManager.getAppManager().finishActivity();
			}
			break;

		default:
			break;
		}
	}

	private void deleteBaby(String loginKey, int baby_id) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put("baby_id", baby_id);

		HttpTool.post(getContext(),Apis.USER_BABY_DELETE,loading, maps, new StringHandler(loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					ToastTool.toastMessage(BabyDeleteActivity.this,
							R.string.toast_baby_delete);
					AppManager.getAppManager().finishActivity();
					AppManager.getAppManager().finishActivity(
							MineBabyActivity.class);
					MineMamaActivity.isRefresh = true;
				}
				loading.setVisibility(View.GONE);
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});
	}
}
