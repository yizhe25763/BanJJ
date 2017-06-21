package com.shzy.bjj.activity.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import net.sourceforge.simcpux.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.login.LoginActivity;
import com.shzy.bjj.activity.teacher.AppointmentTeacherPayActivity;
import com.shzy.bjj.activity.teacher.VoucherActivity;
import com.shzy.bjj.adapter.AppoinmentPayTeacherListAdapter;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.bean.AppointmentPayRespone;
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.bean.ConfigResponse;
import com.shzy.bjj.bean.CouponMoneyResponse;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.bean.OrderSubmitRequest;
import com.shzy.bjj.bean.OrderSubmitRequestBaby;
import com.shzy.bjj.bean.OrderSubmitRequestInsurances;
import com.shzy.bjj.bean.OrderSubmitRequestService;
import com.shzy.bjj.bean.RequestFailBean;
import com.shzy.bjj.bean.ServiceTimeBean;
import com.shzy.bjj.bean.ServiceTimeIdBean;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.bean.TeacherServiceBean;
import com.shzy.bjj.bean.TeacherServiceTypeBean;
import com.shzy.bjj.bean.TimeBean;
import com.shzy.bjj.bean.TimesBean;
import com.shzy.bjj.bean.VoucherBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.DigestTool;
import com.shzy.bjj.tools.DoubleTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 
 * @brief 订单支付
 * @author Fanhao Yi
 * @data 2015年7月1日下午5:13:04
 * @version V1.0
 */
public class AppointmentPayActivity extends BaseActivity {
	/**
	 * 积分抵用券
	 */
	private TableRow couponMoney;
	private Button mOrderOkBtn;
	private TextView voucherTextView;
	public static VoucherBean voucherBean = null;
	public static List<VoucherBean> datas = null;

	/**
	 * 地址
	 */
	private TextView mAddressTxt;
	private AppointmentBean mAddress;
	/**
	 * 宝宝
	 */
	private TextView mBabyTxt;
	private BabyBean mBaby;

	/**
	 * 老师列表
	 */
	private TeacherBean mTeacherBean;
	/**
	 * 微信支付
	 */
	private ImageButton mWechatBtn;
	private int mPayType = 1;
	/**
	 * 支付宝支付
	 */
	private ImageButton mALiPayBtn;

	/**
	 * 老师列表
	 */
	private GridView mTeacherGallery;
	/**
	 * 
	 */
	private AppoinmentPayTeacherListAdapter mAdapter;
	/**
	 * 订单数据
	 */
	public static List<Parcelable> data = null;
	/**
	 * 
	 */
	private List<TeacherBean> lists = null;
	/**
	 * 最终成交总价格 （单位为分）
	 */
	private int total;
	/**
	 * 订单单价
	 */
	private Double mPrice;

	/**
	 * 订单原价
	 */
	private Double originalPrice = (double) 0;
	private TextView mTotalPriceTxt;
	/**
	 * 订单折扣比率
	 */
	private int aggressive;
	/**
	 * 未使用优惠券的总价
	 */
	private Double mFinalTotal = (double) 0;
	private String times;
	/**
	 * 代金券控件
	 */
	private TextView voucherTxt;

	/**
	 * 代金券横线
	 */
	private View line;
	private LinearLayout voucherLayout;
	private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private TextView vOrderNum;

	private String totals;

	/**
	 * 优惠券数据源
	 */
	List<VoucherBean> voucherDataList = new ArrayList<VoucherBean>();
	Double couponMoneys = 0.0;
	int couponMoneyIDS = 0;

	@Override
	public int bindLayout() {
		return R.layout.activity_appointment_pay;
	}

	@Override
	public void initView(View view) {
		voucherBean = null;
		voucherTxt = $(R.id.voucher_content);
		line = $(R.id.voucher_line);
		voucherLayout = $(R.id.voucher_layout);
		vOrderNum = $(R.id.order_num);

		mAddressTxt = $(R.id.address_content);
		mOrderOkBtn = $(R.id.order_ok_btn);
		mBabyTxt = $(R.id.babay_content);
		mTeacherGallery = $(R.id.order_time_gallery);
		mTotalPriceTxt = $(R.id.total_price);
		mWechatBtn = $(R.id.weixin_pay_bt);
		mALiPayBtn = $(R.id.ali_pay_bt);
		couponMoney = $(R.id.coupon_money);
		voucherTextView = $(R.id.voucher);
		mALiPayBtn.setEnabled(false);
		mWechatBtn.setEnabled(true);
	}

	@Override
	public void initData(Context mContext) {
		msgApi.registerApp(Constants.APP_ID);
		mOrderOkBtn.setEnabled(true);
		action_title.setText("订单确认");
		data = (List<Parcelable>) getIntent().getParcelableArrayListExtra(
				"data");
		mAddress = ((TimesBean) data.get(0)).getTimeBean().getAddress();
		mAddressTxt.setText(mAddress.getAddress()+mAddress.getAddress_room());
		mBaby = ((TimesBean) data.get(0)).getTimeBean().getBaby();
		mBabyTxt.setText(mBaby.getName());
		mTeacherBean = ((TimesBean) data.get(0)).getTeacherBean();
		int size = data.size();
		ConfigResponse response = (ConfigResponse) mApplication
				.readObject(DataConst.CONFIG);
		if (response != null) {
			aggressive = response.getDiscount_list().get(0).getAggressive();
		}
		for (int i = 0; i < size; i++) {
			TimesBean mTimesBean = (TimesBean) data.get(i);
			TimeBean timeBean = mTimesBean.getTimeBean();
			String times = timeBean.getOrderTime();
			String mStartTime = DateChannel.channelStartTime(times);
			String mEndTime = DateChannel.channelEndTime(times);
			int mStartTimes = Integer.valueOf(mStartTime);
			int mEndTimes = Integer.valueOf(mEndTime);
			int timesCount = mEndTimes - mStartTimes;
			if (mTimesBean.getTeacherBean().getService_list().get(0)
					.getService_id() == 1) {
				/**
				 * 订单单价
				 */
				int price = ((TimesBean) data.get(i)).getTeacherBean()
						.getService_list().get(0).getService_price();
				// 订单原始总价
				originalPrice = (double) price * timesCount + originalPrice;
				if (timesCount >= 2 && aggressive > 0) {
					/**
					 * 订单优惠后单价
					 */
					price = (price * aggressive / 100);
				}
				/**
				 * 订单优惠总价
				 */
				mFinalTotal = (double) price * timesCount + mFinalTotal;
			}
		}
		lists = new ArrayList<TeacherBean>();
		initTeacherList();
		mAdapter = new AppoinmentPayTeacherListAdapter(getContext());
		mTeacherGallery.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		mTotalPriceTxt
				.setText("￥" + DoubleTool.channelPrice(mFinalTotal / 100));
		vOrderNum.setText("￥" + DoubleTool.channelPrice(mFinalTotal / 100));
		// 提交订单，显示老师和订单时间列表
		if (data != null && data.size() > 0) {
			List<MapBean> list = new ArrayList<MapBean>();
			for (int i = 0, len = data.size(); i < len; i++) {
				list.add(new MapBean(((TimesBean) data.get(i)).getTimeBean()
						.getOrderTime(), ((TimesBean) data.get(i))
						.getTeacherBean().getTeacher_pic_url()));
			}
			mAdapter.setData(list);
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 获取用户代金券
	 * 
	 * @param loginKey
	 * @param page
	 * @param size
	 */
	private void getVoucherData(String loginKey, int page, int size) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.PAGE, page);
		maps.put(DataTag.SIZE, size);
		HttpTool.post(getContext(), Apis.GET_USER_VOUCHE_AVAILABLE,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							CouponMoneyResponse voucherResponse = JSON.decode(
									response, CouponMoneyResponse.class);
							int count = voucherResponse.getCount();
							// VoucherBean beans;
							voucherDataList.clear();
							if (count > 0) {
								List<VoucherBean> list = voucherResponse
										.getList();
								for (VoucherBean bean : list) {
									int status = bean.getStatus();
									if (status == 1) {
										voucherDataList.add(bean);
										int max = 0;
										for (int x = 1; x < voucherDataList
												.size(); x++) {
											if (voucherDataList.get(x)
													.getDenomination() > voucherDataList
													.get(max).getDenomination())
												max = x;
										}
										for (int i = 0; i < voucherDataList
												.size(); i++) {
											if (i == max) {
												voucherDataList.get(i)
														.setIsSelector(true);
											} else {
												voucherDataList.get(i)
														.setIsSelector(false);
											}
										}
										int mChit = voucherDataList.get(max)
												.getDenomination();
										Double mChitDouble = (double) mChit / 100;
										voucherTextView.setText("￥"
												+ DoubleTool
														.channelPrice(mChitDouble));
										voucherLayout
												.setVisibility(View.VISIBLE);
										line.setVisibility(View.VISIBLE);
										voucherTxt.setText("-￥"
												+ DoubleTool
														.channelPrice(mChitDouble));
										Double mFinalPrice = mFinalTotal
												- mChit;
										vOrderNum.setText("￥"
												+ DoubleTool
														.channelPrice(mFinalPrice / 100));
										couponMoneys = (double) mChit;
										couponMoneyIDS = voucherDataList.get(
												max).getId();
										datas = null;
									}
								}
							} else {
								$(R.id.no_message).setVisibility(View.VISIBLE);
							}
							loading.setVisibility(View.GONE);
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

	public static int getMax_2(int[] arr) {
		int max = 0;
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] > arr[max])
				max = x;
		}
		return arr[max];
	}

	@SuppressLint("NewApi")
	private void initTeacherList() {
		int size = data.size();
		int length = 100;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (size * (length + 4) * density);
		int itemWidth = (int) (length * density);
		for (int i = 0; i < size; i++) {
			TeacherBean teacherBean = ((TimesBean) data.get(i))
					.getTeacherBean();
			lists.add(teacherBean);
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
		mTeacherGallery.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		mTeacherGallery.setColumnWidth(itemWidth); // 设置列表项宽
		mTeacherGallery.setHorizontalSpacing(0); // 设置列表项水平间距
		mTeacherGallery.setStretchMode(GridView.NO_STRETCH);
		mTeacherGallery.setNumColumns(size); // 设置列数量=列表集合数
	}

	@Override
	public void initListener() {
		couponMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				if (datas != null && datas.size() > 0) {
					bundle.putSerializable("data", (Serializable) datas);
				} else {
					bundle.putSerializable("data",
							(Serializable) voucherDataList);
				}
				intent.setClass(getContext(), VoucherActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mWechatBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				if (!msgApi.isWXAppInstalled()) {
					ToastTool.toastMessage(getContext(), R.string.no_wechat);
					return;
				}
				if (!msgApi.isWXAppSupportAPI()) {
					ToastTool.toastMessage(getContext(), R.string.no_pay);
					return;
				}
				mPayType = 2;
				mALiPayBtn.setEnabled(true);
				mWechatBtn.setEnabled(false);
			}
		});
		mALiPayBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				mPayType = 1;
				mALiPayBtn.setEnabled(false);
				mWechatBtn.setEnabled(true);
			}
		});
		mOrderOkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				mOrderOkBtn.setClickable(false);
				if (mApplication.isLogin()) {
					loading.setVisibility(View.VISIBLE);
					// 预约提交订单
					if (mAddress == null) {
						ToastTool.toastMessage(getContext(),
								R.string.toast_ordersubmit_address);
						mOrderOkBtn.setClickable(true);
						return;
					}
					if (mBaby == null) {
						ToastTool.toastMessage(getContext(),
								R.string.toast_ordersubmit_baby);
						mOrderOkBtn.setClickable(true);
						return;
					}
					List<TeacherServiceTypeBean> typeList = mTeacherBean
							.getService_type_list();
					List<TeacherServiceBean> serviceList = mTeacherBean
							.getService_list();
					// int couponMoneys = 0;
					// int couponMoneyIDS = 0;
					if (voucherBean != null && voucherBean.getIsSelector()) {
						couponMoneys = (double) voucherBean.getDenomination();
						couponMoneyIDS = voucherBean.getId();
					} else if (voucherBean != null
							&& !voucherBean.getIsSelector()) {
						couponMoneys = 0.0;
						couponMoneyIDS = 0;

					} else {

					}
					OrderSubmitRequest request = new OrderSubmitRequest();
					request.setContact_id(mAddress.getId());
					request.setOrder_contact_name(mAddress.getName());
					request.setOrder_contact_phone(mAddress.getMobile());
					request.setOrder_contact_telphone(mAddress.getTelphone());
					request.setMemo(((TimesBean) data.get(0)).getTimeBean()
							.getMemo().toString().trim());
					request.setUse_score(0);
					request.setUse_coupon_id(couponMoneyIDS);
					request.setPay_type(mPayType);
					request.setOriginal_price((new Double(originalPrice))
							.intValue());// 订单原价
					request.setDiscount_money((new Double(originalPrice))
							.intValue() - (new Double(mFinalTotal)).intValue());
					// 订单折扣金额
					request.setTotal_price(new Double(mFinalTotal
							- couponMoneys).intValue());// 订单最终总价
					request.setScore_money(0);// 积分抵扣金额
					request.setCoupon_money(new Double(couponMoneys).intValue());// 抵用券抵扣金额
					List<OrderSubmitRequestBaby> babys = new ArrayList<OrderSubmitRequestBaby>();
					babys.add(new OrderSubmitRequestBaby(mBaby.getId()));
					request.setBabys(babys);
					List<OrderSubmitRequestInsurances> baby_insurances = new ArrayList<OrderSubmitRequestInsurances>();
					baby_insurances.add(new OrderSubmitRequestInsurances(mBaby
							.getName(), mBaby.getIdentity()));
					request.setBaby_insurances(baby_insurances);
					List<OrderSubmitRequestService> services = new ArrayList<OrderSubmitRequestService>();
					for (int i = 0, len = data.size(); i < len; i++) {
						TimesBean timesBean = (TimesBean) data.get(i);
						services.addAll(getSubmitService(
								timesBean.getTimeBean(),
								timesBean.getTeacherBean()));
					}
					request.setServices(services);
					String orderInfo = getOrderInfo(request);
					String digest = getDigest(mApplication.getLOGIN_KEY(),
							orderInfo);
					loading.setVisibility(View.VISIBLE);
					submitOrder(mApplication.getLOGIN_KEY(), orderInfo, digest,
							request);
				} else {
					getIntentTool().forward(LoginActivity.class);
				}
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

	/**
	 * 获取一个订单的Service信息
	 * 
	 * @param price
	 * @param bean
	 * @param teacherBean
	 * @return
	 */
	private List<OrderSubmitRequestService> getSubmitService(TimeBean bean,
			TeacherBean teacherBean) {
		List<OrderSubmitRequestService> services = new ArrayList<OrderSubmitRequestService>();
		OrderSubmitRequestService service = null;
		String orderTime = bean.getOrderTime();
		String date = orderTime.substring(0, 10);
		String memo = bean.getMemo();
		String startTime = DateChannel.channelStartTime(orderTime);
		String endTime = DateChannel.channelEndTime(orderTime);
		int start = Integer.parseInt(startTime);
		int end = Integer.parseInt(endTime);
		ConfigResponse configResponse = (ConfigResponse) mApplication
				.readObject(DataConst.CONFIG);
		String typeName = this.getResources().getString(
				R.string.teacher_order_course_eachbaby);
		String courseName = this.getResources().getString(
				R.string.teacher_order_course_eachbaby);
		try {
			if (configResponse != null
					&& configResponse.getService_grade_1_list() != null
					&& configResponse.getService_grade_1_list().size() > 0) {
				typeName = configResponse.getService_grade_1_list().get(0)
						.getName();
				courseName = configResponse.getService_grade_1_list().get(0)
						.getService_grade_2_list().get(0).getName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int price = teacherBean.getService_list().get(0).getService_price();
		for (int i = start; i < end; i++) {
			int timeID;
			if (i < 10) {
				timeID = getTimeID(teacherBean, date + "-" + "0" + i + "-00-00");

			} else {
				timeID = getTimeID(teacherBean, date + "-" + i + "-00-00");

			}
			service = new OrderSubmitRequestService(1, 1, timeID, typeName,
					courseName, price, date + " " + i + ":00:00", date + " "
							+ (i + 1) + ":00:00", memo);
			services.add(service);
		}
		return services;
	}

	/**
	 * 获取TimeID
	 * 
	 * @param teacherBean
	 * @param startTime
	 * @return
	 */
	private int getTimeID(TeacherBean teacherBean, String startTime) {
		if (teacherBean != null) {
			List<ServiceTimeBean> serviceTimeLists = teacherBean
					.getService_time_list();
			List<ServiceTimeIdBean> serviceTimeIdLists = teacherBean
					.getService_time_id_list();
			int len = serviceTimeLists.size();
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					ServiceTimeBean bean = serviceTimeLists.get(i);
					String time = bean.getService_time();
					time = time.substring(0, 19);
					if (startTime.equalsIgnoreCase(time)) {
						return serviceTimeIdLists.get(i).getService_time_id();
					}
				}
			}
		}
		return 0;
	}

	private void submitOrder(String login_KEY, String orderInfo, String digest,
			final OrderSubmitRequest request) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_KEY);
		maps.put("order_info", orderInfo);
		maps.put("digest", digest);
		HttpTool.post(getContext(), Apis.BILLING_SUBMIT_ORDER,loading, maps,
				new StringHandler(loading) {
					@Override
					public void success(String response) {
						AppointmentPayRespone bean = JSON.decode(response,
								AppointmentPayRespone.class);
						if (bean.getResult() == 0) {// 订单提交成功
							Intent intent = new Intent(getContext(),
									AppointmentSubmitSuccessActivity.class);
							intent.putExtra("order", bean);
							intent.putExtra("request", request);
							startActivity(intent);
							mOrderOkBtn.setClickable(true);
							AppManager.getAppManager().finishActivity();
						}
						loading.setVisibility(View.GONE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						RequestFailBean failBean = JSON.decode(responseBody,
								RequestFailBean.class);
						if (failBean.getResult() == 1) {
							int type = failBean.getFailue_reason_type();
							ToastTool.toastMessage(getContext(),
									RequestFailBean.getFailType(type));
							mOrderOkBtn.setClickable(true);
						} else if (failBean.getResult() == 5) {
							MyApplication.ShowFailMessage(getContext(),
									responseBody);

						}
						loading.setVisibility(View.GONE);

					}
				});
	}

	@Override
	public void resume() {
		if (voucherBean != null && voucherBean.getIsSelector()) {
			int mChit = voucherBean.getDenomination();
			Double mChitDouble = (double) mChit / 100;
			voucherTextView.setText("￥" + DoubleTool.channelPrice(mChitDouble));
			voucherLayout.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
			voucherTxt.setText("-￥" + DoubleTool.channelPrice(mChitDouble));
			Double mFinalPrice = mFinalTotal - mChit;
			vOrderNum.setText("￥" + DoubleTool.channelPrice(mFinalPrice / 100));
		} else {
			if (voucherBean != null && !voucherBean.getIsSelector()) {
				voucherTextView.setText(R.string.no_user);
				voucherLayout.setVisibility(View.GONE);
				line.setVisibility(View.GONE);
				vOrderNum.setText("￥"
						+ DoubleTool.channelPrice(mFinalTotal / 100));
			} else {
				if (mApplication.isLogin()) {
					loading.setVisibility(View.VISIBLE);
					getVoucherData(mApplication.getLOGIN_KEY(), 1, 100);
				} else {
					loading.setVisibility(View.GONE);
				}
			}
		}
		MobclickAgent.onEvent(getContext(), getString(R.string.b_04));
	}

	@Override
	public void onBackPressed() {
		AppointmentPayActivity.voucherBean = null;
		AppointmentTeacherPayActivity.voucherBean = null;
		super.onBackPressed();
	}

	@Override
	public void destroy() {
		// AppointmentTeacherPayActivity.voucherBean = null;
		// AppointmentPayActivity.voucherBean = null;

	}
}
