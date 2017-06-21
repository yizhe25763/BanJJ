package com.shzy.bjj.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.adapter.ViewPagerAdapter;

/**
 * 
 * @brief 引导界面
 * @author Fanhao.Yi
 * @date 2015年5月4日上午10:59:39
 * @version V1.0
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;

	// 底部小点图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		setCurrentDot(arg0);
	}

	@Override
	public int bindLayout() {
		return R.layout.activity_guide;
	}

	@Override
	public void initView(View view) {
		LayoutInflater inflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		// 初始化引导图片列表
		views.add(inflater.inflate(R.layout.what_new_one, null));
		views.add(inflater.inflate(R.layout.what_new_two, null));
		views.add(inflater.inflate(R.layout.what_new_three, null));
		views.add(inflater.inflate(R.layout.what_new_four, null));
		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views, this);
		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);

	}

	@Override
	public void initData(Context mContext) {

		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态

	}

	@Override
	public void initListener() {
		// 绑定回调
		vp.setOnPageChangeListener(this);

	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

}
