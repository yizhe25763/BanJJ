package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.home.AppointmentListActivity.Refresh;
import com.shzy.bjj.activity.home.DateChannel;
import com.shzy.bjj.activity.home.TeacherResourcesActivity;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.bean.TeacherSkillBean;
import com.shzy.bjj.bean.TimeBean;
import com.shzy.bjj.bean.TimesBean;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.tools.DoubleTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.AsyncImageLoaderLocal;

/**
 * 
 * @brief 预约列表适配器
 * @author Fanhao Yi
 * @data 2015年6月30日下午4:34:22
 * @version V1.0
 */
public class AppointmentListAdapter extends BaseAdapter {
	private Context mContext;
	private Refresh refresh;
	private List<TimesBean> data = new ArrayList<TimesBean>();
	private DisplayImageOptions options;
	private AsyncImageLoaderLocal asyncLoad = null;
	// 是否自动匹配
	private boolean isAuto = false;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private Integer index = -1;

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public List<TimesBean> getData() {
		return data;
	}

	public void setData(List<TimesBean> data) {
		this.data = data;
	}

	public AppointmentListAdapter(Context context, Refresh refresh) {
		this.mContext = context;
		this.refresh = refresh;

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.mine_head_img_baby) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.mine_head_img_baby) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Model model = null;
		// if (convertView == null) {
		model = new Model();
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.adapter_appointmen_list, null);
		model.mDate = (TextView) convertView.findViewById(R.id.apponiment_date);
		model.mWeek = (TextView) convertView.findViewById(R.id.apponiment_week);
		model.mTime = (TextView) convertView.findViewById(R.id.apponiment_time);
		model.mTotalTimes = (TextView) convertView
				.findViewById(R.id.appoinment_hour);
		model.mTotalMoney = (TextView) convertView
				.findViewById(R.id.apponiment_price);
		model.mUserIcon = (ImageView) convertView.findViewById(R.id.user_image);
		model.mUserName = (TextView) convertView.findViewById(R.id.user_name);
		model.mLevel = (RatingBar) convertView.findViewById(R.id.ratingbar);
		model.mAppoinmentNums = (TextView) convertView
				.findViewById(R.id.appointment_num);
		model.mGoodAt = (TextView) convertView
				.findViewById(R.id.appointment_good);
		model.mAddRequest = (TextView) convertView
				.findViewById(R.id.appointment_add_request);
		model.mChannelTeacher = (TextView) convertView
				.findViewById(R.id.appointment_channel_teacher);
		model.mNoMessage = (TextView) convertView
				.findViewById(R.id.no_message_content);
		model.mRemarkEdt = (EditText) convertView
				.findViewById(R.id.service_requirements);
		final Model models = model;
		model.mRemarkEdt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					index = (Integer) v.getTag();
				}
				return false;
			}
		});

		model.mRemarkEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (StringTool.isNoBlankAndNoNull(s.toString())) {
					for (int i = 0; i < data.size(); i++) {
						if (position == i) {
							data.get(position)
									.getTimeBean()
									.setMemo(
											models.mRemarkEdt.getText()
													.toString().trim());
						}
					}

					// new RefresfMemo(position, models);
				}
				// // hashMap.put(position, arg0.toString());
				// // refresh.onComplent(position, arg0.toString());

			}
		});
		convertView.setTag(model);

		// } else {
		// model = (Model) convertView.getTag();
		// }
		final TimesBean beans = data.get(position);
		TeacherBean teacherBean = beans.getTeacherBean();
		final TimeBean timeBean = beans.getTimeBean();
		String times = timeBean.getOrderTime();
		model.mDate.setText(DateChannel.channelMonth(times) + "/"
				+ DateChannel.channelDay(times));
		model.mTime.setText(DateChannel.channelStartTime(times) + "-"
				+ DateChannel.channelEndTime(times) + "点");
		model.mWeek.setText(DateChannel.getWeekday(DateChannel
				.dayFormate(times)));
		String mStartTime = DateChannel.channelStartTime(times);
		String mEndTime = DateChannel.channelEndTime(times);
		int mStartTimes = Integer.valueOf(mStartTime);
		int mEndTimes = Integer.valueOf(mEndTime);
		int timesCount = mEndTimes - mStartTimes;
		String timeCount = String.valueOf(timesCount);
		model.mTotalTimes.setText(timeCount);
		model.mRemarkEdt.setText(timeBean.getMemo());
		if (isAuto) {
			model.mChannelTeacher.setText("更换老师");
		} else {
			model.mChannelTeacher.setText("选择老师");
		}
		if (teacherBean != null && teacherBean.getTeacher_id() > 0) {
			model.mChannelTeacher.setText("更换老师");
			// 取老师数据
			model.mUserName.setVisibility(View.VISIBLE);
			model.mLevel.setVisibility(View.VISIBLE);
			model.mGoodAt.setVisibility(View.VISIBLE);
			model.mUserIcon.setVisibility(View.VISIBLE);
			model.mAppoinmentNums.setVisibility(View.VISIBLE);
			model.mNoMessage.setVisibility(View.GONE);
			model.mLevel.setVisibility(View.VISIBLE);
			model.mUserName.setText(teacherBean.getTeacher_name());
			model.mLevel.setRating(teacherBean.getTeacher_score() / 10);
			model.mGoodAt
					.setText("擅长:" + getSkill(teacherBean.getSkill_list()));
			model.mAppoinmentNums.setText("接单数:"
					+ String.valueOf(teacherBean.getOrder_count()));
			if (teacherBean.getService_list().get(0).getService_id() == 1) {
				int price = teacherBean.getService_list().get(0)
						.getService_price();
				if (DataConst.aggressive == 0) {
					ToastTool.toastMessage(mContext, "网络不畅通，请检查网络设置");
				}
				if (timesCount >= 2 && DataConst.aggressive > 0) {
					price = (int) (price * DataConst.aggressive / 100);
				}
				Double total = (double) price;
				// model.mTotalMoney.setText(total * timesCount / 100 + "元");
				model.mTotalMoney.setText(DoubleTool.channelPrice(total
						* timesCount / 100)
						+ "元");

			}
			String imageUrl = teacherBean.getTeacher_pic_url();
			imageLoader.displayImage(imageUrl, model.mUserIcon, options,
					animateFirstListener);
		} else {
			model.mUserName.setVisibility(View.INVISIBLE);
			model.mGoodAt.setVisibility(View.INVISIBLE);
			model.mUserIcon.setVisibility(View.INVISIBLE);
			model.mNoMessage.setVisibility(View.VISIBLE);
			model.mLevel.setVisibility(View.INVISIBLE);
			model.mAppoinmentNums.setVisibility(View.INVISIBLE);

		}
		// final Model models = model;

		// model.mRemarkEdt.setOnFocusChangeListener(new RefresfMemo(position,
		// model));
		model.mChannelTeacher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TeacherResourcesActivity.startActivity(mContext, position,
						timeBean);

			}
		});

		return convertView;
	}

	/**
	 * 图片加载第一次显示监听器
	 * 
	 * @author Administrator
	 * 
	 */
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 图片淡入效果
				FadeInBitmapDisplayer.animate(imageView, 500);
			}
		}
	}

	class Model {
		/**
		 * 日期
		 */
		private TextView mDate = null;

		/**
		 * 周几
		 */
		private TextView mWeek = null;

		/**
		 * 时间
		 */
		private TextView mTime = null;

		/**
		 * 总计几小时
		 */
		private TextView mTotalTimes = null;

		/**
		 * 费用
		 */
		private TextView mTotalMoney = null;
		/**
		 * 头像
		 */
		private ImageView mUserIcon = null;

		/**
		 * 老师姓名
		 */
		private TextView mUserName = null;

		/**
		 * 老师星级
		 */
		private RatingBar mLevel = null;
		/**
		 * 接单数量
		 */
		private TextView mAppoinmentNums = null;

		/**
		 * 擅长
		 */
		private TextView mGoodAt = null;

		/**
		 * 添加要求
		 */
		private TextView mAddRequest = null;

		/**
		 * 更换老师
		 */
		private TextView mChannelTeacher = null;
		/**
		 * 无教师信息
		 */
		private TextView mNoMessage = null;
		/**
		 * 备注信息
		 */
		private EditText mRemarkEdt = null;

	}

	class RefresfMemo implements OnFocusChangeListener {
		private int position;
		private Model model;

		public RefresfMemo(int position, Model model) {
			this.position = position;
			this.model = model;
		}

		@Override
		public void onFocusChange(View arg0, boolean flag) {
			if (!flag) {
				String memo = (String) model.mRemarkEdt.getTag(position);
				refresh.onComplent(position, memo);
			}
		}

	}

	private String getSkill(List<TeacherSkillBean> skill_list) {
		StringBuilder builder = new StringBuilder();
		if (skill_list != null && skill_list.size() > 0) {
			for (int i = 0, len = skill_list.size(); i < len; i++) {
				builder.append(getSkillName(skill_list.get(i).getSkill_id()));
				if (i < len - 1) {
					builder.append(",");
				}
			}
		}
		return builder.toString().trim();
	}

	private Object getSkillName(int skill_id) {
		skill_id = skill_id - 1;
		List<MapBean> list = MapBean.parseSkill();
		return list.get(skill_id).getName();
	}
}
