package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shzy.bjj.R;
import com.shzy.bjj.bean.TeacherDetailSkillBean;
import com.shzy.bjj.tools.StringTool;

@SuppressLint("ResourceAsColor")
public class HorizontalGrideAdapter extends BaseAdapter {
	private List<TeacherDetailSkillBean> data = new ArrayList<TeacherDetailSkillBean>();
	private LayoutInflater inflater;
	protected DisplayImageOptions options;
	protected ImageLoader imageLoader;
	private Context context;

	public List<TeacherDetailSkillBean> getData() {
		return data;
	}

	public void setData(List<TeacherDetailSkillBean> data) {
		this.data = data;
	}

	public HorizontalGrideAdapter(Context context, ImageLoader imageLoader) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.imageLoader = imageLoader;
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.mine_head_img)
				.showImageOnFail(R.drawable.mine_head_img).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(
					R.layout.adapter_horizontalgrideview, null);
			holder.title = (TextView) contentView.findViewById(R.id.title);
			holder.content = (TextView) contentView.findViewById(R.id.content);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		TeacherDetailSkillBean bean = data.get(position);
		int techerType = bean.getType();
		String title = bean.getName();
		String content = bean.getNum();
		switch (techerType) {
		case 1:// 教师资格证
			contentView.setBackgroundResource(R.drawable.yy_pic_zs_jszgzs);
			setContent(holder, title, content, 1);
			break;
		case 2:// 健康证
			contentView.setBackgroundResource(R.drawable.yy_pic_zs_jkzs02);
			holder.title.setVisibility(View.GONE);
			holder.content.setVisibility(View.GONE);
			break;
		case 3:// 普通话等级证
			contentView.setBackgroundResource(R.drawable.yy_pic_zs_pthzs);
			setContent(holder, title, content, 3);
			break;
		case 4:// 其他证件
			contentView.setBackgroundResource(R.drawable.yy_pic_zs_qtzs);
			setContent(holder, title, content, 4);
			break;

		default:
			break;
		}

		return contentView;
	}

	private void setContent(ViewHolder holder, String title, String content,
			int type) {
		if (!StringTool.isNoBlankAndNoNull(title)) {
			title = "";
		}
		if (!StringTool.isNoBlankAndNoNull(content)) {
			content = "";
		}
		switch (type) {
		case 1:
			holder.content.setTextColor(context.getResources().getColor(
					R.color.yellow_color));
			break;
		case 3:
			holder.content.setTextColor(context.getResources().getColor(
					R.color.green_color));
			break;
		case 4:
			holder.content.setTextColor(context.getResources().getColor(
					R.color.other_color));
			break;
		default:
			break;
		}
		holder.title.setText(title);
		holder.content.setText("NO：" + content);

	}

	class ViewHolder {
		private RelativeLayout imageView;
		private TextView title;
		private TextView content;
	}

}
