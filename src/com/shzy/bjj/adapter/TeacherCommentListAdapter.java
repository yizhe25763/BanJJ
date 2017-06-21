package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shzy.bjj.R;
import com.shzy.bjj.bean.ImageBean;
import com.shzy.bjj.bean.ItemEntity;
import com.shzy.bjj.bean.PicBean;
import com.shzy.bjj.bean.commentList;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.photo.ImagePagerActivity;
import com.shzy.bjj.tools.photo.NoScrollGridAdapter;
import com.shzy.bjj.tools.photo.NoScrollGridView;
import com.shzy.bjj.view.CircularImage;

public class TeacherCommentListAdapter extends BaseAdapter {
	private List<ItemEntity> data = new ArrayList<ItemEntity>();
	private LayoutInflater inflater;
	private Context context;
	private ItemEntity bean;

	public void setData(List<ItemEntity> data) {
		this.data = data;

	}

	public TeacherCommentListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.context = context;
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
			contentView = inflater.inflate(
					R.layout.adapter_teacher_detail_comment, null);
			holder.nameTextView = (TextView) contentView
					.findViewById(R.id.name);
			holder.ratingbar = (RatingBar) contentView
					.findViewById(R.id.ratingbar);
			holder.phoneTextView = (TextView) contentView
					.findViewById(R.id.phone);
			holder.descTextView = (TextView) contentView
					.findViewById(R.id.desc);
			holder.dateTextView = (TextView) contentView
					.findViewById(R.id.date);
			holder.gridview = (NoScrollGridView) contentView
					.findViewById(R.id.gridview);
			holder.iv_avatar = (CircularImage) contentView
					.findViewById(R.id.iv_avatar);
			holder.gridviewTwo = (NoScrollGridView) contentView
					.findViewById(R.id.gridview_two);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		bean = data.get(position);
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最高配置
				.build();//
		ImageLoader.getInstance().displayImage(bean.getAvatar(),
				holder.iv_avatar, options);
		final ArrayList<String> imageUrls = bean.getImageUrls();
		if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
			holder.gridview.setVisibility(View.GONE);
			holder.gridviewTwo.setVisibility(View.GONE);
		} else {
			System.out.println(bean);
			if (imageUrls.size() == 4) {
				holder.gridviewTwo.setVisibility(View.VISIBLE);
				holder.gridview.setVisibility(View.GONE);
				holder.gridviewTwo.setAdapter(new NoScrollGridAdapter(context,
						imageUrls));
			} else {
				holder.gridviewTwo.setVisibility(View.GONE);
				holder.gridview.setVisibility(View.VISIBLE);
				holder.gridview.setAdapter(new NoScrollGridAdapter(context,
						imageUrls));
			}
		}
		holder.descTextView.setText(bean.getContent());
		int length = bean.getUserName().length();
		String names = "";
		for (int i = 0; i < length - 1; i++) {
			names = "*" + names;
		}
		if (StringTool.isNoBlankAndNoNull(bean.getUserName())) {
			holder.nameTextView.setText(bean.getUserName().substring(0, 1)
					+ names);
		} else {
			holder.nameTextView.setText("匿名");
		}
		// 点击回帖九宫格，查看大图
		holder.gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		holder.gridviewTwo.setSelector(new ColorDrawable(Color.TRANSPARENT));
		holder.gridviewTwo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				imageBrower(position, imageUrls);
			}
		});
		holder.gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				imageBrower(position, imageUrls);
			}
		});
		holder.phoneTextView.setText("");
		holder.ratingbar.setRating(bean.getTeacher_score() / 10);
		holder.dateTextView.setText(bean.getTime());
		return contentView;
	}

	class ViewHolder {
		private CircularImage iv_avatar;
		private TextView nameTextView;
		private RatingBar ratingbar;
		private TextView phoneTextView;
		private TextView descTextView;
		private TextView dateTextView;
		private NoScrollGridView gridview;
		private NoScrollGridView gridviewTwo;

	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}
}
