package com.shzy.bjj.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.bean.ConfigResponse;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.fragment.base.BaseFragment;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.MainBottomTabLayout;

public class TabFragment extends BaseFragment {
	/**
	 * TabFragment
	 */
	private TabFragmentAdapter mAdapter;
	/**
	 * 首页
	 */
	private HomeFragment mHomeFragment;
	/**
	 * 我的
	 */
	private MineFragment mMineFragment;
	/**
	 * 订单
	 */
	private OrderFragment mOrderFragment;
	/**
	 * 教师资源
	 */
	private TeacherResourcesFragment mResourcesFragment;
	/**
	 * 集合
	 */
	private ArrayList<Fragment> mFragments;
	/**
	 * 文字
	 */
	private ArrayList<String> mTitleList = new ArrayList<String>();
	private int index;
	private ViewPager viewPager;

	private ImageView hot;
	private MainBottomTabLayout bottomTabLayout;

	public TabFragment() {
	}

	public TabFragment(int index) {
		this.index = index;
	}

	@Override
	public int bindLayout() {
		return R.layout.fragment_main;
	}

	@Override
	public void initView(View view) {
		viewPager = $(R.id.tab_pager);
		bottomTabLayout = $(R.id.main_bottom_tablayout);
//		hot = (ImageView) bottomTabLayout.findViewById(R.id.hot);
	}

	@Override
	public void initData(Context mContext) {
		// 初始化配置参数
		getConfig();

		mHomeFragment = new HomeFragment();
		mMineFragment = new MineFragment();
		mOrderFragment = new OrderFragment();
		mResourcesFragment = new TeacherResourcesFragment();
		mFragments = new ArrayList<Fragment>();
		mFragments.add(mHomeFragment);
		mFragments.add(mResourcesFragment);
		mFragments.add(mOrderFragment);
		mFragments.add(mMineFragment);
		mTitleList.add("伴家家");
		mTitleList.add("教师列表");
		mTitleList.add("订单");
		mTitleList.add("我的");
		mAdapter = new TabFragmentAdapter(getFragmentManager());
		viewPager.setOffscreenPageLimit(0);// 设置预加载页面
		viewPager.setAdapter(mAdapter);
		bottomTabLayout.setViewPager(((ViewPager) $(R.id.tab_pager)));
		viewPager.setCurrentItem(index);

	}

	@Override
	public void initListener() {

	}

	class TabFragmentAdapter extends FragmentPagerAdapter {

		public TabFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTitleList.get(position);
		}
	}

	/**
	 * 
	 * 
	 * @param id
	 */
	protected void getConfig() {
		Map maps = new HashMap<String, String>();
		maps = null;
		HttpTool.post(getActivity(), Apis.CLIENT_CONFIG, maps,
				new StringHandler(loading) {
					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							ConfigResponse configResponse = JSON.decode(
									response, ConfigResponse.class);
							if (configResponse != null) {
								DataConst.aggressive = configResponse
										.getDiscount_list().get(0)
										.getAggressive();
								mApplication.saveObject(configResponse,
										DataConst.CONFIG);
							}
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
