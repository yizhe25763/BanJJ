package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shzy.bjj.R;
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.tools.DateTimeTool;
import com.shzy.bjj.view.CircularImage;

public class BabyListAdapter extends BaseAdapter implements OnClickListener {
	private List<BabyBean> data = new ArrayList<BabyBean>();
	private LayoutInflater inflater;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Boolean tag;

	public List<BabyBean> getData() {
		return data;
	}

	public void setData(List<BabyBean> data) {
		this.data = data;
	}

	public BabyListAdapter(Context context, ImageLoader imageLoader, Boolean tag) {
		this.imageLoader = imageLoader;
		inflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.mine_head_img_baby)
				.showImageOnFail(R.drawable.mine_head_img_baby)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		this.tag = tag;

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
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.adapter_baby, null);
			holder.babyCircularImage = (CircularImage) contentView
					.findViewById(R.id.head_img_baby);
			holder.sexImageView = (ImageView) contentView
					.findViewById(R.id.sex);
			holder.baby_detail_name = (TextView) contentView
					.findViewById(R.id.baby_detail_name);
			holder.baby_detail_age = (TextView) contentView
					.findViewById(R.id.baby_detail_age);
			holder.baby_detail_number = (TextView) contentView
					.findViewById(R.id.baby_detail_number);
			holder.imagess = (ImageView) contentView.findViewById(R.id.images);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		BabyBean bean = data.get(position);
		holder.baby_detail_name.setTag(bean);
		holder.baby_detail_name.setText(bean.getName());
		if (tag) {
		} else {
			holder.imagess.setBackgroundResource(R.drawable.mine_pic_arrow);

		}
		// if (StringTool.isNoBlankAndNoNull(bean.getIdentity())) {
		// holder.baby_detail_number.setText(bean.getIdentity().substring(0, 6)
		// + "************");
		// } else {
		// holder.baby_detail_number.setText("");
		// }
		holder.baby_detail_age.setText(DateTimeTool.channelBrithday(bean
				.getBirthday()));
		if (bean.getSex() == 0) {
			holder.sexImageView.setImageResource(R.drawable.mine_pic_male);
		} else {
			holder.sexImageView.setImageResource(R.drawable.mine_pic_female);
		}
		String imageUrl = bean.getPic_url();
		imageLoader.displayImage(imageUrl, holder.babyCircularImage, options);
		return contentView;
	}

	class ViewHolder {
		private ImageView sexImageView;
		private CircularImage babyCircularImage;
		private TextView baby_detail_name;
		private TextView baby_detail_age;
		private TextView baby_detail_number;
		private ImageView imagess;

	}

	@Override
	public void onClick(View view) {

	}
}
