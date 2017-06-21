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

public class ConditionTitleListViewAdapter extends BaseAdapter {
	private List<MapBean> data = new ArrayList<MapBean>();
	private int tag = 0;
	private LayoutInflater inflater;
	private Resources resources;

	public ConditionTitleListViewAdapter() {
	}

	public ConditionTitleListViewAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		resources = context.getResources();
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
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
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.condition_title_item, null);
			holder.nameTextView = (TextView) contentView
					.findViewById(R.id.name);
			holder.titleTextView = (TextView) contentView
					.findViewById(R.id.title);
			holder.layout = (LinearLayout) contentView
					.findViewById(R.id.layout);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		final MapBean mapBean = data.get(position);
		holder.nameTextView.setTag(mapBean);
		holder.titleTextView.setText(mapBean.getTitle());

		if (position == 0) {
			holder.nameTextView.setText(mapBean.getName());
			holder.nameTextView.setTextColor(resources
					.getColor(R.color.calendar_title__select_clor));
			holder.titleTextView.setTextColor(resources
					.getColor(R.color.calendar_title__select_clor));
		} else {
			int week = mapBean.getId();
			holder.nameTextView.setText(MapBean.getWeek(week));
			if (week == 1 || week == 7) {
				holder.nameTextView.setTextColor(resources
						.getColor(R.color.calendar_weekday_clor));
				holder.titleTextView.setTextColor(resources
						.getColor(R.color.calendar_weekday_clor));
			} else {
				holder.nameTextView.setTextColor(resources
						.getColor(R.color.calendar_week_clor));
				holder.titleTextView.setTextColor(resources
						.getColor(R.color.calendar_week_clor));
			}
		}
		return contentView;
	}



	class ViewHolder {
		private LinearLayout layout;
		private TextView nameTextView;
		private TextView titleTextView;
	}

}
