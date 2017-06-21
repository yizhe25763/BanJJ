package com.shzy.bjj.activity.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.AppointmentAddressAddActivity;
import com.shzy.bjj.activity.home.ChooseAddressActivity;
import com.shzy.bjj.activity.home.ChooseBabyActivity;
import com.shzy.bjj.activity.login.LoginActivity;
import com.shzy.bjj.activity.mine.MineBabyActivity;
import com.shzy.bjj.adapter.ConditionGrideViewAdapter;
import com.shzy.bjj.adapter.ConditionTitleListViewAdapter;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.bean.AppointmentResponse;
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.bean.ConfigResponse;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.bean.OrderSubmitRequest;
import com.shzy.bjj.bean.OrderSubmitRequestBaby;
import com.shzy.bjj.bean.OrderSubmitRequestInsurances;
import com.shzy.bjj.bean.OrderSubmitRequestService;
import com.shzy.bjj.bean.OrderTimeRequest;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.bean.TeacherConditionBean;
import com.shzy.bjj.bean.TeacherDetailBean;
import com.shzy.bjj.bean.TeacherScheduleResponse;
import com.shzy.bjj.bean.TeacherServiceBean;
import com.shzy.bjj.bean.TeacherServiceTypeBean;
import com.shzy.bjj.bean.UserBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.ClassTag;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.DigestTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.umeng.analytics.MobclickAgent;

/**
 * 以老师端为主线，进行订单提交
 * 
 * @author suntao
 * 
 */

public class ChooseTeacherActivity extends BaseActivity implements
		OnClickListener {
	// 日历
	private GridView gridView;
	private ConditionGrideViewAdapter adapter = null;
	private TeacherBean teacherBean = null;
	public static BabyBean babyBean = null;
	private List<AppointmentBean> appointmentList;
	public static AppointmentBean addressBean = null;
	private UserBean userBean;
	private int course_count = 0;

	private LinearLayout order_teacher_layout;
	private LinearLayout order_course_layout;
	private LinearLayout order_address_layout;
	private LinearLayout order_baby_layout;
	// private LinearLayout order_memo_layout;
	private TextView teacher_name;
	private RatingBar ratingbar;
	private ImageView course_personal;
	private ImageView course_eachbaby;
	private LinearLayout layout_course;
	private LinearLayout layout_eachbaby;

	private TextView course_total;
	private TextView course_all;
	private TextView address;
	private TextView baby_name;
	private Button button;

	// 教师搜索默认条件
	private TeacherConditionBean conditionBean = new TeacherConditionBean();
	// 日历数据
	private List<List<MapBean>> dataList;
	// 日历表头
	private List<MapBean> titleList;
	// 1:轻早教 2：私人订制
	private int course = 2;
	/**
	 * 备注信息
	 */
	private String mContent;
	private EditText mContentEdt;
	/**
	 * 备注文本框字数显示
	 */
	private TextView mContentSize;

	private ScrollView scrollView;
	private LinearLayout btLayout;
	private String mTeacherDistrict;
	private String mAddressDistrict;

	/**
	 * 宝宝信息数量
	 */
	private int babyNum;
	/**
	 * 地址信息数量
	 */
	private int addressNum;

	public static void startActivity(Context context, TeacherBean bean) {
		context.startActivity(new Intent(context, ChooseTeacherActivity.class)
				.putExtra("bean", bean));
	}

	@Override
	public int bindLayout() {
		return R.layout.teacher_choose_order;
	}

	@Override
	public void initView(View view) {
		teacherBean = (TeacherBean) getIntent().getSerializableExtra("bean");
		titleList = MapBean.getCalendarHead(ConfigResponse
				.getMyServerTime(mApplication));

		scrollView = $(R.id.srcLayout);
		btLayout = $(R.id.btLayout);
		scrollView.setVisibility(View.GONE);
		btLayout.setVisibility(View.GONE);
		action_title.setText(R.string.home_order_time_title);
		order_teacher_layout = $(R.id.order_teacher_layout);
		order_course_layout = $(R.id.order_course_layout);
		order_address_layout = $(R.id.order_address_layout);
		order_baby_layout = $(R.id.order_baby_layout);

		mContentEdt = $(R.id.order_content);
		mContentSize = $(R.id.order_num_text);

		teacher_name = $(R.id.teacher_name);
		ratingbar = $(R.id.ratingbar);
		course_personal = $(R.id.course_personal);
		course_eachbaby = $(R.id.course_eachbaby);
		layout_course = $(R.id.layout_course);
		layout_eachbaby = $(R.id.layout_eachbaby);
		int service_type_count = teacherBean.getService_type_count();
		if (service_type_count == 2) {
			course = 2;
			course_personal.setEnabled(false);
			course_eachbaby.setEnabled(true);
		} else if (service_type_count == 1) {
			if (teacherBean.getService_list().get(0).getService_id() == 2) {
				course = 2;
				layout_course.setVisibility(View.VISIBLE);
				layout_eachbaby.setVisibility(View.INVISIBLE);
				course_personal.setEnabled(false);
				course_eachbaby.setEnabled(true);
			} else {
				course = 1;
				layout_course.setVisibility(View.INVISIBLE);
				layout_eachbaby.setVisibility(View.VISIBLE);
				course_personal.setEnabled(true);
				course_eachbaby.setEnabled(false);
			}
		} else {
			layout_course.setVisibility(View.INVISIBLE);
			layout_eachbaby.setVisibility(View.INVISIBLE);
		}
		course_total = $(R.id.course_total);
		course_all = $(R.id.course_all);
		address = $(R.id.address);
		baby_name = $(R.id.baby_name);
		button = $(R.id.okbt);
		initCalendar();
	}

	/**
	 * 老师详情
	 * 
	 * @param id
	 */
	protected void getTeacherDetail(int id) {
		Map maps = new HashMap<String, String>();
		maps.put("teacher_id", id);
		HttpTool.post(getContext(), Apis.TEACHER_DETAIL,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						loading.setVisibility(View.GONE);
						if (StringTool.isNoBlankAndNoNull(response)) {
							TeacherDetailBean teacherDetailBean = JSON.decode(
									response, TeacherDetailBean.class);
							if (teacherDetailBean != null) {
								mTeacherDistrict = teacherDetailBean
										.getDistrict();
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

	private void initCalendar() {
		GridView titleGridView = $(R.id.grideview_title);
		gridView = $(R.id.grideview);

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
				new GetCalendar(), true, config_time_max, 0, true,
				positionByTime);
		gridView.setAdapter(adapter);

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
			List<String> data = OrderTimeRequest.getOrderTimeSize(dataList);
			course_count = data.size();
			String size = String.valueOf(course_count);
			course_all.setText(size);
			course_total.setText(size);
			adapter.setData(dataList);
			adapter.setTotal(course_count);
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void initData(Context mContext) {
		if (teacherBean == null) {
			AppManager.getAppManager().finishActivity();
		}
		loading.setVisibility(View.VISIBLE);
		if (!HttpTool.checkNetwork(getContext())) {
			loading.setVisibility(View.GONE);
			return;
		}
		getTeacherScheule(teacherBean.getTeacher_id());
		teacher_name.setText(teacherBean.getTeacher_name());
		ratingbar.setRating(teacherBean.getTeacher_score() / 10);
		 if (mApplication.isLogin()) {
		 getUserAddress(mApplication.getLOGIN_KEY());
		 getUserBaby(mApplication.getLOGIN_KEY());
		 getTeacherDetail(teacherBean.getTeacher_id());
		 }
	}

	@Override
	public void initListener() {
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
				mContentSize.setText(140 - s.length() + "字");
			}
		});
		action_back.setOnClickListener(this);
		order_address_layout.setOnClickListener(this);
		order_baby_layout.setOnClickListener(this);
		button.setOnClickListener(this);
		course_personal.setOnClickListener(this);
		course_eachbaby.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.order_baby_layout:
			if (mApplication.isLogin()) {
				if (babyNum > 0) {
					Intent intent = new Intent(this, ChooseBabyActivity.class);
					this.startActivity(intent);
				} else {
					MineBabyActivity.startActivity(getContext(), true, null);
				}

			} else {
				MobclickAgent.onEvent(getContext(), "C0002");

				Intent intent = new Intent();
				intent.putExtra(DataTag.IS_FROM, DataConst.BABYID);
				intent.setClass(getContext(), ClassTag.LOGINACTIVITY);
				this.startActivity(intent);
			}
			break;

		case R.id.order_address_layout:
			if (mApplication.isLogin()) {
				if (addressNum > 0) {
					Intent intent = new Intent(this,
							ChooseAddressActivity.class);
					this.startActivity(intent);
				} else {
					AppointmentAddressAddActivity.startActivity(getContext(),
							true, null);
				}

			} else {
				MobclickAgent.onEvent(getContext(), "C0002");
				Intent intent = new Intent();
				intent.putExtra(DataTag.IS_FROM, DataConst.ADDRESSID);
				intent.setClass(getContext(), ClassTag.LOGINACTIVITY);
				this.startActivity(intent);
			}
			break;

		case R.id.course_personal:
			course = 2;
			course_personal.setEnabled(false);
			course_eachbaby.setEnabled(true);
			break;

		case R.id.course_eachbaby:
			course = 1;
			course_personal.setEnabled(true);
			course_eachbaby.setEnabled(false);
			break;

		case R.id.okbt:
			if (mApplication.isLogin()) {
				if (teacherBean == null) {
					ToastTool.toastMessage(this,
							R.string.toast_ordersubmit_teacher);
					return;
				}
				if (addressBean == null) {
					ToastTool.toastMessage(this,
							R.string.toast_ordersubmit_address);
					return;
				}
				if (babyBean == null) {
					ToastTool.toastMessage(this,
							R.string.toast_ordersubmit_baby);
					return;
				}
				if (course_count <= 0) {
					ToastTool.toastMessage(getContext(), "请选择时间");
					return;
				}

				if (!StringTool.isNoBlankAndNoNull(addressBean
						.getBusiness_district())) {
					ToastTool.toastMessage(getContext(), "当前地址不在该老师服务范围内");
					return;
				}
				if (!StringTool.isNoBlankAndNoNull(mTeacherDistrict)) {
					ToastTool.toastMessage(getContext(), "当前地址不在该老师服务范围内");
					return;
				}
				if (!checkBusiness(addressBean.getBusiness_district()
						.toString().trim(), mTeacherDistrict.toString().trim())) {
					ToastTool.toastMessage(getContext(), "当前地址不在该老师服务范围内");
					return;
				}
				Intent intent = new Intent(getContext(),
						AppointmentTeacherPayActivity.class);
				// 时间列表（用于页面数据显示类型）
				List<String> lenList = OrderTimeRequest.getOrderTime(dataList,
						titleList);
				StringBuilder builder = new StringBuilder();
				if (lenList.size() > 0) {
					for (int i = 0, len = lenList.size(); i < len; i++) {
						builder.append(lenList.get(i));
						if (i < len - 1) {
							builder.append(",");
						}
					}
				}
				intent.putStringArrayListExtra("datalist",
						(ArrayList<String>) lenList);
				intent.putExtra("lens", builder.toString().trim());
				// 日历时间
				List<MapBean> list = MapBean.getCalendarTimes(dataList,
						titleList);
				if (list == null || list.size() == 0) {
					return;
				}
				// 时间长度
				int length = list.size();

				List<TeacherServiceTypeBean> typeList = teacherBean
						.getService_type_list();
				List<TeacherServiceBean> serviceList = teacherBean
						.getService_list();
				int price = serviceList.get(course - 1).getService_price();
				OrderSubmitRequest request = new OrderSubmitRequest();
				request.setContact_id(addressBean.getId());
				request.setOrder_contact_name(addressBean.getName());
				request.setOrder_contact_phone(addressBean.getMobile());
				request.setOrder_contact_telphone(addressBean.getTelphone());
				request.setMemo(mContentEdt.getText().toString().trim());
				request.setUse_score(0);
				request.setUse_coupon_id(0);
				request.setPay_type(0);
				request.setOriginal_price(price * course_count);
				request.setDiscount_money(0);
				request.setScore_money(0);
				request.setCoupon_money(0);
				request.setTotal_price(price * course_count);

				List<OrderSubmitRequestBaby> babys = new ArrayList<OrderSubmitRequestBaby>();
				babys.add(new OrderSubmitRequestBaby(babyBean.getId()));
				request.setBabys(babys);

				List<OrderSubmitRequestInsurances> baby_insurances = new ArrayList<OrderSubmitRequestInsurances>();
				baby_insurances.add(new OrderSubmitRequestInsurances(babyBean
						.getName(), babyBean.getIdentity()));
				request.setBaby_insurances(baby_insurances);

				List<OrderSubmitRequestService> services = getSubmitService(
						price, list, mContentEdt.getText().toString().trim());
				request.setServices(services);
				// 传递参数给支付页面

				intent.putExtra("request", request);
				intent.putExtra("teacher", teacherBean);
				intent.putExtra("length", length);
				intent.putExtra("start", list.get(0));
				intent.putExtra("end", list.get(length - 1));
				intent.putExtra("baby", babyBean);
				intent.putExtra("address", addressBean);
				this.startActivity(intent);
			} else {
				LoginActivity.startActivity(getContext());
			}
			break;
		default:
			break;
		}
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

	private List<OrderSubmitRequestService> getSubmitService(int price,
			List<MapBean> list, String memo) {
		List<OrderSubmitRequestService> services = new ArrayList<OrderSubmitRequestService>();

		OrderSubmitRequestService service = null;
		for (MapBean map : list) {
			service = new OrderSubmitRequestService(1, 1, map.getId(), "陪伴玩乐",
					"陪伴玩乐", price, map.getName(), map.getTitle(), memo);
			services.add(service);
		}
		return services;
	}

	private String getDigest(String loginKey, String orderInfo) {
		StringBuilder builder = new StringBuilder();
		builder.append(Apis.ORDERKEY);
		builder.append(loginKey);
		builder.append(orderInfo);
		return DigestTool.md5(builder.toString().trim());
	}

	private String getOrderInfo(OrderSubmitRequest request) {
		Gson gson = new GsonBuilder().create();
		String result = gson.toJson(request);
		return result;
	}

	@Override
	public void resume() {

		MobclickAgent.onEvent(getContext(), "C0001");
		if (mApplication.isLogin()) {
			getTeacherDetail(teacherBean.getTeacher_id());
			if (addressBean != null) {
				addressNum = 1;
				address.setText(addressBean.getAddress());
			} else {
				getUserAddress(mApplication.getLOGIN_KEY());
			}
			if (babyBean != null) {
				babyNum = 1;
				baby_name.setText(babyBean.getName());
			} else {
				getUserBaby(mApplication.getLOGIN_KEY());
			}

		} else {

		}
	}

	@Override
	public void destroy() {
	}

	/**
	 * 获取用户地址信息
	 * 
	 * @param login_KEY
	 */
	private void getUserAddress(String login_KEY) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_KEY);
		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT, loading,maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							AppointmentResponse appointmentResponse = JSON
									.decode(response, AppointmentResponse.class);
							if (appointmentResponse != null) {
								addressNum = appointmentResponse.getCount();
								if (addressNum > 0) {
									appointmentList = appointmentResponse
											.getList();
									boolean flag = false;
									for (int i = 0, len = appointmentList
											.size(); i < len; i++) {
										AppointmentBean bean = appointmentList
												.get(i);
										if (1 == bean.getIs_default()) {
											flag = true;
											addressBean = bean;
											address.setText(addressBean
													.getAddress()+addressBean.getAddress_room());
										}
									}
									if (!flag) {
										addressBean = appointmentList.get(0);
										address.setText(addressBean
												.getAddress()+addressBean.getAddress_room());
									}
								} else {
									addressNum = 0;
									address.setText("");
									addressBean = null;
								}
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
							userBean = JSON.decode(response, UserBean.class);
							if (userBean != null) {
								babyNum = userBean.getBaby_count();
								if (babyNum > 0) {
									babyBean = userBean.getBaby_list().get(0);
									baby_name.setText(babyBean.getName());
								} else {
									babyBean = null;
									babyNum = 0;
									baby_name.setText("");
								}
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
	 * 老师排班表
	 * 
	 * @param id
	 */
	protected void getTeacherScheule(int id) {
		Map maps = new HashMap<String, String>();
		maps.put("teacher_id", id);

		HttpTool.post(getContext(), Apis.TEACHER_SCHEDULE,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							TeacherScheduleResponse teacherScheduleResponse = JSON
									.decode(response,
											TeacherScheduleResponse.class);
							if (teacherScheduleResponse != null) {
								dataList = MapBean.getData(
										teacherScheduleResponse.getDate_list(),
										titleList, getContext());
								adapter.setData(dataList);
								adapter.notifyDataSetChanged();
								scrollView.setVisibility(View.VISIBLE);
								btLayout.setVisibility(View.VISIBLE);
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

}
