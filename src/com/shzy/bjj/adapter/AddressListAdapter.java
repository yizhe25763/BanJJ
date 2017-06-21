package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.bean.AppointmentBean;
import com.shzy.bjj.tools.StringTool;

/**
 * 
 * 
 * @brief 常用地址适配器
 * @author Fanhao Yi
 * @data 2015年7月6日下午2:16:50
 * @version V1.0
 */
public class AddressListAdapter extends BaseAdapter {
	private List<AppointmentBean> data = new ArrayList<AppointmentBean>();
	private LayoutInflater inflater;
	private Context mContext;

	public void setData(List<AppointmentBean> data) {
		this.data = data;
	}

	public AddressListAdapter(Context context) {
		this.mContext = context;
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (contentView == null) {
			contentView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_address_list, null);
			holder = new ViewHolder();
			holder.mUserName = (TextView) contentView
					.findViewById(R.id.user_name);
			holder.mAddress = (TextView) contentView
					.findViewById(R.id.user_address);
			holder.mIphone = (TextView) contentView
					.findViewById(R.id.user_iphone);
			holder.mCheck = (ImageView) contentView
					.findViewById(R.id.check_address);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		AppointmentBean bean = data.get(position);
		holder.mUserName.setTag(bean);
		holder.mUserName.setText(bean.getName().toString().trim());
		String address_room = bean.getAddress_room();
		if (!StringTool.isNoBlankAndNoNull(address_room)) {
			address_room = "";
		}
		holder.mAddress.setText(bean.getAddress().toString().trim()
				+ address_room);
		holder.mIphone.setText(bean.getMobile().toString().trim());
		if (bean.getIs_default() == 1) {
			holder.mCheck.setBackgroundResource(R.drawable.choose_address_down);
		} else {
			holder.mCheck.setBackgroundResource(R.drawable.choose_address_up);
		}
		return contentView;
	}

	class ViewHolder {
		/**
		 * 联系人
		 */
		private TextView mUserName;
		/**
		 * 地址
		 */
		private TextView mAddress;
		/**
		 * 电话
		 */
		private TextView mIphone;
		/**
		 * 选择按钮
		 */
		private ImageView mCheck;
	}

}
