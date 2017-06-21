package com.shzy.bjj.activity.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.adapter.InfoListAdapter;
import com.shzy.bjj.bean.OrderBean;
import com.shzy.bjj.bean.OrderDetailCountBean;
import com.shzy.bjj.bean.OrderResponse;
import com.shzy.bjj.bean.TaskDayOrderBean;
import com.shzy.bjj.bean.TeacherDetailBean;
import com.shzy.bjj.bean.TeacherDetailSkillBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.DoubleTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.CircularImage;

/**
 * 
 * 
 * @brief 订单详情
 * @author Fanhao Yi
 * @data 2015年8月18日下午5:06:04
 * @version V1.0
 */
public class OrderDetailActivity extends BaseActivity implements
		OnClickListener, SwipeRefreshLayout.OnRefreshListener {
	// 订单状态
	private TextView order_statusTextView;
	// 订单号
	private TextView order_numberTextView;
	// 订单内容
	private TextView order_nameTextView;
	// 老师
	private CircularImage headImage;
	private TextView teacher_nameTextView;
	private RatingBar ratingBar;
	private TextView teacher_order_totalTextView;
	private TextView teacher_skillTextView;
	// 时间
	private TextView order_dateTextView;
	private TextView order_time_totalTextView;
	// 详情
	private TextView order_baby;
	private TextView order_address;
	private TextView order_phoneEditText;
	private TextView order_more_contactEditText;
	private TextView order_memoEditText;
	// 金额
	private TextView order_all_priceTextView;
	private TextView order_discountTextView;
	private TextView order_payTextView;
	// 下单时间
	private TextView order_user_dateTextView;
	private ImageButton cancelButton;
	// 支付方式
	private LinearLayout weixinLayout;
	private LinearLayout alipayLayout;
	private ImageButton weixin_pay_bt;
	private ImageButton ali_pay_bt;

	private TaskDayOrderBean orderDetailBean;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private int payType;

	// 下拉刷新
	private SwipeRefreshLayout mSwipeLayout;

	public static void startActivity(Context context, TaskDayOrderBean bean) {
		context.startActivity(new Intent(context, OrderDetailActivity.class)
				.putExtra("bean", bean));
	}

	@Override
	public int bindLayout() {
		return R.layout.order_detail;
	}

	@Override
	public void initView(View view) {
		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
		TextView config_hidden = $(R.id.config_hidden);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		config_hidden.requestFocus();
		orderDetailBean = (TaskDayOrderBean) getIntent().getSerializableExtra(
				"bean");
		action_title.setText(R.string.order_detail);
		order_statusTextView = $(R.id.order_status);
		order_numberTextView = $(R.id.order_number);
		order_nameTextView = $(R.id.order_name);
		headImage = $(R.id.head_img);
		teacher_nameTextView = $(R.id.teacher_name);
		ratingBar = $(R.id.ratingbar);
		teacher_order_totalTextView = $(R.id.teacher_order_total);
		teacher_skillTextView = $(R.id.teacher_skill);
		order_dateTextView = $(R.id.order_date);
		order_time_totalTextView = $(R.id.order_time_total);
		order_baby = $(R.id.order_baby);
		order_address = $(R.id.order_address);
		order_phoneEditText = $(R.id.order_phone);
		order_more_contactEditText = $(R.id.order_more_contact);
		order_memoEditText = $(R.id.order_memo);
		order_all_priceTextView = $(R.id.order_all_price);
		order_discountTextView = $(R.id.order_discount);
		order_payTextView = $(R.id.order_pay);
		order_user_dateTextView = $(R.id.order_user_date);
		cancelButton = $(R.id.order_cancel);
		weixinLayout = $(R.id.weixin_pay);
		alipayLayout = $(R.id.ali_pay);
		weixin_pay_bt = $(R.id.weixin_pay_bt);
		ali_pay_bt = $(R.id.ali_pay_bt);

		payType = orderDetailBean.getOrderBean().getPay_type();
		if (payType == 1) {
			// 支付宝
			ali_pay_bt.setEnabled(false);
			weixin_pay_bt.setEnabled(true);
		} else {
			// 微信
			ali_pay_bt.setEnabled(true);
			weixin_pay_bt.setEnabled(false);
		}
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.mine_head_img)
				.showImageOnFail(R.drawable.mine_head_img).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public void initData(Context mContext) {
		if (!HttpTool.checkNetwork(getContext())) {
			loading.setVisibility(View.GONE);
			return;
		}
		if (mApplication.isLogin() && orderDetailBean != null) {
			loading.setVisibility(View.VISIBLE);
			getTeacherDetail(orderDetailBean.getOrderDetailCountBean()
					.getTeacher_id());
		}
		order_statusTextView.setText(TaskDayOrderBean.getOrderDetailStatus(
				orderDetailBean.getOrderBean().getStatus(), orderDetailBean
						.getOrderDetailCountBean().getSchedule_status(),
				orderDetailBean.getOrderDetailCountBean().getIs_comment()));
		order_numberTextView.setText(OrderBean
				.getOrderDetailNumber(orderDetailBean.getOrderDetailCountBean()
						.getOrder_detail_number()));

		int type = orderDetailBean.getOrderDetailCountBean()
				.getService_grade_1_id();
		if (type == 1) {
			order_nameTextView.setText(R.string.teacher_order_course_eachbaby);
		} else {
			order_nameTextView.setText(R.string.teacher_order_course_personal);
		}

		String startTime = orderDetailBean.getOrderDetailCountBean()
				.getService_start_time();
		String endTime = orderDetailBean.getOrderDetailCountBean()
				.getService_end_time();
		String orderTime = OrderDetailCountBean
				.getOrderTime(startTime, endTime);
		order_dateTextView.setText(orderTime);
		int start = Integer.parseInt(startTime.substring(11, 13));
		int end = Integer.parseInt(endTime.substring(11, 13));
		order_time_totalTextView.setText(String.valueOf(end - start) + "小时");
		order_baby.setText(orderDetailBean.getOrderBean().getBaby_name());
		order_address.setText(orderDetailBean.getOrderBean().getAddress()
				+ orderDetailBean.getOrderBean().getAddress_room());
		order_phoneEditText.setText(orderDetailBean.getOrderBean()
				.getOrder_contact_phone());
		order_more_contactEditText.setText(orderDetailBean.getOrderBean()
				.getOrder_contact_telphone());
		order_memoEditText.setText(orderDetailBean.getOrderDetailCountBean()
				.getMemo());
		Double prices = (double) (orderDetailBean.getOrderBean()
				.getOriginal_price() / 100);
		Double discountMoney = (double) (orderDetailBean.getOrderBean()
				.getDiscount_money() / 100);
		Double totalPrices = (double) (orderDetailBean.getOrderBean()
				.getTotal_price() / 100);
		order_all_priceTextView.setText(DoubleTool.channelPrice(prices));
		order_discountTextView.setText(DoubleTool.channelPrice(discountMoney));
		order_payTextView.setText(DoubleTool.channelPrice(totalPrices));
		order_user_dateTextView.setText(InfoListAdapter.getDate(orderDetailBean
				.getOrderBean().getOrder_create_time()));
	}

	@Override
	public void initListener() {
		action_back.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	@Override
	public void resume() {
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
		case R.id.order_cancel:
			OrderCancelDialog.startActivity(this, orderDetailBean);
			break;
		default:
			break;
		}
	}

	/**
	 * 老师详情
	 * 
	 * @param id
	 */
	protected void getTeacherDetail(String id) {
		Map maps = new HashMap<String, String>();
		maps.put("teacher_id", Integer.parseInt(id));

		HttpTool.post(getContext(), Apis.TEACHER_DETAIL,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							TeacherDetailBean teacherDetailBean = JSON.decode(
									response, TeacherDetailBean.class);
							if (teacherDetailBean != null) {
								setTeacherViewData(teacherDetailBean);
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

	private void setTeacherViewData(TeacherDetailBean teacherDetailBean) {
		teacher_nameTextView.setText(teacherDetailBean.getTeacher_name());
		ratingBar.setRating(teacherDetailBean.getTeacher_score() / 10);
		teacher_order_totalTextView.setText(String.valueOf(teacherDetailBean
				.getOrder_count()));
		List<TeacherDetailSkillBean> skillBeans = teacherDetailBean
				.getSkill_ist();
		// 老师技能
		if (skillBeans != null && skillBeans.size() > 0) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0, len = skillBeans.size(); i < len; i++) {
				builder.append(skillBeans.get(i).getDescb());
				if (i < len - 1) {
					builder.append(",");
				}
			}
			teacher_skillTextView.setText(builder.toString().trim());
		}
		String imageUrl = teacherDetailBean.getTeacher_pic_url();
		if (StringTool.isNoBlankAndNoNull(imageUrl)) {
			imageLoader.displayImage(imageUrl, headImage, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
						}
					}, null);
		}
	}

	@Override
	public void onRefresh() {
		getOrderInfo();
	}

	private void getOrderInfo() {
		// String orderID = OrderBean.getOrderDetailNumber(orderDetailBean
		// .getOrderDetailCountBean().getOrder_detail_number());
		String loginKey = mApplication.getLOGIN_KEY();
		Map maps = new HashMap<String, String>();
		maps.put("login_key", loginKey);
		maps.put("order_number", orderDetailBean.getOrderBean()
				.getOrder_number());

		HttpTool.post(getContext(), Apis.ORDERINFOLIST,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							OrderResponse orderResponse = JSON.decode(response,
									OrderResponse.class);
							if (orderResponse != null) {
								int statusid = orderResponse.getList().get(0)
										.getSchedule_status();
								int commentid = orderResponse.getList().get(0)
										.getIs_comment();
								order_statusTextView.setText(TaskDayOrderBean.getOrderDetailStatus(
										orderResponse.getOrder_status(),
										statusid, commentid));
							}
						}
						loading.setVisibility(View.GONE);
						mSwipeLayout.setRefreshing(false);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
						mSwipeLayout.setRefreshing(false);
					}
				});

	}
}
