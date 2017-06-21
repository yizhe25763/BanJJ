package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.bean.MapBean;

public class TeacherDetailTimeAdapter extends BaseAdapter {

	private List<MapBean> data = new ArrayList<MapBean>();
	private int tag = 0;
	private Context context;
	private LayoutInflater inflater;
	private Resources resources;
	private boolean flag = false;
	private int positionByTime;

	public TeacherDetailTimeAdapter() {
	}

	public TeacherDetailTimeAdapter(Boolean flag, Context context,
			int positionByTime) {
		this.flag = flag;
		this.positionByTime = positionByTime;
		inflater = LayoutInflater.from(context);
		resources = context.getResources();
	}

	public List<MapBean> getData() {
		return data;
	}

	public void setData(List<MapBean> data) {
		this.data = data;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
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
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (contentView == null) {
			contentView = inflater.inflate(
					R.layout.adapter_teacher_detail_date_item, null);
			holder = new ViewHolder();
			holder.layout = (LinearLayout) contentView
					.findViewById(R.id.layout);
			holder.nameTextView = (TextView) contentView
					.findViewById(R.id.name);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		MapBean mapBean = data.get(position);
		if (flag) {
			holder.nameTextView.setText(mapBean.getName());
		} else {
			if (tag == 0 || (tag == 1 && position <= positionByTime)) {
				holder.layout.setBackgroundColor(resources
						.getColor(R.color.teacher_detail_appointment));
			} else {
				if (mapBean.isEnable()) {
					holder.layout.setBackgroundColor(resources
							.getColor(R.color.teacher_detail_free));
				} else {
					holder.layout.setBackgroundColor(resources
							.getColor(R.color.teacher_detail_appointment));
				}
			}
		}

		return contentView;
	}

	class ViewHolder {
		private LinearLayout layout;
		private TextView nameTextView;
	}

}
