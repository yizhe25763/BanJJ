package com.shzy.bjj.activity.mine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.adapter.IntegralAdapter;
import com.shzy.bjj.bean.IntegralBean;
import com.shzy.bjj.bean.IntegralResponse;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.CommentComparator;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;

/**
 * 我的积分
 * 
 * @author suntao
 * 
 */
public class MineIntegralActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private ListView listView;
	private LinearLayout layout;
	private IntegralAdapter adapter;
	private TextView integral_count;

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, MineIntegralActivity.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_integral;
	}

	@Override
	public void initView(View view) {
		layout = $(R.id.layout);
		layout.setVisibility(View.INVISIBLE);
		integral_count = $(R.id.integral_count);
		action_title.setText(R.string.mine_integral);
		action_right.setText(R.string.mine_integral_instructions);
		listView = $(R.id.listview);
	}

	@Override
	public void initData(Context mContext) {
		adapter = new IntegralAdapter(this);
		listView.setAdapter(adapter);
	}

	@Override
	public void initListener() {
		action_back.setOnClickListener(this);
		action_right.setOnClickListener(this);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void resume() {
		if (mApplication.isLogin()) {
			loading.setVisibility(View.VISIBLE);
			getIntegralData(mApplication.getLOGIN_KEY(), 1, 1000);
		} else {
			loading.setVisibility(View.GONE);
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.action_right:
			MineIntegralExplainActivity.startActivity(getContext());

			break;
		default:
			break;
		}
	}

	private void getIntegralData(String loginKey, int page, int size) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.PAGE, page);
		maps.put(DataTag.SIZE, size);
		HttpTool.post(getContext(), Apis.GET_USER_INTEGRAL,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							IntegralResponse integralResponse = JSON.decode(
									response, IntegralResponse.class);
							int count = integralResponse.getCount();
							int total = integralResponse.getTotal();
							integral_count.setText(String
									.valueOf(integralResponse.getTotal()) + "分");
							if (count > 0) {
								// List<IntegralBean> list = new
								// ArrayList<IntegralBean>();
								// list.addAll(getSimepleList(integralResponse
								// .getList()));
								adapter.setData(integralResponse.getList());
								adapter.notifyDataSetChanged();
							} else {
								$(R.id.no_message).setVisibility(View.VISIBLE);
							}
						}
						loading.setVisibility(View.GONE);
						layout.setVisibility(View.VISIBLE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	public static List<IntegralBean> getSimepleList(List<IntegralBean> list) {
		Collections.sort(list, new CommentComparator());
		return list;
	}
}
