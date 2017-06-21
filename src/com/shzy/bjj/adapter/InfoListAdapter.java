package com.shzy.bjj.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.bean.InfoBean;
import com.shzy.bjj.tools.StringTool;

public class InfoListAdapter extends BaseAdapter {
	private List<InfoBean> data = new ArrayList<InfoBean>();
	private LayoutInflater inflater;
	private Resources resources;

	public List<InfoBean> getData() {
		return data;
	}

	public void setData(List<InfoBean> data) {
		this.data = data;
	}

	public InfoListAdapter(Context context) {
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
			contentView = inflater.inflate(R.layout.adapter_info, null);
			holder = new ViewHolder();
			holder.idTextView = (TextView) contentView.findViewById(R.id.id);
			holder.typeTextView = (TextView) contentView
					.findViewById(R.id.type);
			holder.timeTextView = (TextView) contentView
					.findViewById(R.id.time);
			holder.contenTextView = (TextView) contentView
					.findViewById(R.id.content);
			holder.hot = (ImageView) contentView.findViewById(R.id.hot);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		InfoBean bean = data.get(position);
		holder.contenTextView.setTag(bean);
		holder.idTextView.setText(String.valueOf(bean.getId()));
		holder.typeTextView.setText(getType(bean.getType()));
		holder.contenTextView.setText(bean.getContent());
		if (bean.getState() == 0) {
			holder.hot.setVisibility(View.VISIBLE);
		}else{
			holder.hot.setVisibility(View.GONE);
		}
		String date = getDate(bean.getTime());
		holder.timeTextView.setText(date);
		return contentView;
	}

	public static String getDate(String time) {
		String result = time;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
		if (StringTool.isNoBlankAndNoNull(time)) {
			Date date;
			try {
				date = dateFormat.parse(time);
				result = simpleDateFormat.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	class ViewHolder {
		private TextView idTextView;
		private TextView typeTextView;
		private TextView timeTextView;
		private TextView contenTextView;
		private ImageView hot;
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
