package com.shzy.bjj.activity.mine;

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
import com.shzy.bjj.activity.TabFragmentActivity;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.CallPhoneActivity;
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
 * @brief 退出弹出框
 * @author Fanhao Yi
 * @data 2015年8月14日下午9:01:30
 * @version V1.0
 */
public class LoginOutActivity extends BaseActivity implements OnClickListener {
	private TextView cancelBt;
	private TextView okBt;
	TaskDayOrderBean bean;

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, LoginOutActivity.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.login_out_dialog;
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
			logout(mApplication.getLOGIN_KEY());
			break;

		default:
			break;
		}
	}

	public void logout() {
		mApplication.setLogin(false);
		mApplication.setLOGIN_KEY("");
		PreferencesTool.putString(this, DataTag.LOGINKEY, "");
		TabFragmentActivity.startActivity(getContext(), 3);
		AppManager.getAppManager().finishActivity();
	}

	/**
	 * 退出登录
	 * 
	 * @param loginKey
	 */
	private void logout(String loginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);

		HttpTool.post(getContext(),Apis.USER_LOGOUT,loading, maps, new StringHandler(null) {

			@Override
			public void success(String response) {
				logout();
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});
	}

}
