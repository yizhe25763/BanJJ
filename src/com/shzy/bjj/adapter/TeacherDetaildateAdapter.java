package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.bean.MapBean;

public class TeacherDetaildateAdapter extends BaseAdapter {

	private List<List<MapBean>> data = new ArrayList<List<MapBean>>();
	private List<MapBean> titleArray = new ArrayList<MapBean>();
	private Context context;
	private LayoutInflater inflater;
	private Resources resources;
	private int positionByTime;

	public List<List<MapBean>> getData() {
		return data;
	}

	public void setData(List<List<MapBean>> data) {
		this.data = data;
	}

	public List<MapBean> getTitleArray() {
		return titleArray;
	}

	public void setTitleArray(List<MapBean> titleArray) {
		this.titleArray = titleArray;
	}

	public TeacherDetaildateAdapter() {
	}

	public TeacherDetaildateAdapter(Context context, int positionByTime) {
		this.context = context;
		this.positionByTime = positionByTime;
		inflater = LayoutInflater.from(context);
		resources = context.getResources();
	}

	@Override
	public int getCount() {
		return titleArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		return titleArray.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		TeacherDetailTimeAdapter todayAdapter = null;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(
					R.layout.adapter_teacher_detail_date, null);
			holder.layout = (LinearLayout) contentView
					.findViewById(R.id.layout);
			holder.nameTextView = (TextView) contentView
					.findViewById(R.id.name);
			holder.gridView = (GridView) contentView
					.findViewById(R.id.grideview);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		if (todayAdapter == null) {
			todayAdapter = new TeacherDetailTimeAdapter(false, context,
					positionByTime);
		}
		if (position == 0) {
			holder.nameTextView.setText("今天");
		} else if (position == 1) {
			holder.nameTextView.setText("明天");
		} else if (position == 2) {
			holder.nameTextView.setText("后天");
		} else {
			holder.nameTextView.setText(titleArray.get(position).getTitle());
		}
		holder.gridView.setAdapter(todayAdapter);
		todayAdapter.setData(data.get(position));
		todayAdapter.setTag(position);
		todayAdapter.notifyDataSetChanged();
		return contentView;
	}

	class ViewHolder {
		private LinearLayout layout;
		private TextView nameTextView;
		private GridView gridView;
	}

}
