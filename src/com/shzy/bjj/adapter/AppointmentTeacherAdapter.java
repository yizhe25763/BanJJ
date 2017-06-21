package com.shzy.bjj.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.home.TeacherResourcesActivity;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.tools.StringTool;

/**
 * 
 * @brief 预约列表适配器
 * @author Fanhao Yi
 * @data 2015年6月30日下午4:34:22
 * @version V1.0
 */
public class AppointmentTeacherAdapter extends BaseAdapter {
	private Context mContext;
	private List<TeacherBean> data;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private String mRemark;

	public AppointmentTeacherAdapter(Context mContext, List<TeacherBean> data,
			String mRemark) {
		this.mContext = mContext;
		this.data = data;
		this.mRemark = mRemark;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.mine_head_img_baby)
				.showImageOnFail(R.drawable.mine_head_img_baby)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

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
		if (convertView == null) {
			model = new Model();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_appointmen_list, null);
			model.mDate = (TextView) convertView
					.findViewById(R.id.apponiment_date);
			model.mWeek = (TextView) convertView
					.findViewById(R.id.apponiment_week);
			model.mTime = (TextView) convertView
					.findViewById(R.id.apponiment_time);
			model.mTotalTimes = (TextView) convertView
					.findViewById(R.id.appoinment_hour);
			model.mTotalMoney = (TextView) convertView
					.findViewById(R.id.apponiment_price);
			model.mUserIcon = (ImageView) convertView
					.findViewById(R.id.user_image);
			model.mUserName = (TextView) convertView
					.findViewById(R.id.user_name);
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
			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		TeacherBean bean = data.get(position);
		model.mNoMessage.setVisibility(View.GONE);
		model.mUserName.setText(bean.getTeacher_name());
		model.mAppoinmentNums.setText(String.valueOf(bean.getOrder_count()));
		model.mGoodAt.setText(bean.getTeacher_descb());
		model.mLevel.setRating(bean.getTeacher_score());
		model.mRemarkEdt.setText(mRemark);
		// 加载头像
		String imageUrl = bean.getTeacher_pic_url();
		if (StringTool.isNoBlankAndNoNull(imageUrl)) {
			imageLoader.displayImage(imageUrl, model.mUserIcon, options,
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
		model.mChannelTeacher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TeacherResourcesActivity.startActivity(mContext,position);
			}
		});
		return convertView;
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

}
