package com.shzy.bjj.activity.home;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.adapter.AppointmentListAdapter;
import com.shzy.bjj.adapter.AppointmentListTimeAdapter;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.bean.ConfigResponse;
import com.shzy.bjj.bean.TeacherAppointmentRequest;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.bean.TeacherResponse;
import com.shzy.bjj.bean.TimeBean;
import com.shzy.bjj.bean.TimesBean;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.DoubleTool;
import com.shzy.bjj.tools.ToastTool;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @brief 预约列表
 * @author Fanhao Yi
 * @data 2015年6月29日下午4:18:15
 * @version V1.0
 */
public class AppointmentListActivity extends BaseActivity {
	/**
	 * 订单数据对象
	 */
	private TimesBean beans = null;
	/**
	 * 是否刷新
	 */
	public static boolean isRefresh = false;
	/**
	 * 提交订单按钮
	 */
	private Button mOrderOkBtn;
	/**
	 * 费用总计
	 */
	private TextView mOrderNum;
	/**
	 * 
	 */
	private RelativeLayout mOrderTimeLayout;
	/**
	 * 时间画廊控件
	 */
	private GridView mOrderTimeGallery;
	/**
	 * 地址控件
	 */
	private TextView mAddressTxt;
	/**
	 * 宝宝控件
	 */
	private TextView mBabyTxt;
	/**
	 * false 不显示时间 true显示时间
	 */
	private Boolean mIsShow = false;
	/**
	 * 时间列表按钮
	 */
	private ImageView mOrderTimeImg;
	/**
	 * 顶部时间适配器
	 */
	private AppointmentListTimeAdapter mAdapter;
	/**
	 * 教师列表
	 */
	private ListView mTimeList;
	/**
	 * 地址栏
	 */
	private TableRow mAddressTab;
	/**
	 * 宝宝栏
	 */
	private TableRow mBabyTab;
	/**
	 * 城市
	 */
	private String mCity;
	/**
	 * 
	 */
	private TeacherAppointmentRequest request;
	/**
	 * 
	 */
	private TeacherResponse response;
	/**
	 * true:自动匹配，false：手动选择
	 */
	private Boolean flag;
	/**
	 * 教师详情适配器
	 */
	private AppointmentListAdapter mAppointmentAdapter;
	/**
	 * 订单数据
	 */
	public static List<TimesBean> data = null;
	/**
	 * 订单总价
	 */
	private Double mTotal;

	private Double originalPrice = (double) 0;
	private int aggressive;
	private Double mFinalTotal = (double) 0;

	public static void startActivity(Context context,
			TeacherAppointmentRequest request, boolean flag,
			TeacherResponse response) {
		context.startActivity(new Intent(context, AppointmentListActivity.class)
				.putExtra("response", response).putExtra("flag", flag)
				.putExtra("request", request));
	}

	@Override
	public int bindLayout() {
		return R.layout.activity_appointment_list;
	}

	@Override
	public void initView(View view) {
		data = new ArrayList<TimesBean>();
		action_title.setText("预约列表");
		mAddressTab = $(R.id.order_address);
		mBabyTab = $(R.id.order_baby);
		mOrderOkBtn = $(R.id.order_ok_btn);
		mOrderNum = $(R.id.order_num);
		mOrderTimeLayout = $(R.id.order_time_real);
		mOrderTimeGallery = $(R.id.order_time_gallery);
		mAddressTxt = $(R.id.address_content);
		mBabyTxt = $(R.id.baby_txt);
		mOrderTimeImg = $(R.id.order_time_img);
		mTimeList = $(R.id.order_list);
		mAppointmentAdapter = new AppointmentListAdapter(getContext(),
				new Refresh() {

					@Override
					public void onComplent(int position, String memo) {
						// 当前获取到每条数据对应的备注信息
						ToastTool.toastMessage(getContext(), "position:"
								+ position + "memo:" + memo);
						data.get(position).getTimeBean().setMemo(memo);
						mAppointmentAdapter.setData(data);
						mAppointmentAdapter.notifyDataSetChanged();
					}
				});

		mTimeList.setDivider(null);
		mTimeList.setAdapter(mAppointmentAdapter);
	}

	@SuppressWarnings("null")
	@Override
	public void initData(Context mContext) {

		request = (TeacherAppointmentRequest) getIntent().getSerializableExtra(
				"request");

		flag = getIntent().getBooleanExtra("flag", false);
		if (flag == true)
			response = (TeacherResponse) getIntent().getSerializableExtra(
					"response");
		List<TimeBean> requestList = new ArrayList<TimeBean>();
		List<String> dataList = request.getDataList();
		AppointmentBean addressBean = request.getAddress();
		String memo = request.getMemo();
		BabyBean babyBean = request.getBaby();
		String time = request.getTime();
		int count = request.getCount();
		mAddressTxt.setText(addressBean.getAddress()+addressBean.getAddress_room());
		mBabyTxt.setText(babyBean.getName());
		List<TeacherBean> teacherList = null;
		if (response != null) {
			teacherList = response.getList();
		}
		TimesBean timesBean = null;
		// 自动匹配
		if (flag) {
			for (int i = 0, len = teacherList.size(); i < len; i++) {
				timesBean = new TimesBean();
				timesBean.setTeacherBean(teacherList.get(i));
				String times = teacherList.get(i)
						.getAuto_match_condition_times().toString().trim();
				String channelTime = "";
				int lastTime = times.lastIndexOf("-");
				if (times.contains("AND")) {
					channelTime = times.substring(0, 21)
							+ times.substring(lastTime - 15, lastTime + 3);
				} else {
					channelTime = times;
				}
				timesBean.setTimeBean(new TimeBean(channelTime, addressBean,
						memo, babyBean, time, 0));
				data.add(timesBean);
			}
			mAppointmentAdapter.setAuto(true);
		} else {
			// 手动选择
			for (String str : dataList) {
				requestList.add(new TimeBean(str, addressBean, memo, babyBean,
						time, count));
			}
			for (TimeBean bean : requestList) {
				timesBean = new TimesBean();
				timesBean.setTimeBean(bean);
				timesBean.setTeacherBean(new TeacherBean());
				data.add(timesBean);
			}
			mAppointmentAdapter.setAuto(false);
		}
		mAppointmentAdapter.setData(data);
		mAppointmentAdapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @param isShow
	 *            false 隐藏时间 true 显示时间
	 */
	@SuppressLint("NewApi")
	private void showTime(Boolean isShow) {
		if (isShow) {
			// 隐藏时间
			mAddressTab.setVisibility(View.VISIBLE);
			mBabyTab.setVisibility(View.VISIBLE);
			mOrderTimeGallery.setVisibility(View.GONE);
			mOrderTimeImg.setBackgroundResource(R.drawable.order_time_up);
			mIsShow = false;
		} else {
			// 显示时间
			mAddressTab.setVisibility(View.GONE);
			mBabyTab.setVisibility(View.GONE);
			mOrderTimeGallery.setVisibility(View.VISIBLE);
			int size = request.getDataList().size();
			int length = 100;
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			float density = dm.density;
			int gridviewWidth = (int) (size * (length + 4) * density);
			int itemWidth = (int) (length * density);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
			mOrderTimeGallery.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
			mOrderTimeGallery.setColumnWidth(itemWidth); // 设置列表项宽
			mOrderTimeGallery.setHorizontalSpacing(5); // 设置列表项水平间距
			mOrderTimeGallery.setStretchMode(GridView.NO_STRETCH);
			mOrderTimeGallery.setNumColumns(size); // 设置列数量=列表集合数
			mOrderTimeImg.setBackgroundResource(R.drawable.order_time_down);
			mAdapter = new AppointmentListTimeAdapter(getContext(),
					request.getDataList());
			mOrderTimeGallery.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
			mIsShow = true;
		}
	}

	public boolean checkData() {
		int size = data.size();
		boolean flag = true;
		for (int i = 0; i < size; i++) {
			if (data.get(i).getTeacherBean().getTeacher_id() <= 0) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	@Override
	public void initListener() {
		/**
		 * 提交订单按钮
		 */
		mOrderOkBtn.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {

				if (checkData()) {
					if (ButtonTool.isFastClick()) {
						return;
					}
					Intent intent = new Intent();
					intent.setClass(getContext(), AppointmentPayActivity.class);
					intent.putParcelableArrayListExtra("data",
							(ArrayList<? extends Parcelable>) data);
					startActivity(intent);
				} else {
					ToastTool.toastMessage(getContext(), "请选择老师");
				}
			}

		});
		mOrderTimeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				showTime(mIsShow);
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

	Double total = (double) 0;

	@Override
	public void resume() {
		total = (double) 0;
		MobclickAgent.onEvent(getContext(), "B0003");

		for (int i = 0, len = data.size(); i < len; i++) {
			TeacherBean bean = data.get(i).getTeacherBean();
			TimeBean timeBean = data.get(i).getTimeBean();
			String times = timeBean.getOrderTime();
			String mStartTime = DateChannel.channelStartTime(times);
			String mEndTime = DateChannel.channelEndTime(times);
			int mStartTimes = Integer.valueOf(mStartTime);
			int mEndTimes = Integer.valueOf(mEndTime);
			int timesCount = mEndTimes - mStartTimes;
			String timeCount = String.valueOf(timesCount);
			if (flag) {
				if (bean.getService_list().get(0).getService_id() == 1) {
					int price = bean.getService_list().get(0)
							.getService_price();
					if (DataConst.aggressive == 0) {
						ToastTool.toastMessage(getContext(), "网络不畅通，请检查网络设置");
						return;
					}
					if (timesCount >= 2 && DataConst.aggressive > 0) {
						price = (int) (price * DataConst.aggressive / 100);
					}
					total = (double) price * timesCount + total;
				}
				// mOrderNum.setText("￥" + String.valueOf(total / 100));
				mOrderNum.setText("￥" + DoubleTool.channelPrice(total / 100));

			} else {

			}
		}

		if (isRefresh && mAppointmentAdapter != null) {
			mAppointmentAdapter.setData(data);
			mAppointmentAdapter.notifyDataSetChanged();
			isRefresh = false;
		}
	}

	@Override
	public void destroy() {

	}

	public interface Refresh {
		public void onComplent(int position, String memo);

	}

}
