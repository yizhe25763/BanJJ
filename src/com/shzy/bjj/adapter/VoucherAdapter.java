package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.activity.mine.MineVoucherDialog;
import com.shzy.bjj.activity.teacher.GetVoucherData;
import com.shzy.bjj.bean.VoucherBean;

public class VoucherAdapter extends BaseAdapter {
	private List<VoucherBean> data = new ArrayList<VoucherBean>();
	private LayoutInflater inflater;
	private Context context;
	private GetVoucherData getVoucherData;

	public List<VoucherBean> getData() {
		return data;
	}

	public void setData(List<VoucherBean> data) {
		this.data = data;
	}

	public VoucherAdapter(Context context, GetVoucherData getVoucherData) {
		this.context = context;
		this.getVoucherData = getVoucherData;
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
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.adapter_voucher, null);
			holder = new ViewHolder();
			holder.numberTextView = (TextView) contentView
					.findViewById(R.id.number);
			holder.nameTextView = (TextView) contentView
					.findViewById(R.id.name);
			holder.descTextView = (TextView) contentView
					.findViewById(R.id.desc);
			holder.expire_dateTextView = (TextView) contentView
					.findViewById(R.id.expire_date);
			holder.bt = (ImageButton) contentView.findViewById(R.id.bt);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		final ViewHolder holders = holder;
		final VoucherBean bean = data.get(position);
		int size = 0;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getIsSelector()) {
				size++;
			}
		}
		holder.numberTextView.setText(bean.getId() + "");
		holder.nameTextView.setText(bean.getName());
		holder.descTextView.setText(bean.getDescb());
		holder.expire_dateTextView.setText(bean.getExpire_date());
		if (bean.getIsSelector()) {
			holder.bt.setBackgroundResource(R.drawable.choose_address_down);
		} else {
			holder.bt.setBackgroundResource(R.drawable.choose_address_up);

		}
		final int positions = position;
		holder.bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				if (bean.getIsSelector() == false) {// 当前代金券未被选择
					holders.bt
							.setBackgroundResource(R.drawable.choose_address_down);
					bean.setIsSelector(true);
					getVoucherData.onClick(bean, data,positions,true);
				} else {// 取消当前代金券
					holders.bt
							.setBackgroundResource(R.drawable.choose_address_up);
					bean.setIsSelector(false);
					getVoucherData.onClick(bean, data,positions,false);
				}
			}
		});
		return contentView;
	}

	class ViewHolder {
		private TextView numberTextView;
		private TextView nameTextView;
		private TextView descTextView;
		private TextView expire_dateTextView;
		private ImageButton bt;
	}
}
