package com.shzy.bjj.activity.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.impl.IBaseFragmentActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @brief 基本类FragmentActivity需继承此类
 * @author Fanhao.Yi
 * @date 2015年4月29日上午10:40:42
 * @version V1.0
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements
		IBaseFragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		AppManager.getAppManager().addActivity(this);

		setContentView(R.layout.activity_main);
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager
				.findFragmentById(R.id.base_fragment);
		if (fragment == null) {
			fragment = createFragment();
			fragmentManager.beginTransaction().addToBackStack(null)
					.add(R.id.base_fragment, fragment).commit();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}

}
