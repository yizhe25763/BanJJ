package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.activity.mine.MineAppointmentActivity.SetDefault;
import com.shzy.bjj.activity.mine.MineAppointmentAddActivity;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.tools.StringTool;

public class AppointmentAdapter extends BaseAdapter {
	private List<AppointmentBean> data = new ArrayList<AppointmentBean>();
	private LayoutInflater inflater;
	private Resources resources;
	private Context context;
	private SetDefault setDefault;

	public List<AppointmentBean> getData() {
		return data;
	}

	public void setData(List<AppointmentBean> data) {
		this.data = data;
	}

	public AppointmentAdapter(Context context, SetDefault setDefault) {
		this.context = context;
		this.setDefault = setDefault;
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
	public View getView(final int position, View contentView,
			ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (contentView == null) {
			contentView = inflater
					.inflate(R.layout.adapter_appointmental, null);
			holder = new ViewHolder();
			holder.appointment_name = (TextView) contentView
					.findViewById(R.id.appointment_name);
			holder.appointment_phone = (TextView) contentView
					.findViewById(R.id.appointment_phone);
			holder.appointment_mobile = (TextView) contentView
					.findViewById(R.id.appointment_mobile);
			holder.appointment_address = (TextView) contentView
					.findViewById(R.id.appointment_address);
			holder.mine_appointment_default = (TextView) contentView
					.findViewById(R.id.mine_appointment_default);
			holder.mine_appointment_default_set = (TextView) contentView
					.findViewById(R.id.mine_appointment_default_set);
			holder.mine_appointment_edit = (TextView) contentView
					.findViewById(R.id.mine_appointment_edit);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		final AppointmentBean bean = data.get(position);
		holder.appointment_name.setTag(bean);

		holder.appointment_name.setText(bean.getName());
		holder.appointment_mobile.setText(bean.getTelphone());
		holder.appointment_phone.setText(bean.getMobile());
		String address_room = bean.getAddress_room();
		if (!StringTool.isNoBlankAndNoNull(address_room)) {
			address_room = "";
		}
		holder.appointment_address.setText(bean.getAddress() + address_room);
		if (bean.getIs_default() == 1) {
			holder.mine_appointment_default.setVisibility(View.VISIBLE);
			holder.mine_appointment_default_set.setVisibility(View.INVISIBLE);
		} else {
			holder.mine_appointment_default.setVisibility(View.INVISIBLE);
			holder.mine_appointment_default_set.setVisibility(View.VISIBLE);
		}
		holder.mine_appointment_edit.setOnClickListener(new Click(false,
				holder, bean, position));
		holder.mine_appointment_default_set.setOnClickListener(new Click(true,
				holder, bean, position));
		return contentView;
	}

	class Click implements OnClickListener {
		private boolean flag;
		private ViewHolder holder;
		private AppointmentBean bean;
		private int position;

		public Click(boolean flag, ViewHolder holder, AppointmentBean bean,
				int position) {
			this.flag = flag;
			this.holder = holder;
			this.bean = bean;
			this.position = position;
		}

		@Override
		public void onClick(View view) {
			if (flag) {
				holder.mine_appointment_default_set
						.setVisibility(View.INVISIBLE);
				setDefault.onReset(position);
			} else {
				if (bean != null) {
					MineAppointmentAddActivity.startActivity(context, false,
							bean);
				}
			}
		}

	}

	class ViewHolder {
		private TextView appointment_name;
		private TextView appointment_phone;
		private TextView appointment_mobile;
		private TextView appointment_address;
		private TextView mine_appointment_default;
		private TextView mine_appointment_default_set;
		private TextView mine_appointment_edit;
	}

}
