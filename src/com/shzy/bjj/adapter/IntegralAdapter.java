package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.bean.IntegralBean;

public class IntegralAdapter extends BaseAdapter {
	private List<IntegralBean> data = new ArrayList<IntegralBean>();
	private LayoutInflater inflater;
	private Resources resources;

	public List<IntegralBean> getData() {
		return data;
	}

	public void setData(List<IntegralBean> data) {
		this.data = data;
	}

	public IntegralAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		resources = context.getResources();
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
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.adapter_integral, null);
			holder = new ViewHolder();
			holder.idTextView = (TextView) contentView.findViewById(R.id.id);
			holder.typeTextView = (TextView) contentView
					.findViewById(R.id.type);
			holder.timeTextView = (TextView) contentView
					.findViewById(R.id.time);
			holder.nameTextView = (TextView) contentView
					.findViewById(R.id.name);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		IntegralBean bean = data.get(position);
		holder.idTextView.setTag(bean);
		int type = bean.getType();
		if (type > 0) {
			holder.typeTextView.setTextColor(resources
					.getColor(R.color.voucher_txt_asc));
		} else {
			holder.typeTextView.setTextColor(resources
					.getColor(R.color.voucher_txt_desc));
		}
		holder.typeTextView.setText(bean.getValue() + "分");
		holder.timeTextView.setText(bean.getTime());
		holder.nameTextView.setText(bean.getDescb());
		return contentView;
	}

	class ViewHolder {
		private TextView idTextView;
		private TextView typeTextView;
		private TextView timeTextView;
		private TextView nameTextView;
	}

	private String getType(int type) {
		String str = "";
		switch (type) {
		case 1:
			str = resources.getString(R.string.info_sysytem);
			break;

		default:
			break;
		}
		return str;
	}
}
