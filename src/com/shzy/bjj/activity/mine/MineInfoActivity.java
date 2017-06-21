package com.shzy.bjj.activity.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.adapter.InfoListAdapter;
import com.shzy.bjj.bean.InfoBean;
import com.shzy.bjj.bean.InfoResponse;
import com.shzy.bjj.bean.RequestFailBean;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

/**
 * 我的信息--消息中心
 * 
 * @author suntao
 * 
 */
public class MineInfoActivity extends BaseActivity implements OnClickListener {
	private ListView listView;
	private InfoListAdapter adapter;
	private List<InfoBean> list;

	public static void startActivity(Context context, String loginkey) {
		context.startActivity(new Intent(context, MineInfoActivity.class)
				.putExtra(DataTag.LOGINKEY, loginkey));
	}

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, MineInfoActivity.class)
				);
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_info;
	}

	@Override
	public void initView(View view) {
		action_title.setText(R.string.mine_infolist);
		listView = $(R.id.listview);
	}

	@Override
	public void initData(Context mContext) {
		String loginkey = getIntent().getStringExtra(DataTag.LOGINKEY);
		if (mApplication.isLogin()) {
			if (!StringTool.isNoBlankAndNoNull(loginkey)) {
				loginkey = mApplication.getLOGIN_KEY();
			}
			loading.setVisibility(View.VISIBLE);
			getInformationData(loginkey, 1, 100, "");
		} else {
			loading.setVisibility(View.GONE);
		}
		list = new ArrayList<InfoBean>();
		adapter = new InfoListAdapter(this);

	}

	@Override
	public void initListener() {
		action_back.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView ContentTextView = (TextView) view
						.findViewById(R.id.content);
				InfoBean bean = (InfoBean) ContentTextView.getTag();
				if (bean != null) {
					loading.setVisibility(View.VISIBLE);
					ReadMessage(bean.getId());
				}

			}

		});
	}

	private void ReadMessage(int id) {

		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, mApplication.getLOGIN_KEY());
		maps.put(DataTag.ID, id);
		HttpTool.imagePost(getContext(), Apis.MESSAGEREAD, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							if (JSON.decode(response, RequestFailBean.class)
									.getResult() == 1) {
								getInformationData(mApplication.getLOGIN_KEY(),
										1, 100, "");
							} else {
								$(R.id.no_message).setVisibility(View.VISIBLE);

							}
						}
						loading.setVisibility(View.GONE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void resume() {
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}

	private void getInformationData(String loginKey, int page, int size,
			String ids) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.PAGE, page);
		maps.put(DataTag.SIZE, size);
		maps.put(DataTag.IDS, ids);
		HttpTool.post(getContext(), Apis.GET_USER_INFO, loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						list.clear();
						if (StringTool.isNoBlankAndNoNull(response)) {
							InfoResponse infoResponse = JSON.decode(response,
									InfoResponse.class);
							int count = infoResponse.getCount();
							if (count > 0) {
								list = infoResponse.getList();
							} else {
								$(R.id.no_message).setVisibility(View.VISIBLE);

							}
						}
						adapter.setData(list);
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						loading.setVisibility(View.GONE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}
}
