package com.shzy.bjj.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.adapter.CouponMoneyListAdapter;
import com.shzy.bjj.bean.CouponMoneyResponse;
import com.shzy.bjj.bean.VoucherBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;

public class CouponMoneyActivity extends BaseActivity {

	private ListView couponMoneyist;
	private CouponMoneyListAdapter unUseAdapter;
	private List<VoucherBean> data;

	@Override
	public int bindLayout() {
		return R.layout.activity_coupon_money;
	}

	@Override
	public void initView(View view) {
		couponMoneyist = $(R.id.coupon_money);

	}

	@Override
	public void initData(Context mContext) {
		data = new ArrayList<VoucherBean>();
		unUseAdapter = new CouponMoneyListAdapter(getContext(), data);
		couponMoneyist.setAdapter(unUseAdapter);
		if (mApplication.isLogin()) {
			loading.setVisibility(View.VISIBLE);
			getVoucherData(mApplication.getLOGIN_KEY(), 1, 100);
		}

	}

	@Override
	public void initListener() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

	private void getVoucherData(String loginKey, int page, int size) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.PAGE, page);
		maps.put(DataTag.SIZE, size);
		HttpTool.post(getContext(),Apis.GET_USER_VOUCHE,loading, maps, new StringHandler(loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					CouponMoneyResponse beans = JSON.decode(response,
							CouponMoneyResponse.class);
					int count = beans.getCount();
					if (count > 0) {
						List<VoucherBean> list = beans.getList();
						for (VoucherBean oucherBean : list) {
							data.add(oucherBean);
						}
					}
					unUseAdapter.notifyDataSetChanged();
					loading.setVisibility(View.GONE);
				}
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});

	}

}
