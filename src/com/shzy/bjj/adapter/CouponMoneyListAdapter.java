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
import com.shzy.bjj.bean.VoucherBean;

public class CouponMoneyListAdapter extends BaseAdapter {
	private List<VoucherBean> data = new ArrayList<VoucherBean>();
	private LayoutInflater inflater;
	private Context context;

	public CouponMoneyListAdapter(Context context, List<VoucherBean> data) {
		this.context = context;
		inflater = LayoutInflater.from(context);
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
		final VoucherBean bean = data.get(position);

		if (bean.getStatus() == 1) {
			holder.bt.setVisibility(View.INVISIBLE);
		} else {
			holder.bt.setVisibility(View.VISIBLE);
		}
		holder.numberTextView.setText(bean.getId()+"");
		holder.nameTextView.setText(bean.getName());
		holder.descTextView.setText(bean.getDescb());
		holder.expire_dateTextView.setText(bean.getExpire_date());
//		holder.bt.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				// 兑换
//				MineVoucherDialog.startActivity(context, bean);
//			}
//		});
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
