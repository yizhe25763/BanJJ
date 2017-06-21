package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.home.AppointmentListActivity;
import com.shzy.bjj.activity.teacher.ChooseTeacherActivity;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.bean.TeacherDetailBean;
import com.shzy.bjj.bean.TeacherSkillBean;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.CircularImage;

public class TeacherListAdapter extends BaseAdapter {
	private List<TeacherBean> data = new ArrayList<TeacherBean>();
	private LayoutInflater inflater;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Context context;
	private Boolean flag = false;
	private int pose;

	public List<TeacherBean> getData() {
		return data;
	}

	public void setData(List<TeacherBean> data) {
		this.data = data;
	}

	public TeacherListAdapter(Context context, boolean flag, int position) {
		this.context = context;
		this.flag = flag;
		this.pose = position;
		inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.mine_head_img_baby)
				.showImageForEmptyUri(R.drawable.mine_head_img_baby)
				.showImageOnFail(R.drawable.mine_head_img_baby)
				.cacheInMemory(false).cacheOnDisk(true)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
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

	class ViewHolder {
		private CircularImage head_img;
		private TextView teacher_nameTextView;
		private RatingBar ratingbar;
		private ImageView teacher_order_hasImageView;
		private ImageView teacher_order_focusImageView;

		private TextView descTextView;
		private TextView teacher_skillTextView;

		private TextView teacher_order_numberTextView;
		private TextView chooseTextView;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.adapter_teacher, null);
			holder.head_img = (CircularImage) contentView
					.findViewById(R.id.head_img);
			holder.teacher_nameTextView = (TextView) contentView
					.findViewById(R.id.teacher_name);
			holder.ratingbar = (RatingBar) contentView
					.findViewById(R.id.ratingbar);
			holder.teacher_order_hasImageView = (ImageView) contentView
					.findViewById(R.id.teacher_order_has);
			holder.teacher_order_focusImageView = (ImageView) contentView
					.findViewById(R.id.teacher_order_focus);

			holder.teacher_skillTextView = (TextView) contentView
					.findViewById(R.id.teacher_skill);
			holder.descTextView = (TextView) contentView
					.findViewById(R.id.desc);
			holder.teacher_order_numberTextView = (TextView) contentView
					.findViewById(R.id.teacher_order_number);
			holder.chooseTextView = (TextView) contentView
					.findViewById(R.id.choose);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		final TeacherBean bean = data.get(position);
		holder.ratingbar.setRating(bean.getTeacher_score() / 10);
		holder.teacher_nameTextView.setText(bean.getTeacher_name());
		holder.teacher_nameTextView.setTag(bean);
		holder.teacher_skillTextView.setText(getSkill(bean.getSkill_list()));
		holder.descTextView.setText(bean.getTeacher_descb());

		holder.teacher_order_numberTextView.setText(String.valueOf(bean
				.getOrder_count()));
		if (bean.isFollowed()) {
			holder.teacher_order_focusImageView.setVisibility(View.VISIBLE);
		} else {
			holder.teacher_order_focusImageView.setVisibility(View.GONE);
		}
		if (bean.isServiced()) {
			holder.teacher_order_hasImageView.setVisibility(View.VISIBLE);
		} else {
			holder.teacher_order_hasImageView.setVisibility(View.GONE);
		}
		// 加载头像
		String imageUrl = bean.getTeacher_pic_url();
		imageLoader.displayImage(imageUrl, holder.head_img, options);

		// 选她
		holder.chooseTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (flag) {
					AppointmentListActivity.data.get(pose).setTeacherBean(bean);
					AppointmentListActivity.isRefresh = true;
					AppManager.getAppManager().finishActivity();
				} else {
					ChooseTeacherActivity.startActivity(context, bean);
				}
			}
		});
		return contentView;
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
