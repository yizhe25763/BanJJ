package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.activity.home.DateChannel;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.tools.DateTimeTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.view.AsyncImageLoaderLocal;
import com.shzy.bjj.view.AsyncImageLoaderLocal.ImageCallback;

public class AppoinmentPayTeacherListAdapter extends BaseAdapter {
	private Context mContext;
	private List<MapBean> data = new ArrayList<MapBean>();
	private AsyncImageLoaderLocal asyncLoad = null;

	public AppoinmentPayTeacherListAdapter(Context mContext) {
		this.mContext = mContext;
		asyncLoad = new AsyncImageLoaderLocal();
	}

	public List<MapBean> getData() {
		return data;
	}

	public void setData(List<MapBean> data) {
		this.data = data;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Model model = null;
		if (convertView == null) {
			model = new Model();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_appointment_pay_teacher, null);
			model.mTime = (TextView) convertView
					.findViewById(R.id.apponiment_time);
			model.icon = (ImageView) convertView
					.findViewById(R.id.teacher_icon);
			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		MapBean bean = data.get(position);
		String imageUrl = bean.getTitle();
		if (StringTool.isNoBlankAndNoNull(imageUrl)) {
			String imageId = imageUrl.substring(imageUrl.lastIndexOf("/"),
					imageUrl.length());
			imageId = imageId.replace(".", "");
			model.icon.setTag(imageUrl);
			Bitmap bitmap = asyncLoad.loadDrawable(imageId, imageUrl,
					new ImageCallback() {

						@Override
						public void imageLoaded(Bitmap imageDrawable,
								String imageUrl, ImageView imageView) {
							String url = (String) imageView.getTag();
							if (imageDrawable != null && url != null
									&& url.equals(imageUrl)) {
								imageView.setImageBitmap(imageDrawable);
							}
						}
					}, mContext, model.icon);
			if (bitmap != null) {
				model.icon.setImageBitmap(bitmap);
			} else {
				model.icon.setImageResource(R.drawable.mine_head_img_baby);
			}

		}
		String time = bean.getName();
		String date = DateChannel.dayFormate(time);
		model.mTime.setText(DateTimeTool.getDateDetail(date)
				+ DateChannel.channelStartTime(time) + "-"
				+ DateChannel.channelEndTime(time) + "点");

		return convertView;

	}

	class Model {
		/**
		 * 周几
		 */
		private ImageView icon = null;

		/**
		 * 时间
		 */
		private TextView mTime = null;

	}
}
