package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shzy.bjj.R;
import com.shzy.bjj.activity.teacher.GetCalendarData;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.view.CustomListView;

public class ConditionGrideViewAdapter extends BaseAdapter {

	private List<List<MapBean>> data = new ArrayList<List<MapBean>>();
	private Context context;
	private LayoutInflater inflater;
	private GetCalendarData getCalendarData;
	private boolean flag;
	private int max;
	private int total;
	private boolean isMax;
	// 判断当前时间对应的时间段，24小时内不可预约
	private int dayTime_position = -1;

	public ConditionGrideViewAdapter() {
	}

	public ConditionGrideViewAdapter(Context context,
			GetCalendarData getCalendarData, boolean flag, int max, int total,
			boolean isMax, int dayTime_position) {
		this.context = context;
		this.flag = flag;
		this.max = max;
		this.total = total;
		this.isMax = isMax;
		this.dayTime_position = dayTime_position;
		this.getCalendarData = getCalendarData;
		inflater = LayoutInflater.from(context);
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<List<MapBean>> getData() {
		return data;
	}

	public void setData(List<List<MapBean>> data) {
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
		ConditionListViewAdapter adapter = null;
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.condition_listview, null);
			holder = new ViewHolder();
			holder.listView = (CustomListView) contentView
					.findViewById(R.id.listview);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		if (adapter == null) {
			adapter = new ConditionListViewAdapter(context, flag, max, isMax,dayTime_position);
		}
		List<MapBean> list = data.get(position);
		adapter.setTag(position);
		adapter.setData(list);
		adapter.setTotal(total);
		adapter.setGetCalendarData(getCalendarData);
		adapter.notifyDataSetChanged();
		holder.listView.setAdapter(adapter);
		return contentView;
	}

	class ViewHolder {
		private CustomListView listView;
	}

}
