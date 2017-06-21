package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.bean.TeacherDetailResumeBean;

public class TeacherResumeAdapter extends BaseAdapter {
	private List<TeacherDetailResumeBean> data = new ArrayList<TeacherDetailResumeBean>();
	private LayoutInflater inflater;

	public List<TeacherDetailResumeBean> getData() {
		return data;
	}

	public void setData(List<TeacherDetailResumeBean> data) {
		this.data = data;
	}
	
	public TeacherResumeAdapter(Context context) {
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(
					R.layout.adapter_teacher_detail_education, null);
			holder.schoolTextView = (TextView) contentView
					.findViewById(R.id.school);
			holder.typeTextView = (TextView) contentView
					.findViewById(R.id.type);
			holder.dateTextView = (TextView) contentView
					.findViewById(R.id.date);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		TeacherDetailResumeBean bean = data.get(position);
		holder.dateTextView.setText(bean.getTime_descb());
		holder.typeTextView.setText(bean.getProfession_descb());
		holder.schoolTextView.setText(bean.getUnit_descb());
		return contentView;
	}

	class ViewHolder {
		private TextView schoolTextView;
		private TextView typeTextView;
		private TextView dateTextView;
	}

}
