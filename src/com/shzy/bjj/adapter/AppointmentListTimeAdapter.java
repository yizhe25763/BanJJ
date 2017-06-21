package com.shzy.bjj.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.activity.home.DateChannel;

/**
 * 
 * 
 * @brief 预约列表时间适配器
 * @author Fanhao Yi
 * @data 2015年7月1日下午2:36:44
 * @version V1.0
 */
public class AppointmentListTimeAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> list;

	public AppointmentListTimeAdapter(Context mContext, List<String> list) {
		this.mContext = mContext;
		this.list = list;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
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
					R.layout.adapter_appointmen_list_time, null);
			model.mDate = (TextView) convertView
					.findViewById(R.id.apponiment_date);
			model.mWeek = (TextView) convertView
					.findViewById(R.id.apponiment_week);
			model.mTime = (TextView) convertView
					.findViewById(R.id.apponiment_time);
			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		String str = list.get(position);
		model.mDate.setText(DateChannel.channelMonth(str) + "/"
				+ DateChannel.channelDay(str));
		model.mTime.setText(DateChannel.channelStartTime(str) + "-"
				+ DateChannel.channelEndTime(str) + "点");
		model.mWeek
				.setText(DateChannel.getWeekday(DateChannel.dayFormate(str)));
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

	}

}
