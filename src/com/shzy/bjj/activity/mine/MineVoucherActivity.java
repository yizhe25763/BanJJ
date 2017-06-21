package com.shzy.bjj.activity.mine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.adapter.VoucherListAdapter;
import com.shzy.bjj.bean.CouponMoneyResponse;
import com.shzy.bjj.bean.VoucherBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.DateTimeTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.zxing.CaptureActivity;
import com.shzy.bjj.zxing.DecodeThread;

/**
 * 我的代金券
 * 
 * @author suntao
 * 
 */
public class MineVoucherActivity extends BaseActivity implements
		OnClickListener, ViewPager.OnPageChangeListener {

	private int currentIndex = 0;
	private int tabWidth;
	private ViewPager mViewPager;
	private ImageView mIvTabIndicator;
	private List<View> pagerList;
	private View pageOne;
	private View pageTwo;
	private LayoutInflater inflater;

	// 未使用
	private TextView unUsedLayout;
	private ListView listView_one;
	private VoucherListAdapter unUseAdapter;

	// 已使用
	private TextView usedLayout;
	private ListView listView_two;
	private VoucherListAdapter usedAdapter;

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, MineVoucherActivity.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_voucher;
	}

	@Override
	public void initView(View view) {
		action_title.setText(R.string.mine_voucher);
		unUsedLayout = $(R.id.voucher_unuse);
		usedLayout = $(R.id.voucher_used);

		mIvTabIndicator = $(R.id.iv_indicator);
		mViewPager = $(R.id.viewPager);
		mViewPager.setOnPageChangeListener(this);

		pagerList = new ArrayList<View>();
		inflater = LayoutInflater.from(this);
		pageOne = inflater.inflate(R.layout.listview, null);
		pageTwo = inflater.inflate(R.layout.listview, null);
		pagerList.add(pageOne);
		pagerList.add(pageTwo);
		mViewPager.setAdapter(new ViewPagerAdapter(pagerList));
		mViewPager.setCurrentItem(currentIndex);

		// action_right.setVisibility(View.GONE);
		action_right.setText("兑换");
		setTabWidth();
	}

	@Override
	public void initData(Context mContext) {
		listView_one = (ListView) pageOne.findViewById(R.id.listview);
		listView_two = (ListView) pageTwo.findViewById(R.id.listview);
		unUseAdapter = new VoucherListAdapter(mContext);
		usedAdapter = new VoucherListAdapter(mContext);
		listView_one.setAdapter(unUseAdapter);
		listView_two.setAdapter(usedAdapter);
//		Bundle extras = getIntent().getExtras();
//		if (null != extras) {
//			int width = extras.getInt("width");
//			int height = extras.getInt("height");
//			String result = extras.getString("result");
//			ToastTool.toastMessage(mContext, result + "");
//		}
	}

	@Override
	public void initListener() {
		unUsedLayout.setOnClickListener(this);
		usedLayout.setOnClickListener(this);
		action_back.setOnClickListener(this);
		action_right.setOnClickListener(this);
	}

	/**
	 * 根据屏幕的宽度，初始化Tab指示器的宽度
	 */
	private void setTabWidth() {
		DisplayMetrics outMetrics = new DisplayMetrics();
		getContext().getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(outMetrics);
		tabWidth = outMetrics.widthPixels / 4;
		setTabIndicatorLeftMargin();
	}

	/**
	 * 设置Tab指示器左边的偏移量
	 */
	private void setTabIndicatorLeftMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIvTabIndicator
				.getLayoutParams();
		lp.width = tabWidth;
		if (currentIndex == 0) {
			lp.leftMargin = tabWidth / 2;
		} else {
			lp.leftMargin = tabWidth / 2 + 2 * tabWidth;
		}
		mIvTabIndicator.setLayoutParams(lp);
	}

	@Override
	public void resume() {
		if (mApplication.isLogin()) {
			loading.setVisibility(View.VISIBLE);
			getVoucherData(mApplication.getLOGIN_KEY(), 1, 100);
		} else {
			loading.setVisibility(View.GONE);
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.voucher_unuse:
			currentIndex = 0;
			setTabIndicatorLeftMargin();
			mViewPager.setCurrentItem(currentIndex);
			break;

		case R.id.voucher_used:
			currentIndex = 1;
			setTabIndicatorLeftMargin();
			mViewPager.setCurrentItem(currentIndex);
			break;

		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.action_right:
			Intent intent = new Intent(this, CaptureActivity.class);
			// Intent intent = new Intent(this, MineVoucherDialog.class);
			startActivity(intent);
		default:
			break;
		}
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {

	}

	@Override
	public void onPageSelected(int i) {
		currentIndex = i;
		setTabIndicatorLeftMargin();
		mViewPager.setCurrentItem(currentIndex);
		if (currentIndex == 0) {
			unUsedLayout.setTextColor(getResources().getColor(
					R.color.voucher_h_color));
			usedLayout.setTextColor(getResources().getColor(
					R.color.voucher_n_color));
		} else {
			unUsedLayout.setTextColor(getResources().getColor(
					R.color.voucher_n_color));
			usedLayout.setTextColor(getResources().getColor(
					R.color.voucher_h_color));
		}
	}

	@Override
	public void onPageScrollStateChanged(int i) {
	}

	public class ViewPagerAdapter extends PagerAdapter {
		private List<View> listView;

		public ViewPagerAdapter() {
		}

		public ViewPagerAdapter(List<View> listViews) {
			this.listView = listViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(listView.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(listView.get(position), 0);
			return listView.get(position);
		}

		@Override
		public int getCount() {
			return listView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	private void getVoucherData(String loginKey, int page, int size) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.PAGE, page);
		maps.put(DataTag.SIZE, size);
		HttpTool.post(getContext(), Apis.GET_USER_VOUCHE,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							List<VoucherBean> data1 = new ArrayList<VoucherBean>();
							List<VoucherBean> data2 = new ArrayList<VoucherBean>();
							CouponMoneyResponse voucherResponse = JSON.decode(
									response, CouponMoneyResponse.class);
							int count = voucherResponse.getCount();
							if (count > 0) {
								// 获取当前时间
								Date currentTime = DateTimeTool
										.gainCurrentDate();
								List<VoucherBean> list = voucherResponse
										.getList();
								for (VoucherBean data : list) {
									int status = data.getStatus();
									// 代金券过期时间
									String expireTime = data.getExpire_time();
									Date expireDate = DateTimeTool
											.parseDate(expireTime);
									if (DateTimeTool.compareDate(currentTime,
											expireDate)) {// 未过期
										if (status == 1) {
											data1.add(data);
										} else {
											data2.add(data);
										}
									} else {
										// 已过期
										data2.add(data);
									}

								}
							} else {
								$(R.id.no_message).setVisibility(View.VISIBLE);
							}
							unUseAdapter.setData(data1);
							unUseAdapter.notifyDataSetChanged();

							usedAdapter.setData(data2);
							usedAdapter.notifyDataSetChanged();
							loading.setVisibility(View.GONE);
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}
}
