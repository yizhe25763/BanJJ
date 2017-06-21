package com.shzy.bjj.activity.mine;

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
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.AppoinmentAddAddressActivity;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.bean.BaiDuMapBusiness;
import com.shzy.bjj.bean.BusinessResponse;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.tools.ValidTool;

/**
 *
 * 
 * @author suntao
 * 
 */
public class MineAppointmentAddActivity extends BaseActivity implements
		OnClickListener {
	private EditText contactEditText;
	private EditText phoneEditText;
	private EditText mobileEditText;
	private TextView addressEditText;
	private Button deleteButton;
	private boolean tag = false;
	private AppointmentBean appointmentBean;
	private EditText mAddressInfoEdt;

	private String mAddress;
	private double lat;
	private double log;
	private String mDescAddress;
	private String mBusiness;
	private List<String> mBusinessList;
	private String mUserBusiness;
	private String mAddressInfo;

	private String address;

	public static void startActivity(Context context, boolean flag,
			AppointmentBean appointmentBean) {
		context.startActivity(new Intent(context,
				MineAppointmentAddActivity.class).putExtra("tag", flag)
				.putExtra("bean", appointmentBean));
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_appointment_insert;
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

		loading.setVisibility(View.GONE);
		if (!tag) {
			appointmentBean = (AppointmentBean) getIntent()
					.getSerializableExtra("bean");
			if (appointmentBean != null) {
				address = appointmentBean.getAddress();
				contactEditText.setText(appointmentBean.getName());
				phoneEditText.setText(appointmentBean.getTelphone());
				mobileEditText.setText(appointmentBean.getMobile());
				addressEditText.setText(appointmentBean.getAddress());
				String addressRoom = appointmentBean.getAddress_room();
				if (!StringTool.isNoBlankAndNoNull(appointmentBean
						.getAddress_room())) {
					addressRoom = "";
				}
				mAddressInfoEdt.setText(addressRoom);
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
		action_back.setOnClickListener(this);
		action_right.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		addressEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getContext(),
						AppoinmentAddAddressActivity.class);
				intent.putExtra("address", address);
				startActivityForResult(intent, DataConst.REQUESTCODEOK);
			}
		});
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
					}
				}
				loading.setVisibility(View.GONE);

			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
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
								mUserBusiness = setTotal.toString();
								int sizes = mUserBusiness.length();
								mUserBusiness = mUserBusiness.substring(1,
										sizes - 1);
								addressEditText.setText(mAddress);
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
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.action_right:
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
				loading.setVisibility(View.VISIBLE);
				// String mUserAddressInfo = mAddress + mAddressInfo;
				if (tag) {
					addAppointment(mApplication.getLOGIN_KEY(), name, mobile,
							phone, mAddress, mAddressInfo, String.valueOf(lat),
							String.valueOf(log));
				} else {
					modifyAppointment(mApplication.getLOGIN_KEY(), name,
							mobile, phone, mAddress, mAddressInfo,
							appointmentBean.getId(),
							appointmentBean.getIs_default());
				}
			}
			break;

		case R.id.delete:
			if (mApplication.isLogin()) {

				Intent intent = new Intent(this, DeleteAddressActivity.class);
				startActivityForResult(intent, 0);
				// DeleteAddressActivity.startActivity(getContext(),
				// appointmentBean);
			} else {
				loading.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
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
			mBusiness = "";
		}
		maps.put("business_district", mBusiness);

		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT_CREATE,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							AppManager.getAppManager().finishActivity();
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

	private void modifyAppointment(String loginKey, String name, String mobile,
			String telphone, String address, String addressInfo,
			int contact_id, int is_default) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.NAME, name);
		maps.put(DataTag.MOBILE, mobile);
		maps.put(DataTag.TELPHONE, telphone);
		maps.put(DataTag.ADDRESS, address);
		if (StringTool.isNoBlankAndNoNull(addressInfo)) {
			maps.put("address_room", addressInfo);
		}
		maps.put(DataTag.CONTACT_ID, contact_id);
		maps.put("is_default", is_default);

		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT_MODIFY,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							AppManager.getAppManager().finishActivity();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			switch (resultCode) {
			case 0:
				mAddress = data.getExtras().getString("address");
				lat = data.getExtras().getDouble("lat");
				log = data.getExtras().getDouble("log");
				break;

			case 1:
				loading.setVisibility(View.VISIBLE);
				deleteAppointment(mApplication.getLOGIN_KEY(),
						appointmentBean.getId());

			}
		} catch (Exception e) {
		}
	}

	private void deleteAppointment(String loginKey, int contact_id) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.CONTACT_ID, contact_id);

		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT_DELETE,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							loading.setVisibility(View.GONE);
							MineAppointmentActivity.startActivity(getContext());
							AppManager.getAppManager().finishActivity();
						}
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
