package com.shzy.bjj.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;

import com.shzy.bjj.activity.base.BaseFragmentActivity;
import com.shzy.bjj.activity.mine.MineInfoActivity;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.fragment.TabFragment;
import com.shzy.bjj.tools.AppTool;
import com.shzy.bjj.tools.DataUtil;
import com.shzy.bjj.tools.StringTool;

/**
 * 
 * @brief Tab
 * @author Fanhao.Yi
 * @date 2015年4月29日上午11:25:29
 * @version V1.0
 */
public class TabFragmentActivity extends BaseFragmentActivity {
	private int index;

	public static void startActivity(Context context, int index) {
		context.startActivity(new Intent(context, TabFragmentActivity.class)
				.putExtra("index", index));
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		startJPushActivity(this, getIntent());
	}

	@Override
	public Fragment createFragment() {
		index = getIntent().getIntExtra("index", 0);
		return new TabFragment(index);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppTool.exitBy2Click(TabFragmentActivity.this, this);
		}
		return false;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		startJPushActivity(this, intent);
	}

	private void startJPushActivity(Context context, Intent intent) {
		if (intent != null
				&& StringTool.isNoBlankAndNoNull(intent
						.getStringExtra(DataTag.LOGINKEY))) {
			MineInfoActivity.startActivity(TabFragmentActivity.this,
					intent.getStringExtra(DataTag.LOGINKEY));

		}
	}
}
