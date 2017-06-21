package com.shzy.bjj.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.bean.BaiDuMapBusiness;
import com.shzy.bjj.bean.BusinessResponse;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.tools.ValidTool;

/**
 * 
 * 
 * @brief 新增预约地址
 * @author Fanhao Yi
 * @data 2015年7月10日上午10:53:43
 * @version V1.0
 */
public class AppointmentAddressAddActivity extends BaseActivity implements
		OnClickListener {
	private EditText contactEditText;
	private EditText phoneEditText;
	private EditText mobileEditText;
	private TextView addressEditText;
	private Button deleteButton;
	private boolean tag = false;
	private AppointmentBean appointmentBean;

	private String mAddress;
	private double lat;
	private double log;
	private String mDescAddress;
	private String mBusiness;
	private List<String> mBusinessList;
	private String mUserBusiness;

	private EditText mAddressInfoEdt;
	private String mAddressInfo;

	private TableRow vAddressTab;

	public static void startActivity(Context context, boolean flag,
			AppointmentBean appointmentBean) {
		context.startActivity(new Intent(context,
				AppointmentAddressAddActivity.class).putExtra("tag", flag)
				.putExtra("bean", appointmentBean));
	}

	@Override
	public int bindLayout() {
		return R.layout.appointment_address_info;
	}

	@Override
	public void initView(View view) {
		tag = getIntent().getBooleanExtra("tag", false);
		action_right.setText(R.string.save);
		action_title.setText(R.string.appointment_add_address);
		contactEditText = $(R.id.contact);
		phoneEditText = $(R.id.telphone);
		mobileEditText = $(R.id.mobile);
		addressEditText = $(R.id.address);
		deleteButton = $(R.id.delete);
		mAddressInfoEdt = $(R.id.address_info);
		vAddressTab = $(R.id.address_layout);
		if (!tag) {
			appointmentBean = (AppointmentBean) getIntent()
					.getSerializableExtra("bean");
			if (appointmentBean != null) {
				contactEditText.setText(appointmentBean.getName());
				phoneEditText.setText(appointmentBean.getTelphone());
				mobileEditText.setText(appointmentBean.getMobile());
				addressEditText.setText(appointmentBean.getAddress());
			} else {
				tag = true;
			}
		} else {
			deleteButton.setVisibility(View.GONE);
		}
	}

	@Override
	public void initData(Context mContext) {

	}

	@Override
	public void initListener() {
		vAddressTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				Intent intent = new Intent();
				intent.setClass(getContext(),
						AppoinmentAddAddressActivity.class);
				startActivityForResult(intent, DataConst.REQUESTCODEOK);
			}
		});
		action_back.setOnClickListener(this);
		action_right.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
	}

	@Override
	public void resume() {
		if (lat > 0 && log > 0) {
			loading.setVisibility(View.VISIBLE);
			channelBusiness(lat, log);
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.action_back:
			if (ButtonTool.isFastClick()) {
				return;
			}
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.action_right:
			if (ButtonTool.isFastClick()) {
				return;
			}
			String name = contactEditText.getText().toString().trim();
			if (!StringTool.isNoBlankAndNoNull(name)) {
				ToastTool.toastMessage(this, R.string.toast_name_empty);
				return;
			}
			String mobile = mobileEditText.getText().toString().trim();
			if (!ValidTool.isPhoneNumberValid(mobile)) {
				ToastTool.toastMessage(getContext(), R.string.phone_lack);
				return;
			}
			String phone = phoneEditText.getText().toString().trim();
			String mAddress = addressEditText.getText().toString().trim();
			if (!StringTool.isNoBlankAndNoNull(mAddress)) {
				ToastTool.toastMessage(this, R.string.toast_address_empty);
				return;
			}
			mAddressInfo = mAddressInfoEdt.getText().toString().trim();
			if (!StringTool.isNoBlankAndNoNull(mAddressInfo)) {
				ToastTool.toastMessage(this, R.string.toast_address_info_empty);
				return;
			}
			if (mApplication.isLogin()) {
				if (tag) {
					loading.setVisibility(View.VISIBLE);
					// String mUserAddressInfo = mAddress + mAddressInfo;
					addAppointment(mApplication.getLOGIN_KEY(), name, mobile,
							phone, mAddress, mAddressInfo, String.valueOf(lat),
							String.valueOf(log));
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 转换商圈
	 * 
	 * @param lat
	 * @param log
	 */
	private void channelBusiness(double lat, double log) {
		String urls = "http://api.map.baidu.com/geocoder?" + "location=" + lat
				+ "," + log
				+ "&output=json&key=28bcdd84fae25699606ffad27f8da77b";
		HttpTool.get(getContext(), urls, new StringHandler(null) {
			BaiDuMapBusiness bean = null;

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					bean = JSON.decode(response, BaiDuMapBusiness.class);
					if (bean != null) {
						if (bean.getStatus().equals("OK")) {
							mBusiness = bean.getResult().getBusiness()
									.toString().trim();
							String city = bean.getResult()
									.getAddressComponent().getCity().toString()
									.trim();
							if (city.equals(DataConst.mCity)) {

								// comparison(mBusiness, city);
								addressEditText.setText(mAddress);
								// if (StringTool.isNoBlankAndNoNull(mBusiness))
								// {
								// comparison(mBusiness, city);
								// } else {
								// ToastTool.toastMessage(getContext(),
								// "当前地址未开通服务，请重新选择地址！");
								// }
							} else {
								ToastTool.toastMessage(getContext(),
										"当前城市未开通服务，请重新选择城市！");
							}

						} else {

						}
					} else {

					}
				} else {

				}
				loading.setVisibility(View.GONE);
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				loading.setVisibility(View.GONE);
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});
	}

	/**
	 * 百度地图商圈与服务端商圈对比
	 * 
	 * @param mBusiness
	 *            百度地图返回的商圈信息
	 */

	private void comparison(final String mBusiness, String city) {
		Map maps = new HashMap<String, String>();
		maps.put("city", city);
		HttpTool.post(getContext(), Apis.CLIENT_BUSINESS_LIST,loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							BusinessResponse businessResponse = JSON.decode(
									response, BusinessResponse.class);
							if (mBusiness.contains(",")) {
								String[] ary = mBusiness.trim().split(",");
								mBusinessList = java.util.Arrays.asList(ary);
								List<String> mServerBusinessList = new ArrayList<String>();
								for (int i = 0; i < businessResponse
										.getArea_count(); i++) {
									for (int j = 0; j < businessResponse
											.getArea_list().get(i)
											.getDistrict_list().size(); j++) {
										String name = businessResponse
												.getArea_list().get(i)
												.getDistrict_list().get(j)
												.getDistrict_name();
										mServerBusinessList.add(name);
									}
								}
								HashSet<String> setTotal = new HashSet<String>();
								HashSet<String> setBusiness = new HashSet<String>();
								HashSet<String> setServerBusiness = new HashSet<String>();
								setServerBusiness.addAll(mServerBusinessList);
								setBusiness.addAll(mBusinessList);
								setTotal.addAll(setBusiness);
								setTotal.retainAll(setServerBusiness);
								int length = setTotal.toString().trim()
										.length();
								if (length > 0) {
									mUserBusiness = setTotal.toString().trim()
											.substring(1, length - 1);
									addressEditText.setText(mAddress);
								} else {
									ToastTool.toastMessage(getContext(),
											"您所选的地址不在老师的服务范围内");
									return;
								}

							} else {
								List<String> mServerBusinessList = new ArrayList<String>();
								for (int i = 0; i < businessResponse
										.getArea_count(); i++) {
									for (int j = 0; j < businessResponse
											.getArea_list().get(i)
											.getDistrict_list().size(); j++) {
										String name = businessResponse
												.getArea_list().get(i)
												.getDistrict_list().get(j)
												.getDistrict_name();
										if (name.equals(mBusiness)) {
											mUserBusiness = name;
											addressEditText.setText(mAddress);
											return;
										} else {
											ToastTool.toastMessage(
													getContext(),
													"您所选的地址不在老师的服务范围内");
											return;
										}
									}
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

	private void addAppointment(String loginKey, String name, String mobile,
			String telphone, String address, String addressInfo,
			String address_longitude, String address_latitude) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.NAME, name);
		maps.put(DataTag.MOBILE, mobile);
		maps.put(DataTag.TELPHONE, telphone);
		maps.put(DataTag.ADDRESS, address);
		if (StringTool.isNoBlankAndNoNull(addressInfo)) {
			maps.put("address_room", addressInfo);
		}
		maps.put("address_longitude", address_longitude);
		maps.put("address_latitude", address_latitude);
		if (!StringTool.isNoBlankAndNoNull(mBusiness)) {
			mUserBusiness = "";
		}
		maps.put("business_district", mBusiness);
		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT_CREATE, loading,maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						loading.setVisibility(View.GONE);
						if (StringTool.isNoBlankAndNoNull(response)) {
							AppManager.getAppManager().finishActivity();
						}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			switch (resultCode) {
			case 0:
				mAddress = data.getExtras().getString("address");
				lat = data.getExtras().getDouble("lat");
				log = data.getExtras().getDouble("log");

				break;

			}
		} catch (Exception e) {
		}
	}
}
