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
import android.widget.ListView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.adapter.AppointmentAdapter;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.bean.AppointmentResponse;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

/**
 * 预约人列表页面
 * 
 * @author suntao
 * 
 */
public class MineAppointmentActivity extends BaseActivity implements
		OnClickListener {
	private ListView listView;
	private AppointmentAdapter adapter;
	private List<AppointmentBean> data = new ArrayList<AppointmentBean>();

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, MineAppointmentActivity.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_appointment;
	}

	@Override
	public void initView(View view) {
		action_title.setText(R.string.mine_appointment_manage);
		action_right.setText(R.string.add);
		listView = $(R.id.listview);
	}

	@Override
	public void initData(Context mContext) {
		listView.setDivider(null);
		adapter = new AppointmentAdapter(this, new SetDefault() {

			@Override
			public void onReset(int position) {
				// 设为默认地址
				AppointmentBean bean = data.get(position);
				bean.setIs_default(1);
				// 直接设为默认
				if (mApplication.isLogin()) {
					loading.setVisibility(View.VISIBLE);
					modifyAppointment(mApplication.getLOGIN_KEY(), bean);
				} else {
					loading.setVisibility(View.GONE);
				}
			}
		});
		listView.setAdapter(adapter);
	}

	@Override
	public void initListener() {
		action_back.setOnClickListener(this);
		action_right.setOnClickListener(this);
	}

	@Override
	public void resume() {
		if (mApplication.isLogin()) {
			loading.setVisibility(View.VISIBLE);
			getAppointmentData(mApplication.getLOGIN_KEY());
		} else {
			loading.setVisibility(View.GONE);
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.action_right:
			MineAppointmentAddActivity.startActivity(this, true, null);
			break;

		default:
			break;
		}
	}

	private void getAppointmentData(String loginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							AppointmentResponse appointmentResponse = JSON
									.decode(response, AppointmentResponse.class);
							if (appointmentResponse != null) {
								int count = appointmentResponse.getCount();
								if (count > 0) {
									$(R.id.no_message).setVisibility(View.GONE);
									data = appointmentResponse.getList();
									adapter.setData(data);
									adapter.notifyDataSetChanged();
								} else {
									$(R.id.no_message).setVisibility(
											View.VISIBLE);
								}

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

	private void modifyAppointment(final String loginKey, AppointmentBean bean) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.NAME, bean.getName());
		maps.put(DataTag.MOBILE, bean.getMobile());
		maps.put(DataTag.TELPHONE, bean.getTelphone());
		maps.put(DataTag.ADDRESS, bean.getAddress());
		maps.put(DataTag.CONTACT_ID, bean.getId());
		maps.put("address_room", bean.getAddress_room());
		maps.put("is_default", bean.getIs_default());

		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT_MODIFY,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						ToastTool.toastMessage(MineAppointmentActivity.this,
								R.string.toast_appointment_set_default);
						getAppointmentData(mApplication.getLOGIN_KEY());
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});
	}

	public interface SetDefault {
		public void onReset(int position);
	}
}
