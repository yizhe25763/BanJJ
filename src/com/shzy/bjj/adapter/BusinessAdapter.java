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
import com.shzy.bjj.bean.BusinessDistrictBean;

/**
 * 商圈，选择条件
 */
public class BusinessAdapter extends BaseAdapter {

	private List<BusinessDistrictBean> data = new ArrayList<BusinessDistrictBean>();
	private int lastPosition = -1;
	private Context context;
	private LayoutInflater inflater;
	private Resources resources;

	public BusinessAdapter() {
	}

	public BusinessAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		resources = context.getResources();
	}

	public List<BusinessDistrictBean> getData() {
		return data;
	}

	public void setData(List<BusinessDistrictBean> data) {
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

			contentView = inflater.inflate(R.layout.adapter_condition_area,
					null);

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
			BusinessDistrictBean bean = data.get(position);
			holder.nameTextView.setText(bean.getDistrict_name());
			holder.nameTextView.setTag(bean);

		holder.layout.setBackgroundColor(resources.getColor(R.color.white));

		if (lastPosition != -1) {
			if (lastPosition == position) {
				holder.layout.setBackgroundColor(resources
						.getColor(R.color.white));
			} else {
				holder.layout.setBackgroundColor(resources
						.getColor(R.color.white));
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
