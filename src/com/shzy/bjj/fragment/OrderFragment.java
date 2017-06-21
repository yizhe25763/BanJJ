package com.shzy.bjj.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.CommonPayActivity;
import com.shzy.bjj.activity.home.AppointmentTimeActivity;
import com.shzy.bjj.activity.order.CancelOrderDialog;
import com.shzy.bjj.activity.order.ComplainOrderActivity;
import com.shzy.bjj.activity.order.EvaluateActivity;
import com.shzy.bjj.activity.order.OrderCancelDialog;
import com.shzy.bjj.activity.order.OrderCommentDialog;
import com.shzy.bjj.activity.order.OrderDetailActivity;
import com.shzy.bjj.adapter.OrderListAdapter;
import com.shzy.bjj.bean.OrderBean;
import com.shzy.bjj.bean.OrderDetailCountBean;
import com.shzy.bjj.bean.OrderResponse;
import com.shzy.bjj.bean.TaskDayBean;
import com.shzy.bjj.bean.TaskDayOrderBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.fragment.base.BaseFragment;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.DateTimeTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.XListView;
import com.shzy.bjj.view.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @brief 订单
 * @author Fanhao.Yi
 * @date 2015年5月5日下午4:28:29
 * @version V1.0
 */
@SuppressLint("ResourceAsColor")
public class OrderFragment extends BaseFragment implements OnClickListener,
		ViewPager.OnPageChangeListener, IXListViewListener {
	private int currentIndex = 0;
	private int tabWidth;
	private ViewPager mViewPager;
	private ImageView mIvTabIndicator;
	private List<View> pagerList;
	private View pageOne;
	private View vNoMessagepageOne;
	private View vNoMessagepageTwo;
	private View pageTwo;
	private LayoutInflater inflater;

	// 进行中
	private RelativeLayout loadingLayout;
	private TextView leftTextView;
	private TextView loading_messageTextView;
	private XListView listView_one;
	private OrderListAdapter LoadingAdapter;

	// 已完成
	private RelativeLayout finishedLayout;
	private TextView rightTextView;
	private TextView finished_messageTextView;
	private XListView listView_two;
	private OrderListAdapter finishedAdapter;

	private int mIsMessage = 0; // 0都无数据 1有最新无完成 2无最新有完成 3都存在数据

	private TextView toGoOrder;
	private TextView toGoOrders;

	// private XListView mListView;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;

	@Override
	public int bindLayout() {
		return R.layout.fragment_order;
	}

	@Override
	public void initView(View view) {
		action_back.setVisibility(View.INVISIBLE);
		action_title.setText(R.string.order_title_content);

		loadingLayout = $(R.id.order_loading);
		leftTextView = $(R.id.order_user_loading);
		loading_messageTextView = $(R.id.order_loading_count);

		finishedLayout = $(R.id.order_finished);
		rightTextView = $(R.id.order_user_finished);
		finished_messageTextView = $(R.id.order_finished_count);

		mIvTabIndicator = $(R.id.iv_indicator);
		mViewPager = $(R.id.viewPager);
		mViewPager.setOnPageChangeListener(this);

		pagerList = new ArrayList<View>();
		inflater = LayoutInflater.from(getContext());
		pageOne = inflater.inflate(R.layout.orderlistview, null);
		pageTwo = inflater.inflate(R.layout.orderlistview, null);
		vNoMessagepageOne = inflater.inflate(R.layout.no_meaage, null);
		vNoMessagepageTwo = inflater.inflate(R.layout.no_meaage, null);
		listView_two = (XListView) pageTwo.findViewById(R.id.listview);
		toGoOrder = (TextView) vNoMessagepageOne
				.findViewById(R.id.no_message_contents);
		toGoOrders = (TextView) vNoMessagepageTwo
				.findViewById(R.id.no_message_contents);
		listView_one = (XListView) pageOne.findViewById(R.id.listview);
		listView_one.setPullLoadEnable(true);
		listView_one.setXListViewListener((IXListViewListener) this);
		listView_two.setPullLoadEnable(true);
		listView_two.setXListViewListener((IXListViewListener) this);
		// toGoOrder = (TextView) listView_two
		// .findViewById(R.id.no_message_contents);
	}

	private void onLoad() {
		listView_one.stopRefresh();
		listView_one.stopLoadMore();
		listView_one
				.setRefreshTime(DateTimeTool.formatFriendly(DateTimeTool
						.parseDate(DateTimeTool
								.gainCurrentDate("yyyy-MM-dd HH:mm:ss"))));

		listView_two.stopRefresh();
		listView_two.stopLoadMore();
		listView_two
				.setRefreshTime(DateTimeTool.formatFriendly(DateTimeTool
						.parseDate(DateTimeTool
								.gainCurrentDate("yyyy-MM-dd HH:mm:ss"))));
	}

	int nums = 0;

	@Override
	public void onRefresh() {
		if (HttpTool.checkNetwork(getActivity())) {
			nums = 1;
			initData(getContext());
//			loading.setVisibility(View.GONE);
		} else {
			listView_two.stopRefresh();
			listView_one.stopRefresh();

		}

	}

	@Override
	public void onLoadMore() {
	}

	
	
	@Override
	public void onResume() {
		initData(getContext());
		MobclickAgent.onEvent(getContext(), "A0009");
		super.onResume();
	}

	@Override
	public void initData(Context mContext) {
		if (!mApplication.isLogin()) {
			mIsMessage = 0;
			pagerList.clear();
			pagerList.add(vNoMessagepageOne);
			pagerList.add(vNoMessagepageTwo);
			mViewPager.setAdapter(new ViewPagerAdapter(pagerList));
			mViewPager.setCurrentItem(currentIndex);
			setTabWidth();
			toGoOrders.setText("还没有订单哟，要不要约一单？");
			toGoOrder.setText("还没有订单哟，要不要约一单？");
			toGoOrder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getIntentTool().forward(AppointmentTimeActivity.class);
				}
			});
			toGoOrders.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getIntentTool().forward(AppointmentTimeActivity.class);
				}
			});

		}

		LoadingAdapter = new OrderListAdapter(mContext, new OrderOperation() {

			@Override
			public void onClick(int tag, TaskDayOrderBean bean) {
				OnWidgetClick(tag, bean);
			}

			@Override
			public void onClick(int tag, OrderBean bean) {
				if (tag == 0) {
					// 去支付
					Intent intent = new Intent(getContext(),
							CommonPayActivity.class);
					intent.putExtra("bean", bean);
					getContext().startActivity(intent);
				} else if (tag == 1) {
					// 取消订单
					if (mApplication.isLogin() && bean != null) {
						CancelOrderDialog.startActivity(getContext(), bean);
					}
				}
			}

		});
		finishedAdapter = new OrderListAdapter(mContext, new OrderOperation() {

			@Override
			public void onClick(int tag, TaskDayOrderBean bean) {
				OnWidgetClick(tag, bean);
			}

			@Override
			public void onClick(int tag, OrderBean bean) {
			}

		});
		listView_one.setAdapter(LoadingAdapter);
		listView_two.setAdapter(finishedAdapter);
		if (!HttpTool.checkNetwork(getActivity())) {
			// loading.setVisibility(View.GONE);
			return;
		}
		if (mApplication.isLogin()) {
			if (nums == 1) {
				loading.setVisibility(View.GONE);

			} else {
				loading.setVisibility(View.VISIBLE);

			}
			// 请求数据
//			loading.setVisibility(View.VISIBLE);
			getOrderData(mApplication.getLOGIN_KEY(), 1, 100);
		} else {
			// loading.setVisibility(View.GONE);
		}

	}

	private void confirmOrder(String loginKey, final TaskDayOrderBean bean,
			String number, String orderDetailNum) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.ORDERNUMBER, number);
		maps.put(DataTag.ORDERDETAILNUMBER, orderDetailNum);
		HttpTool.post(getActivity(), Apis.CONFIRMORDER, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							ToastTool.toastMessage(getContext(), "获得50积分");
							OrderCommentDialog.startActivity(getActivity(),
									bean);
//							onResume();
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
	 * 子订单各按钮点击事件处理
	 * 
	 * @param tag
	 * @param bean
	 */
	private void OnWidgetClick(int tag, TaskDayOrderBean bean) {
		switch (tag) {
		case 0:
			// 订单详情
			OrderDetailActivity.startActivity(getContext(), bean);
			break;
		case 1:
			// 确定
			if (mApplication.isLogin()
					&& StringTool.isNoBlankAndNoNull(bean.getOrderBean()
							.getOrder_number())
					&& StringTool
							.isNoBlankAndNoNull(bean.getOrderDetailCountBean()
									.getOrder_detail_number())) {
				confirmOrder(mApplication.getLOGIN_KEY(), bean, bean
						.getOrderBean().getOrder_number(), bean
						.getOrderDetailCountBean().getOrder_detail_number());
			}
			break;
		case 2:
			// 取消
			OrderCancelDialog.startActivity(getContext(), bean);

			break;
		case 3:
			// 评论
			EvaluateActivity.startActivity(getContext(), bean);
			break;
		case 4:
			if (mApplication.isLogin()
					&& StringTool.isNoBlankAndNoNull(bean.getOrderBean()
							.getOrder_number())
					&& StringTool
							.isNoBlankAndNoNull(bean.getOrderDetailCountBean()
									.getOrder_detail_number())) {
				ComplainOrderActivity.startActivity(getActivity(), bean);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void initListener() {
		loadingLayout.setOnClickListener(this);
		finishedLayout.setOnClickListener(this);
	}

	/**
	 * 根据屏幕的宽度，初始化Tab指示器的宽度
	 */
	private void setTabWidth() {
		DisplayMetrics outMetrics = new DisplayMetrics();
		getContext().getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(outMetrics);
		tabWidth = outMetrics.widthPixels / 4;
		setTabIndicatorLeftMargin();
	}

	/**
	 * 设置Tab指示器左边的偏移量
	 */
	private void setTabIndicatorLeftMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIvTabIndicator
				.getLayoutParams();
		lp.width = tabWidth;
		if (currentIndex == 0) {
			lp.leftMargin = tabWidth / 2;
		} else {
			lp.leftMargin = tabWidth / 2 + 2 * tabWidth;
		}
		mIvTabIndicator.setLayoutParams(lp);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.order_loading:
			leftTextView.setTextColor(R.color.tab_selected);
			rightTextView.setTextColor(R.color.tab_normal);
			currentIndex = 0;
			setTabIndicatorLeftMargin();
			mViewPager.setCurrentItem(currentIndex);
			break;
		case R.id.order_finished:
			leftTextView.setTextColor(R.color.tab_normal);
			rightTextView.setTextColor(R.color.tab_selected);
			currentIndex = 1;
			setTabIndicatorLeftMargin();
			mViewPager.setCurrentItem(currentIndex);
			break;

		default:
			break;
		}
	}

	private void getOrderData(String loginKey, int page, int size) {
		if (!HttpTool.checkNetwork(getActivity())) {
			// loading.setVisibility(View.GONE);
			return;
		}
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.PAGE, 1);
		maps.put(DataTag.SIZE, 1000);
		HttpTool.post(getActivity(), Apis.GET_USER_ORDER, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							OrderResponse orderResponse = JSON.decode(response,
									OrderResponse.class);
							int count = orderResponse.getCount();
							if (count > 0) {
								markOrderData(orderResponse);
							} else {
								mIsMessage = 0;
								markOrderData(orderResponse);
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

	private void markOrderData(OrderResponse orderResponse) {
		List<OrderBean> list = orderResponse.getList();
		List<TaskDayBean> aData = new ArrayList<TaskDayBean>();
		List<TaskDayBean> dData = new ArrayList<TaskDayBean>();
		for (OrderBean bean : list) {
			int status = bean.getStatus();
			if (getStatus(status)) {
				// 已完成
				dData.add(new TaskDayBean(getDataList(bean), bean));
			} else {
				// 未完成
				aData.add(new TaskDayBean(getDataList(bean), bean));
			}
		}
		if (dData.size() > 0 && aData.size() > 0) {
			mIsMessage = 3;
			pagerList.clear();
			pagerList.add(pageOne);
			pagerList.add(pageTwo);
		} else {
			if (dData.size() > 0) {
				mIsMessage = 2;
				pagerList.clear();
				pagerList.add(vNoMessagepageOne);
				pagerList.add(pageTwo);
			} else if (aData.size() > 0) {
				mIsMessage = 1;
				pagerList.clear();

				pagerList.add(pageOne);
				pagerList.add(vNoMessagepageTwo);
			} else {
				mIsMessage = 0;
				pagerList.clear();
				pagerList.add(vNoMessagepageOne);
				pagerList.add(vNoMessagepageTwo);
			}
		}
		if (mIsMessage == 0 || mIsMessage == 2) {
			toGoOrder.setText("还没有订单哟，要不要约一单？");
		}
		mViewPager.setAdapter(new ViewPagerAdapter(pagerList));
		mViewPager.setCurrentItem(currentIndex);
		setTabWidth();
		finishedAdapter.setData(dData);
		finishedAdapter.notifyDataSetChanged();
		LoadingAdapter.setData(aData);
		LoadingAdapter.notifyDataSetChanged();
		onLoad();
		toGoOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mApplication.isLogin()) {
					if (mIsMessage == 0 || mIsMessage == 2) {
						getIntentTool().forward(AppointmentTimeActivity.class);
					}
				}
			}
		});
	}

	private boolean getStatus(int status) {
		// 已完成订单
		if (status == 108 || status == 109 || status == 320) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {

	}

	@Override
	public void onPageSelected(int i) {
		currentIndex = i;
		setTabIndicatorLeftMargin();
		mViewPager.setCurrentItem(currentIndex);
	}

	@Override
	public void onPageScrollStateChanged(int i) {
	}

	public class ViewPagerAdapter extends PagerAdapter {
		private List<View> listView;

		public ViewPagerAdapter() {
		}

		public ViewPagerAdapter(List<View> listViews) {
			this.listView = listViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(listView.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(listView.get(position), 0);
			return listView.get(position);
		}

		@Override
		public int getCount() {
			return listView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	private List<TaskDayOrderBean> getDataList(OrderBean orderBean) {
		List<TaskDayOrderBean> list = new ArrayList<TaskDayOrderBean>();
		String start = "";
		String end = "";
		int price = 0;
		StringBuilder builder = null;

		int count = orderBean.getDetail_count();
		List<OrderDetailCountBean> tList = null;
		int length = 0;
		if (count > 0) {
			// 子订单列表
			tList = orderBean.getDetail_list();
			length = tList.size();
			builder = new StringBuilder();

			// 至少有一个子订单
			OrderDetailCountBean orderDetailBean = orderBean.getDetail_list()
					.get(0);
			start = orderDetailBean.getService_start_time();
			// 价格
			price = orderDetailBean.getService_unit_price();
			builder.append(orderDetailBean.getOrder_detail_number());
			builder.append(",");

			// 循环判断子订单 是否连续
			end = orderDetailBean.getService_end_time();
			if (length > 1) {
				for (int j = 1; j < length; j++) {
					OrderDetailCountBean bean = orderBean.getDetail_list().get(
							j);
					// 如果连续
					if (end.equalsIgnoreCase(bean.getService_start_time())) {
						builder.append(bean.getOrder_detail_number());
						builder.append(",");
						end = bean.getService_end_time();
						price += bean.getService_unit_price();

						// 如果不连续
					} else {
						list.add(getTaskOrderBean(orderBean, orderDetailBean,
								start, end, builder.toString().trim(), price));
						// 开始下个订单匹配
						orderDetailBean = bean;
						builder = new StringBuilder(
								bean.getOrder_detail_number());
						builder.append(",");
						start = bean.getService_start_time();
						price = bean.getService_unit_price();
						end = bean.getService_end_time();
					}
					// 如果到订单最后一个子订单，结束本订单匹配
					if (j == length - 1) {
						list.add(getTaskOrderBean(orderBean, orderDetailBean,
								start, end, builder.toString().trim(), price));
					}
				}
			} else if (length > 0) {
				list.add(getTaskOrderBean(orderBean, orderDetailBean, start,
						end, builder.toString().trim(), price));
			}
			// 一个大订单循环结束
		}
		return list;
	}

	/**
	 * 一个连续订单
	 * 
	 * @param orderBean
	 * @param orderDetailCountBean
	 * @param statr
	 * @param end
	 * @param number
	 * @return
	 */
	private TaskDayOrderBean getTaskOrderBean(OrderBean orderBean,
			OrderDetailCountBean orderDetailCountBean, String statr,
			String end, String number, int price) {
		number = number.substring(0, number.length() - 1);
		orderDetailCountBean.setService_start_time(statr);
		orderDetailCountBean.setService_end_time(end);
		orderDetailCountBean.setOrder_detail_number(number);
		orderDetailCountBean.setService_unit_price(price);
		return new TaskDayOrderBean(orderBean, orderDetailCountBean);
	}

	public interface OrderOperation {
		public void onClick(int tag, TaskDayOrderBean bean);

		public void onClick(int tag, OrderBean bean);
	}

}
