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

/**
 * 教师列表，选择条件
 */
public class TeacherTimeAdapter extends BaseAdapter {

	private List<MapBean> data = new ArrayList<MapBean>();
	private int lastPosition = -1;
	private int tag = 0;
	private Context context;
	private LayoutInflater inflater;
	private Resources resources;

	public TeacherTimeAdapter() {
	}

	public TeacherTimeAdapter(int flag, Context context) {
		this.tag = flag;
		inflater = LayoutInflater.from(context);
		resources = context.getResources();
	}

	public List<MapBean> getData() {
		return data;
	}

	public void setData(List<MapBean> data) {
		this.data = data;
	}

	public int getLastPosition() {
		return lastPosition;
	}

	public void setLastPosition(int lastPosition) {
		this.lastPosition = lastPosition;
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
		AreaHolder holder = null;
		if (contentView == null) {
			if (0 == tag) {
				contentView = inflater.inflate(
						R.layout.adapter_condition_skill, null);
			} else {
				contentView = inflater.inflate(R.layout.adapter_condition_area,
						null);
			}
			holder = new AreaHolder();
			holder.layout = (LinearLayout) contentView
					.findViewById(R.id.layout);
			holder.nameTextView = (TextView) contentView
					.findViewById(R.id.name);
			holder.idTextView = (TextView) contentView.findViewById(R.id.id);
			holder.line = contentView.findViewById(R.id.line);
			contentView.setTag(holder);
		} else {
			holder = (AreaHolder) contentView.getTag();
		}
		MapBean mapBean = data.get(position);
		holder.nameTextView.setText(mapBean.getName());
		holder.idTextView.setText(mapBean.getId());

		if (2 == tag) {
			holder.line.setVisibility(View.VISIBLE);
			holder.layout.setBackgroundColor(resources.getColor(R.color.white));
		} else {
			holder.line.setVisibility(View.GONE);
			if (1 == tag) {
				holder.layout.setBackgroundColor(resources
						.getColor(R.color.gray03));
			}
		}
		if (lastPosition != -1) {
			if (tag == 0) {
				if (lastPosition == position) {
					holder.nameTextView.setTextColor(resources
							.getColor(R.color.teacher_condition_selected));
				} else {
					holder.nameTextView.setTextColor(resources
							.getColor(R.color.teacher_condition_normal));
				}
			}
			if (tag == 1) {
				if (lastPosition == position) {
					holder.layout.setBackgroundColor(resources
							.getColor(R.color.white));
				} else {
					holder.layout.setBackgroundColor(resources
							.getColor(R.color.gray03));
				}
			}
			if (tag == 2) {

			}
		}
		return contentView;
	}

	class AreaHolder {
		private LinearLayout layout;
		private TextView nameTextView;
		private TextView idTextView;
		private View line;
	}

}
