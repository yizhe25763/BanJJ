package com.shzy.bjj.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.login.LoginActivity;
import com.shzy.bjj.activity.mine.MineBabyActivity;
import com.shzy.bjj.activity.teacher.GetCalendarData;
import com.shzy.bjj.adapter.ConditionGrideViewAdapter;
import com.shzy.bjj.adapter.ConditionTitleListViewAdapter;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.bean.AppointmentResponse;
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.bean.CalendarTimeBean;
import com.shzy.bjj.bean.ConfigResponse;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.bean.OrderTimeRequest;
import com.shzy.bjj.bean.TeacherAppointmentRequest;
import com.shzy.bjj.bean.TeacherResponse;
import com.shzy.bjj.bean.TimesBean;
import com.shzy.bjj.bean.UserBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.ClassTag;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.ListTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.CustomDialog;
import com.shzy.bjj.view.Switch;
import com.shzy.bjj.view.Switch.OnSwitchChangeListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @brief 预约上门
 * @author Fanhao Yi
 * @data 2015年6月18日下午5:26:08
 * @version V1.0
 */
public class AppointmentTimeActivity extends BaseActivity {
	private ConditionGrideViewAdapter adapter;
	/**
	 * 自动匹配老师开关
	 */
	private Switch mTeacherBtn;
	/**
	 * 自动匹配老师
	 */
	private String mIsTeacher = "true";

	/**
	 * 备注文本框
	 */
	private EditText mContentEdt;
	/**
	 * 备注信息
	 */
	private String mContent;
	/**
	 * 备注文本框字数显示
	 */
	private TextView mContentSize;
	/**
	 * 地址
	 */
	private TableRow mAddressTab;
	/**
	 * 确认
	 */
	private Button mOkBtn;

	/**
	 * LoginKey
	 */
	private String mLoginKey;
	/**
	 * 宝宝
	 */
	private TableRow mBabyTab;
	/**
	 * 地址
	 */
	private TextView mAddressText;
	private AppointmentBean addressBean;
	/**
	 * 宝宝
	 */
	private TextView mBabyText;
	private BabyBean babyBean;
	/**
	 * 帮助
	 */
	private ImageView mSwitchHelp;
	GridView gridView;
	// 日历数据
	protected List<List<MapBean>> dataList = MapBean.getData(null, null, this);
	// 日历表头
	protected List<MapBean> titleList;

	private TextView mOrderNum;
	/**
	 * 共多少课时
	 */
	private int mOrderNums;

	private List<TimesBean> lists;
	/**
	 * 宝宝信息数量
	 */
	private int babyNum;
	/**
	 * 地址信息数量
	 */
	private int addressNum;

	/**
	 * 地址回传数据
	 */
	public static AppointmentBean addressBeans;

	/**
	 * 宝宝回传数据
	 */
	public static BabyBean babyBeans;
	private CustomDialog dialog;

	@Override
	public int bindLayout() {
		return R.layout.activity_appoinment_time;

	}

	@Override
	public void initView(View view) {
		mTeacherBtn = $(R.id.switch_btn);
		mContentEdt = $(R.id.order_content);
		mContentSize = $(R.id.order_num_text);
		mAddressTab = $(R.id.order_address);
		mOkBtn = $(R.id.order_ok_btn);
		mBabyTab = $(R.id.order_baby);
		mAddressText = $(R.id.address_content);
		mBabyText = $(R.id.baby_content);
		mSwitchHelp = $(R.id.switch_help);
		mOrderNum = $(R.id.order_num);
	}

	private void initCalendar() {
		GridView titleGridView = $(R.id.grideview_title);
		gridView = $(R.id.grideview);
		RelativeLayout layout = $(R.id.clear_layout);
		layout.setVisibility(View.GONE);
		titleList = MapBean.getCalendarHead(ConfigResponse
				.getMyServerTime(mApplication));

		int config_time_max = 10;
		ConfigResponse configResponse = (ConfigResponse) mApplication
				.readObject(DataConst.CONFIG);
		if (configResponse != null) {
			config_time_max = configResponse.getOrder_time_max_limit();
		}
		// isMAX=是否限制选择数量 flag:是否初始化timeID
		int positionByTime = MapBean.getTimePositionByTime(MapBean
				.getCurrentTime(mApplication));
		adapter = new ConditionGrideViewAdapter(getContext(),
				new GetCalendar(), false, config_time_max, 0, true,
				positionByTime);
		gridView.setAdapter(adapter);
		adapter.setData(dataList);
		adapter.notifyDataSetChanged();

		ConditionTitleListViewAdapter titleAdapter = new ConditionTitleListViewAdapter(
				getContext());
		titleGridView.setAdapter(titleAdapter);
		titleAdapter.setData(titleList);
		titleAdapter.notifyDataSetChanged();
	}

	class GetCalendar implements GetCalendarData {

		@Override
		public void onClick(int tag, int position, MapBean bean) {
			dataList.get(tag).set(position, bean);
			List<String> d = OrderTimeRequest.getOrderTimeSize(dataList);
			mOrderNums = d.size();
			mOrderNum.setText("共选择课时:" + mOrderNums);
			adapter.setData(dataList);
			adapter.setTotal(mOrderNums);
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void initData(Context mContext) {
		if (mApplication.isLogin()) {
			getUserAddress(mApplication.getLOGIN_KEY());
			getUserBaby(mApplication.getLOGIN_KEY());
		}
		// ((RelativeLayout) $(R.id.bottom_btn)).getBackground().setAlpha(80);
	}

	@Override
	protected void onResume() {
		initCalendar();
		// 初始化标题
		gridView.setFocusable(false);
		mOkBtn.setClickable(true);
		action_title.setText(R.string.home_order_time_title);
		if (mApplication.isLogin()) {
			if (addressBeans != null) {
				addressNum = 1;
				addressBean = addressBeans;
				mAddressText.setText(addressBeans.getAddress()+addressBeans.getAddress_room());
			} else {
				getUserAddress(mApplication.getLOGIN_KEY());
			}

			if (babyBeans != null) {
				babyNum = 1;
				babyBean = babyBeans;
				mBabyText.setText(babyBeans.getName());
			} else {
				getUserBaby(mApplication.getLOGIN_KEY());
			}
		}
		super.onResume();
	}

	@Override
	public void initListener() {

		/**
		 * 帮助
		 * 
		 */
		mSwitchHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}

				CustomDialog.Builder customBuilder = new CustomDialog.Builder(
						getContext());
				customBuilder
						.setTitle("说明")
						.setMessage("会自动根据您宝宝的年龄匹配合适的老师,如果不满意也可随时手动更换")
						.setPositiveButton("知道了",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				dialog = customBuilder.create();
				dialog.show();

			}
		});
		/**
		 * 宝宝
		 */
		mBabyTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (mApplication.isLogin()) {
					if (babyNum > 0) {// 跳转到宝宝选择
						getIntentTool().forward(ChooseBabyActivity.class);
					} else {// 跳转到添加宝宝页面
						MineBabyActivity
								.startActivity(getContext(), true, null);
					}
				} else {// 跳转到登录页面
					Intent intent = new Intent();
					intent.putExtra(DataTag.IS_FROM, DataConst.BABYID);
					MobclickAgent.onEvent(getContext(),
							getString(R.string.b_02));
					intent.setClass(getContext(), ClassTag.LOGINACTIVITY);
					// startActivity(intent);
					startActivityForResult(intent, DataConst.REQUESTCODEOK);

				}
			}
		});
		/**
		 * 返回按钮
		 */
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				AppManager.getAppManager().finishActivity();
			}
		});
		/**
		 * 确认
		 */
		mOkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mOkBtn.setClickable(false);
				if (ButtonTool.isFastClick()) {
					return;
				}
				if (DataConst.aggressive == 0) {
					ToastTool.toastMessage(getContext(), "网络不畅通，请检查网络设置");
					mOkBtn.setClickable(true);
					getConfig();
					return;
				}
				List<String> list = OrderTimeRequest.getOrderTime(dataList,
						titleList);
				String time = CalendarTimeBean.getCalendarTime(dataList,
						titleList);
				mContent = mContentEdt.getText().toString().trim();
				if (mApplication.isLogin()) {
					if (StringTool.isNoBlankAndNoNull(time) && babyBean != null
							&& addressBean != null && mOrderNums > 0) {
						TeacherAppointmentRequest request = new TeacherAppointmentRequest(
								list, addressBean, mContent, babyBean, time,
								mOrderNums);
						if (!StringTool.isNoBlankAndNoNull(DataConst.mCity)) {
							DataConst.mCity = getString(R.string.city);
						}
						if (!StringTool.isNoBlankAndNoNull(request.getAddress()
								.getBusiness_district())) {
							ToastTool.toastMessage(getContext(), "该时间没有老师");
							mOkBtn.setClickable(true);
							return;
						}
						loading.setVisibility(View.VISIBLE);
						getTeacherList(DataConst.mCity, request);

					} else {
						mOkBtn.setClickable(true);
						if (addressBean == null) {
							ToastTool.toastMessage(getContext(),
									R.string.choose_address);
							return;
						}
						if (babyBean == null) {
							ToastTool.toastMessage(getContext(),
									R.string.choose_baby);
							return;
						}
						if (mOrderNums <= 0) {
							ToastTool.toastMessage(getContext(),
									R.string.choose_time);
							return;

						}
					}
				} else {
					mOkBtn.setClickable(true);
					LoginActivity.startActivity(getContext());

				}
			}

		});
		/**
		 * 地址
		 */
		mAddressTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mApplication.isLogin()) {
					if (addressNum > 0) {// 跳转到地址选择
						// Intent intent = new Intent();
						// intent.setClass(getContext(),
						// ChooseAddressActivity.class);
						// startActivityForResult(intent,
						// DataConst.REQUESTCODEOK);
						getIntentTool().forward(ChooseAddressActivity.class);
					} else {// 跳转到添加地址页面
						AppointmentAddressAddActivity.startActivity(
								getContext(), true, null);
					}

				} else {// 跳转到登录页面
					// getIntentTool().forward(LoginActivity.class);

					Intent intent = new Intent();
					intent.putExtra(DataTag.IS_FROM, DataConst.ADDRESSID);
					MobclickAgent.onEvent(getContext(),
							getString(R.string.b_02));
					intent.setClass(getContext(), ClassTag.LOGINACTIVITY);
					// startActivity(intent);
					startActivityForResult(intent, DataConst.REQUESTCODEOK);
				}
			}
		});
		/**
		 * 备注文本框
		 */
		mContentEdt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mContentSize.setText(200 - s.length() + "字");
			}
		});
		/**
		 * 自动匹配老师资源开关
		 */
		mTeacherBtn.setOnSwitchChangeListener(new OnSwitchChangeListener() {

			@Override
			public void onSwitchChanged(boolean open) {
				if (open) {
					mIsTeacher = "true";
				} else {
					mIsTeacher = "false";
				}

			}
		});

	}

	@Override
	public void resume() {
		MobclickAgent.onEvent(getContext(), getString(R.string.b_01));
	}

	/**
	 * 获取宝宝信息
	 * 
	 * @param login_KEY
	 */
	private void getUserBaby(String login_KEY) {

		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_KEY);
		HttpTool.post(getContext(), Apis.GET_USER,loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							UserBean userBean = JSON.decode(response,
									UserBean.class);
							babyNum = userBean.getBaby_count();
							if (babyNum > 0) {
								babyBean = userBean.getBaby_list().get(0);
								mBabyText.setText(babyBean.getName());
							} else {
								babyNum = 0;
								mBabyText.setText("");
								babyBeans = null;
							}

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

	/**
	 * 获取用户地址信息
	 * 
	 * @param login_KEY
	 */
	private void getUserAddress(String login_KEY) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_KEY);
		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT,loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							AppointmentResponse appointmentResponse = JSON
									.decode(response, AppointmentResponse.class);
							addressNum = appointmentResponse.getCount();
							if (addressNum > 0) {
								List<AppointmentBean> appointmentBeanList = appointmentResponse
										.getList();
								boolean flag = false;
								for (int i = 0; i < addressNum; i++) {
									AppointmentBean appointmentBean = appointmentBeanList
											.get(i);
									if (appointmentBean.getIs_default() == 1) {
										addressBean = appointmentBean;
										mAddressText.setText(addressBean
												.getAddress()+addressBean.getAddress_room());
										flag = true;
									}
								}
								if (flag == false) {
									addressBean = appointmentBeanList.get(0);
									mAddressText.setText(addressBean
											.getAddress()+addressBean.getAddress_room());
								}
							} else {
								addressNum = 0;
								mAddressText.setText("");
								addressBeans = null;
							}
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

	@Override
	public void destroy() {

	}

	/**
	 * 获取对应时间的教师列表
	 * 
	 * @param mCitys
	 *            所在城市
	 * @param mTimes
	 *            选择时间段
	 */
	private void getTeacherList(String mCitys,
			final TeacherAppointmentRequest request) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, mApplication.getLOGIN_KEY());
		maps.put("city", getString(R.string.city));
		maps.put("condition_time", request.getTime());
		maps.put("condition_district", request.getAddress()
				.getBusiness_district());
		HttpTool.post(getContext(), Apis.GET_AUTOMATCH_SEARCH_LIST,loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							TeacherResponse bean = JSON.decode(response,
									TeacherResponse.class);
							if (bean != null
									&& bean.getCount() == request.getDataList()
											.size()) {
								List<String> mServerBusinessList = new ArrayList<String>();
								for (int i = 0; i < bean.getCount(); i++) {
									for (int j = 0; j < bean.getList().get(i)
											.getDistrict_list().size(); j++) {
										String name = bean.getList().get(i)
												.getDistrict_list().get(j)
												.getDistrict();
										mServerBusinessList.add(name);
									}
								}
								if (!checkBusiness(request.getAddress()
										.getBusiness_district(), ListTool
										.join(mServerBusinessList))) {
									ToastTool.toastMessage(getContext(),
											"该时间没有老师");
									return;
								}
								;
								if (mIsTeacher.equals("true")) {
									AppointmentListActivity.startActivity(
											getContext(), request, true, bean);
								} else {
									AppointmentListActivity.startActivity(
											getContext(), request, false, null);
								}
							} else {
								List<String> list1 = request.getDataList();
								List<String> list2 = new ArrayList<String>();
								List<String> list3 = new ArrayList<String>();
								int size = bean.getList().size();
								if (size > 0) {
									for (int j = 0; j < size; j++) {
										list2.add(bean
												.getList()
												.get(j)
												.getAuto_match_condition_times());
									}
									for (String i : list1) {
										if (!list2.contains(i)) {
											list3.add(i);
										}
									}
									String noTime = ListTool.join(list3);
									String[] ary;
									List<String> mBusinessList = new ArrayList<String>();
									List<String> noTimeList = new ArrayList<String>();
									if (noTime.contains(",")) {
										ary = noTime.trim().split(",");
										mBusinessList = java.util.Arrays
												.asList(ary);
										for (int i = 0; i < mBusinessList
												.size(); i++) {
											noTimeList.add(channelTime(ary[i]));
										}
										String content = ListTool.join(
												noTimeList).replaceAll(",",
												"\n")
												+ "\n暂无老师，请重新选择时间";
										commonIntent("提示", content);
									} else {
										String contents = channelTime(noTime)
												+ "\n暂无老师，请重新选择时间";
										commonIntent("提示", contents);
									}
									loading.setVisibility(View.GONE);
								} else {
									commonIntent("提示", "当前所选的时间无老师，请重新选择时间");
									loading.setVisibility(View.GONE);

								}
							}
							mOkBtn.setClickable(true);

						}
						loading.setVisibility(View.GONE);

					}

					private String channelTime(String noTime) {
						String month = noTime.substring(5, 7);
						String day = noTime.substring(8, 10);
						String beginTime = noTime.substring(11, 13);
						String endTime = noTime.substring(31, 33);
						String content = month + "月" + day + "日" + beginTime
								+ "点到" + endTime + "点";
						return content;
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
						mOkBtn.setClickable(true);

					}
				});
	}

	private void commonIntent(String title, String content) {
		Intent intent = new Intent();
		intent.setClass(getContext(), CommonDialogActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("content", content);
		startActivity(intent);

	}

	/**
	 * 判断当前地址与老师的服务商圈
	 * 
	 * @param business_district
	 *            当前地址对应商圈
	 * @param mTeacherDistrict
	 *            服务地址
	 * @return
	 */
	private boolean checkBusiness(String business_district,
			String mTeacherDistrict) {
		List<String> mBusinessList = new ArrayList<String>();
		List<String> mServerBusinessList = new ArrayList<String>();
		if (business_district.contains(",")) {
			String[] ary = business_district.trim().split(",");
			mBusinessList = java.util.Arrays.asList(ary);
		} else {
			mBusinessList.add(business_district);
		}
		if (mTeacherDistrict.contains(",")) {
			String[] arys = mTeacherDistrict.trim().split(",");
			mServerBusinessList = java.util.Arrays.asList(arys);
		} else {
			mServerBusinessList.add(mTeacherDistrict);

		}
		HashSet<String> setTotal = new HashSet<String>();
		HashSet<String> setBusiness = new HashSet<String>();
		HashSet<String> setServerBusiness = new HashSet<String>();
		setServerBusiness.addAll(mServerBusinessList);
		setBusiness.addAll(mBusinessList);
		setTotal.addAll(setBusiness);
		setTotal.retainAll(setServerBusiness);
		int length = setTotal.toString().trim().length();
		if (length > 3) {
			return true;
		} else {
			return false;
		}
	}

	protected void getConfig() {
		Map maps = new HashMap<String, String>();
		HttpTool.post(getContext(), Apis.CLIENT_CONFIG,loading, maps,
				new StringHandler(null) {
					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							ConfigResponse configResponse = JSON.decode(
									response, ConfigResponse.class);
							if (configResponse != null) {
								DataConst.aggressive = configResponse
										.getDiscount_list().get(0)
										.getAggressive();
								mApplication.saveObject(configResponse,
										DataConst.CONFIG);
							}
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
