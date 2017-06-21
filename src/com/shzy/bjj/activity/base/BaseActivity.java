package com.shzy.bjj.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.impl.IBaseActivity;
import com.shzy.bjj.tools.IntentTool;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * 
 * @brief 基本类 所有Activity需继承此类
 * @author Fanhao.Yi
 * @date 2015年4月27日上午11:09:44
 * @version V1.0
 */
public abstract class BaseActivity extends Activity implements IBaseActivity {
	/** 整个应用Applicaiton **/
	protected MyApplication mApplication = null;
	/** 当前Activity渲染的视图View **/
	protected View mContextView = null;
	/** 共通操作 **/
	protected IntentTool mIntentTool = null;
	protected ImageButton action_back;
	protected TextView action_title;
	protected TextView action_right;
	protected ImageButton action_fav;
	protected ImageButton action_share;
	protected Activity mContext;
	/** 日志输出标志 **/
	protected final String TAG = this.getClass().getSimpleName();
	protected View loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		// SDKInitializer.initialize(getApplicationContext());
		mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
		setContentView(mContextView);
		// 获取应用Application
		mApplication = (MyApplication) getApplicationContext();
		// 实例化共通操作
		mIntentTool = new IntentTool(this);
		initHeadView(mContextView);
		// 初始化控件
		initView(mContextView);
		// 业务操作
		initData(this);
		initListener();
	}

	/**
	 * 自定义findViewById
	 * 
	 * @param id
	 *            控件资源ID
	 * @return
	 */
	public <T extends View> T $(int id) {
		return (T) super.findViewById(id);
	}

	/**
	 * 获取当前Activity
	 * 
	 * @return 当前Activityd对象
	 */

	protected Activity getContext() {
		return AppManager.getAppManager().currentActivity();
	}

	/**
	 * 获取共通操作机能
	 * 
	 * @return 当前共通操作机能对象
	 */
	public IntentTool getIntentTool() {
		return this.mIntentTool;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	public void initHeadView(View view) {
		loading = $(R.id.loading);
		action_back = (ImageButton) view.findViewById(R.id.action_back);
		action_title = (TextView) view.findViewById(R.id.action_title);
		action_right = (TextView) view.findViewById(R.id.action_right);
		action_fav = (ImageButton) view.findViewById(R.id.action_fav);
		action_share = (ImageButton) view.findViewById(R.id.action_share);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppManager.getAppManager().finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
