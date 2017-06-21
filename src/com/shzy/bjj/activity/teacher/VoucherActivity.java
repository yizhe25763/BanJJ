package com.shzy.bjj.activity.teacher;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.AppointmentPayActivity;
import com.shzy.bjj.adapter.VoucherAdapter;
import com.shzy.bjj.bean.VoucherBean;
import com.shzy.bjj.tools.ToastTool;

/**
 * 我的代金券
 * 
 * @author suntao
 * 
 */
public class VoucherActivity extends BaseActivity implements OnClickListener {
	private ListView listView;
	private VoucherAdapter adapter;
	private List<VoucherBean> data;

	// public static void startActivity(Context context,
	// List<VoucherBean> voucherDataList) {
	// context.startActivity(new Intent(context, VoucherActivity.class)
	// .putParcelableArrayListExtra("data", voucherDataList));
	// }

	@Override
	public int bindLayout() {
		return R.layout.mine_info;
	}

	@Override
	public void initView(View view) {
		action_title.setText(R.string.mine_voucher);
		listView = $(R.id.listview);
		listView.setDivider(null);
	}

	@Override
	public void initData(Context mContext) {
		adapter = new VoucherAdapter(mContext, new GetVoucherData() {

			@Override
			public void onClick(VoucherBean bean, List<VoucherBean> datas,
					int positions, boolean tag) {
				for (int i = 0; i < datas.size(); i++) {
					if (i == positions) {
						if (tag) {
							datas.get(i).setIsSelector(true);
						} else {
							datas.get(i).setIsSelector(false);
						}
					} else {
//						if (tag) {
//							datas.get(i).setIsSelector(true);
//						} else {
							datas.get(i).setIsSelector(false);
//						}
					}

				}
				AppointmentPayActivity.voucherBean = bean;
				AppointmentTeacherPayActivity.voucherBean = bean;
				AppointmentTeacherPayActivity.datas = datas;
				AppointmentPayActivity.datas = datas;
				AppManager.getAppManager().finishActivity();
			}
		});

		data = (List<VoucherBean>) getIntent().getSerializableExtra("data");
		if (data.size() <= 0) {
			$(R.id.no_message).setVisibility(View.VISIBLE);
			return;
		}
		adapter.setData(data);
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);
	}

	@Override
	public void initListener() {
		action_back.setOnClickListener(this);
	}

	@Override
	public void resume() {
	}

	@Override
	public void destroy() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}

}
