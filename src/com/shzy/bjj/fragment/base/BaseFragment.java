package com.shzy.bjj.fragment.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.fragment.base.impl.IBaseFragment;
import com.shzy.bjj.tools.IntentTool;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

/**
 * 
 * @brief Fragment基类
 * @author Fanhao.Yi
 * @date 2015年4月29日上午10:54:29
 * @version V1.0
 */
@SuppressLint("NewApi")
public abstract class BaseFragment extends Fragment implements IBaseFragment {

	/** 当前Fragment渲染的视图View **/
	private View mContextView = null;
	/** 共通操作 **/
	private IntentTool mIntentTool = null;
	protected ImageButton action_back;
	protected TextView action_title;
	protected TextView action_right;
	protected ImageButton action_fav;
	protected ImageButton action_share;
	/** 整个应用Applicaiton **/
	protected MyApplication mApplication = null;
	protected View loading;

	/** 日志输出标志 **/
	protected final String TAG = this.getClass().getSimpleName();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 获取应用Application
		mApplication = (MyApplication) getContext().getApplicationContext();
		// 渲染视图View(防止切换时重绘View)
		if (null != mContextView) {
			ViewGroup parent = (ViewGroup) mContextView.getParent();
			if (null != parent) {
				parent.removeView(mContextView);
			}
		} else {
			mContextView = inflater.inflate(bindLayout(), container, false);
			// 控件初始化
			initHeadView(mContextView);
			initView(mContextView);
			// 实例化共通操作
			mIntentTool = new IntentTool(getActivity());
		}

		// 业务处理
		initData(getActivity());
		initListener();
		return mContextView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		MobclickAgent.onResume(getActivity());
		super.onResume();
	}

	@Override
	public void onPause() {
		MobclickAgent.onPause(getActivity());
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	/**
	 * 自定义findViewById
	 * 
	 * @param id
	 *            控件资源ID
	 * @return
	 */
	public <T extends View> T $(int id) {
		return (T) mContextView.findViewById(id);
	}

	/**
	 * 获取当前Fragment依附在的Activity
	 * 
	 * @return
	 */
	protected Activity getContext() {
		return getActivity();
	}

	/**
	 * 获取共通操作机能
	 */
	public IntentTool getIntentTool() {
		return this.mIntentTool;
	}

	public void initHeadView(View view) {
		loading = $(R.id.loading);
		action_back = (ImageButton) view.findViewById(R.id.action_back);
		action_title = (TextView) view.findViewById(R.id.action_title);
		action_right = (TextView) view.findViewById(R.id.action_right);
		action_fav = (ImageButton) view.findViewById(R.id.action_fav);
		action_share = (ImageButton) view.findViewById(R.id.action_share);
	}

}
