package com.shzy.bjj.activity.teacher;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.bean.TeacherBean;

public class TeacherViewPagerActivity extends BaseActivity implements
		ViewPager.OnPageChangeListener {
	protected TeacherBean teacherBean;
	protected DisplayImageOptions options;
	protected ImageLoader imageLoader;

	protected int currentIndex = 0;
	protected ViewPager mViewPager;
	protected List<View> pagerList;
	protected View pageOne;
	protected View pageTwo;
	protected LayoutInflater inflater;
	protected Resources resources;

	protected TextView action_title_right;

	@Override
	public int bindLayout() {
		return R.layout.teacher_detail;
	}

	@Override
	public void initView(View view) {
		teacherBean = (TeacherBean) getIntent().getSerializableExtra("bean");
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.mine_head_img)
				.showImageOnFail(R.drawable.mine_head_img).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		resources = this.getResources();
		mViewPager = $(R.id.viewPager);
		mViewPager.setOnPageChangeListener(this);
		pagerList = new ArrayList<View>();
		inflater = LayoutInflater.from(getContext());
		pageOne = inflater.inflate(R.layout.teacher_detail_summarize, null);
		pageTwo = inflater.inflate(R.layout.teacher_detail_data, null);
		pagerList.add(pageOne);
		pagerList.add(pageTwo);
		mViewPager.setAdapter(new ViewPagerAdapter(pagerList));
		mViewPager.setCurrentItem(currentIndex);

		action_title_right = $(R.id.action_title_right);
	}

	@Override
	public void initData(Context mContext) {

	}

	@Override
	public void initListener() {

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {

	}

	@Override
	public void onPageSelected(int i) {
		if (0 == i) {
			action_title.setTextColor(resources.getColor(R.color.white));
			action_title_right.setTextColor(resources
					.getColor(R.color.teacher_title));
		} else {
			action_title
					.setTextColor(resources.getColor(R.color.teacher_title));
			action_title_right.setTextColor(resources.getColor(R.color.white));
		}
		currentIndex = i;
		mViewPager.setCurrentItem(currentIndex);
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

}
