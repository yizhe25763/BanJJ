package com.shzy.bjj.activity.teacher;

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
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.AppointmentPayActivity;
import com.shzy.bjj.activity.home.AppointmentSubmitSuccessActivity;
import com.shzy.bjj.activity.home.DateChannel;
import com.shzy.bjj.adapter.AppoinmentPayTeacherListAdapter;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.bean.AppointmentPayRespone;
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.bean.ConfigResponse;
import com.shzy.bjj.bean.CouponMoneyResponse;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.bean.OrderSubmitRequest;
import com.shzy.bjj.bean.RequestFailBean;
import com.shzy.bjj.bean.TeacherBean;
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

public class AppointmentTeacherPayActivity extends BaseActivity implements
		OnClickListener {
	protected OrderSubmitRequest t_request;
	protected TeacherBean t_teacherBean;
	protected int t_length;
	protected MapBean t_start;
	protected MapBean t_end;
	protected String t_lens;
	protected BabyBean babyBean;
	protected AppointmentBean addressBean;

	private Button mOrderOkBtn;
	private TextView mAddressTxt;
	private TextView mBabyTxt;
	// 微信支付
	private ImageButton mWechatBtn;
	private int mPayType = 1;
	// 支付宝支付
	private ImageButton mALiPayBtn;

	// 老师列表
	private GridView mTeacherGallery;
	private AppoinmentPayTeacherListAdapter mAdapter;
	private TextView mTotalPriceTxt;
	private List<String> dataList;

	private int pricesTotal;
	private int aggressive;
	private TextView voucherTextView;
	public static VoucherBean voucherBean = null;
	private TableRow coupon_money;
	private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

	private Double mFinalTotal = (double) 0;
	private Double mOriginalPrice = (double) 0;
	/**
	 * 代金券控件
	 */
	private TextView voucherTxt;

	/**
	 * 代金券横线
	 */
	private View line;
	private LinearLayout voucherLayout;
	private TextView vOrderNum;
	/**
	 * 优惠券数据源
	 */
	List<VoucherBean> voucherDataList = new ArrayList<VoucherBean>();
	public static List<VoucherBean> datas = null;

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
		coupon_money = $(R.id.coupon_money);
		vOrderNum = $(R.id.order_num);

		addressBean = (AppointmentBean) getIntent().getSerializableExtra(
				"address");
		babyBean = (BabyBean) getIntent().getSerializableExtra("baby");
		t_request = (OrderSubmitRequest) getIntent().getSerializableExtra(
				"request");
		t_teacherBean = (TeacherBean) getIntent().getSerializableExtra(
				"teacher");
		t_length = getIntent().getIntExtra("length", 0);
		t_start = (MapBean) getIntent().getSerializableExtra("start");
		t_end = (MapBean) getIntent().getSerializableExtra("end");
		t_lens = getIntent().getStringExtra("lens");

		mAddressTxt = $(R.id.address_content);
		mOrderOkBtn = $(R.id.order_ok_btn);
		mBabyTxt = $(R.id.babay_content);
		mTeacherGallery = $(R.id.order_time_gallery);
		mTotalPriceTxt = $(R.id.total_price);
		mWechatBtn = $(R.id.weixin_pay_bt);
		mALiPayBtn = $(R.id.ali_pay_bt);
		voucherTextView = $(R.id.voucher);
		mALiPayBtn.setEnabled(false);
		mWechatBtn.setEnabled(true);

		mAdapter = new AppoinmentPayTeacherListAdapter(getContext());
		mTeacherGallery.setAdapter(mAdapter);
	}

	@Override
	public void initData(Context mContext) {
		msgApi.registerApp(Constants.APP_ID);
		mOrderOkBtn.setClickable(true);
		action_title.setText("订单确认");
		dataList = getIntent().getStringArrayListExtra("datalist");
		mAddressTxt.setText(addressBean.getAddress()+addressBean.getAddress_room());
		mBabyTxt.setText(babyBean.getName());
		// 订单原价总金额
		int originalPrice = t_request.getOriginal_price();
		// 订单单价
		int unitPrice = originalPrice / t_length;
		//
		String times = null;
		String mStartTime = null;
		String mEndTime = null;
		int mStartTimes;
		int mEndTimes;
		int timesCount;
		int size = dataList.size();
		ConfigResponse response = (ConfigResponse) mApplication
				.readObject(DataConst.CONFIG);
		if (response != null) {
			if (response.getDiscount_list().get(0).getType() == 2) {
				aggressive = response.getDiscount_list().get(0).getAggressive();
			}
		} else {

		}
		for (int i = 0; i < size; i++) {
			times = dataList.get(i);
			mStartTime = DateChannel.channelStartTime(times);
			mEndTime = DateChannel.channelEndTime(times);
			mStartTimes = Integer.valueOf(mStartTime);
			mEndTimes = Integer.valueOf(mEndTime);
			timesCount = mEndTimes - mStartTimes;

			Double price = (double) unitPrice;
			// 订单原始总价
			mOriginalPrice = (double) originalPrice + mOriginalPrice;
			if (DataConst.aggressive == 0) {
				ToastTool.toastMessage(getContext(), "网络不畅通，请检查网络设置");
				getConfig();
				return;
			}
			if (timesCount >= 2 && DataConst.aggressive > 0) {
				/**
				 * 订单优惠后单价
				 */
				price = (price * DataConst.aggressive / 100);
			}
			/**
			 * 订单优惠总价
			 */
			mFinalTotal = (double) price * timesCount + mFinalTotal;
		}

		mTotalPriceTxt
				.setText("￥" + DoubleTool.channelPrice(mFinalTotal / 100));
		vOrderNum.setText("￥" + DoubleTool.channelPrice(mFinalTotal / 100));
		t_request.setOriginal_price(originalPrice);
		t_request.setDiscount_money(originalPrice
				- (new Double(mFinalTotal)).intValue());

		// 提交订单，显示老师和订单时间列表
		if (StringTool.isNoBlankAndNoNull(t_lens)) {
			List<MapBean> list = new ArrayList<MapBean>();
			String[] array = t_lens.split(",");
			int len = array.length;
			for (int i = 0; i < len; i++) {
				list.add(new MapBean(array[i], t_teacherBean
						.getTeacher_pic_url()));
			}
			mAdapter.setData(list);
			mAdapter.notifyDataSetChanged();
			initTeacherList(len);
		}
	}

	@SuppressLint("NewApi")
	private void initTeacherList(int len) {
		int length = 100;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (len * (length + 4) * density);
		int itemWidth = (int) (length * density);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
		mTeacherGallery.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		mTeacherGallery.setColumnWidth(itemWidth); // 设置列表项宽
		mTeacherGallery.setHorizontalSpacing(0); // 设置列表项水平间距
		mTeacherGallery.setStretchMode(GridView.NO_STRETCH);
		mTeacherGallery.setNumColumns(len); // 设置列数量=列表集合数
	}

	@Override
	public void initListener() {
		mWechatBtn.setOnClickListener(this);
		mALiPayBtn.setOnClickListener(this);
		action_back.setOnClickListener(this);
		mOrderOkBtn.setOnClickListener(this);
		coupon_money.setOnClickListener(new OnClickListener() {

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
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.weixin_pay_bt:
			if (!msgApi.isWXAppInstalled()) {
				ToastTool.toastMessage(getContext(), "未安装微信，请选择其他支付方式");
				return;
			}
			if (!msgApi.isWXAppSupportAPI()) {
				ToastTool.toastMessage(getContext(), "当前版本不支持支付功能");
				return;
			}
			mPayType = 2;
			mALiPayBtn.setEnabled(true);
			mWechatBtn.setEnabled(false);
			break;
		case R.id.ali_pay_bt:
			mPayType = 1;
			mALiPayBtn.setEnabled(false);
			mWechatBtn.setEnabled(true);
			break;
		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.order_ok_btn:
			if (ButtonTool.isFastClick()) {
				return;
			}
			mOrderOkBtn.setClickable(false);
			loading.setVisibility(View.VISIBLE);
			t_request.setPay_type(mPayType);
			if (voucherBean != null && voucherBean.getIsSelector()) {
				couponMoneys = (double) voucherBean.getDenomination();
				couponMoneyIDS = voucherBean.getId();
			} else if (voucherBean != null && !voucherBean.getIsSelector()) {
				couponMoneys = 0.0;
				couponMoneyIDS = 0;
			}
			t_request.setUse_coupon_id(couponMoneyIDS);
			t_request.setCoupon_money(new Double(couponMoneys).intValue());
			t_request.setTotal_price(new Double(mFinalTotal - couponMoneys)
					.intValue());
			String orderInfo = getOrderInfo(t_request);
			String digest = getDigest(mApplication.getLOGIN_KEY(), orderInfo);
			submitOrder(mApplication.getLOGIN_KEY(), orderInfo, digest,
					t_request);
			break;
		default:
			break;
		}
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
						loading.setVisibility(View.GONE);
						AppointmentPayRespone bean = JSON.decode(response,
								AppointmentPayRespone.class);
						if (bean.getResult() == 0) {// 订单提交成功
							MobclickAgent.onEvent(getContext(), "C0006");
							Intent intent = new Intent(getContext(),
									AppointmentSubmitSuccessActivity.class);
							intent.putExtra("order", bean);
							intent.putExtra("request", request);
							startActivity(intent);
							mOrderOkBtn.setClickable(true);
							AppManager.getAppManager().finishActivity();
							AppManager.getAppManager().finishActivity(
									ChooseTeacherActivity.class);
						}

					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						loading.setVisibility(View.GONE);
						RequestFailBean failBean = JSON.decode(responseBody,
								RequestFailBean.class);
						mOrderOkBtn.setClickable(true);
						int type = failBean.getFailue_reason_type();
						ToastTool.toastMessage(getContext(),
								RequestFailBean.getFailType(type));
					}
				});
	}

	@Override
	public void resume() {

		// if (voucherBean != null) {
		// int mChit = voucherBean.getDenomination();
		// Double mChitDouble = (double) mChit / 100;
		// voucherTextView.setText("￥" + mChitDouble);
		// voucherLayout.setVisibility(View.VISIBLE);
		// line.setVisibility(View.VISIBLE);
		// voucherTxt.setText("-￥" + mChitDouble);
		// Double mFinalPrice = mFinalTotal - mChit;
		// vOrderNum.setText("￥" + mFinalPrice / 100);
		// } else {
		// voucherTextView.setText("未使用");
		// }

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
		MobclickAgent.onEvent(getContext(), "C0004");

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
							VoucherBean beans;
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

	@Override
	public void onBackPressed() {
		AppointmentPayActivity.voucherBean = null;
		AppointmentTeacherPayActivity.voucherBean = null;
		AppManager.getAppManager().finishActivity();
	}

	@Override
	public void destroy() {
		// AppointmentPayActivity.voucherBean = null;
		// AppointmentTeacherPayActivity.voucherBean=null;
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
