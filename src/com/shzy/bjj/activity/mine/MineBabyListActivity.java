package com.shzy.bjj.activity.mine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.mine.MineBabyActivity;
import com.shzy.bjj.activity.teacher.ChooseTeacherActivity;
import com.shzy.bjj.adapter.BabyListAdapter;
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.bean.LoginSuccessUserChooseBean;
import com.shzy.bjj.bean.UserBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.view.CustomListView;

/**
 * 
 * 
 * @brief 宝宝信息管理
 * @author Fanhao Yi
 * @data 2015年9月1日上午10:28:46
 * @version V1.0
 */
public class MineBabyListActivity extends BaseActivity {

	private CustomListView listView;
	private BabyListAdapter adapter;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	LoginSuccessUserChooseBean mLoginSuccessUserChooseBean;

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, MineBabyListActivity.class));
	}

	@Override
	public int bindLayout() {

		return R.layout.activity_choose_baby;
	}

	@Override
	public void initView(View view) {
		imageLoader = ImageLoader.getInstance();
		listView = $(R.id.listview);

	}

	@Override
	public void initData(Context mContext) {
		action_title.setText("宝宝信息管理");
		action_right.setText("添加");
		if (mApplication.isLogin()) {
			loading.setVisibility(View.VISIBLE);
			getUserInfo(mApplication.getLOGIN_KEY());
		}
		adapter = new BabyListAdapter(this, imageLoader, false);
		listView.setAdapter(adapter);
	}

	@Override
	public void initListener() {
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				AppManager.getAppManager().finishActivity();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BabyBean bean = (BabyBean) arg0.getItemAtPosition(arg2);
				if (bean != null) {
					MineBabyActivity.startActivity(MineBabyListActivity.this,
							false, bean);
				}

			}
		});
		action_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				MineBabyActivity.startActivity(getContext(), true, null);
			}
		});
	}

	@Override
	public void resume() {
		if (mApplication.isLogin()) {
			loading.setVisibility(View.VISIBLE);
			getUserInfo(mApplication.getLOGIN_KEY());
		}
	}

	private void getUserInfo(String loginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);

		HttpTool.post(getContext(), Apis.GET_USER,loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							UserBean userBean = JSON.decode(response,
									UserBean.class);
							if (userBean != null) {
								if (userBean.getBaby_count() > 0) {
									((LinearLayout) $(R.id.no_message))
											.setVisibility(View.GONE);
									reseView(userBean);
								} else {
									((LinearLayout) $(R.id.no_message))
											.setVisibility(View.VISIBLE);
									((TextView) $(R.id.no_message_contents))
											.setText("暂时还没有宝宝信息,要不添加一个吧!");
								}
							}
						}
						loading.setVisibility(View.GONE);

					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						loading.setVisibility(View.GONE);
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});
	}

	private void reseView(UserBean userBean) {
		if (userBean != null) {
			List<BabyBean> list = userBean.getBaby_list();
			if (list != null) {
				adapter.setData(list);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void destroy() {

	}

}
