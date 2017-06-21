package com.shzy.bjj.activity.home;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.teacher.ChooseTeacherActivity;
import com.shzy.bjj.adapter.AddressListAdapter;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.bean.AppointmentResponse;
import com.shzy.bjj.bean.LoginSuccessUserChooseBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;

/**
 * 
 * 
 * @brief 选择地址
 * @author Fanhao Yi
 * @data 2015年7月6日上午10:46:21
 * @version V1.0
 */
public class ChooseAddressActivity extends BaseActivity {

	private TextView mAddressTxt;
	private Button mAddAddressBtn;
	private String mLoginKey;
	private AddressListAdapter mAdapter;
	private List<AppointmentBean> data = new ArrayList<AppointmentBean>();
	private int resultCode = 0;
	private ListView mAddressListView;
	private LoginSuccessUserChooseBean mLoginSuccessUserChooseBean;

	@Override
	public int bindLayout() {
		return R.layout.activity_choose_address;
	}

	@Override
	public void initView(View view) {
		mAddressTxt = $(R.id.address_content);
		mAddressListView = $(R.id.address_list);
		mAddAddressBtn = $(R.id.add_address_btn);

	}

	@Override
	public void initData(Context mContext) {
		action_title.setText("服务地点");
		data = new ArrayList<AppointmentBean>();
		mAdapter = new AddressListAdapter(ChooseAddressActivity.this);
		mAddressListView.setDivider(null);
		mAddressListView.setAdapter(mAdapter);

	}

	@Override
	public void initListener() {
		mAddressListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				AppointmentBean bean = (AppointmentBean) arg0
						.getItemAtPosition(arg2);
				AppointmentTimeActivity.addressBeans = bean;
				// 老师界面订单 选择宝宝
				ChooseTeacherActivity.addressBean = bean;
				AppManager.getAppManager().finishActivity();
			}
		});

		mAddAddressBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				AppointmentAddressAddActivity.startActivity(getContext(), true,
						null);
			}
		});

		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				AppManager.getAppManager().finishActivity();
			}
		});

	}

	@Override
	public void resume() {
		if (mApplication.isLogin()) {
			mLoginKey = mApplication.getLOGIN_KEY();
			getAddress(mLoginKey);
			loading.setVisibility(View.VISIBLE);
		}
	}

	private void getAddress(String mLoginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, mLoginKey);
		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT, loading,maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {

						if (StringTool.isNoBlankAndNoNull(response)) {
							AppointmentResponse appointmentResponse = JSON
									.decode(response, AppointmentResponse.class);
							int count = appointmentResponse.getCount();
							if (count > 0) {
								data = appointmentResponse.getList();
								mAdapter.setData(data);
								mAdapter.notifyDataSetChanged();
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

	@Override
	public void destroy() {

	}

}
