package com.shzy.bjj.activity.teacher;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.adapter.HorizontalGrideAdapter;
import com.shzy.bjj.adapter.TeacherResumeAdapter;
import com.shzy.bjj.bean.TeacherDetailBean;
import com.shzy.bjj.view.CustomListView;
import com.shzy.bjj.view.HorizontalGrideView;

/**
 * 老师资料
 * 
 * @author suntao
 * 
 */
public class TeacherDataActivity extends TeacherViewPagerActivity {
	protected TextView teacher_detail_introduction;
	protected CustomListView listview_resume;
	protected com.shzy.bjj.view.MyGallery grideView;
	private int dayLengths = 3;
	// 屏幕宽度（像素）
	private int screenWidth;

	@Override
	public void initView(View view) {
		super.initView(view);
		teacher_detail_introduction = (TextView) pageTwo
				.findViewById(R.id.teacher_detail_introduction);
		listview_resume = (CustomListView) pageTwo
				.findViewById(R.id.listview_resume);
		grideView = (com.shzy.bjj.view.MyGallery) pageTwo
				.findViewById(R.id.grideview);
		teacher_detail_introduction.setFocusable(true);
		teacher_detail_introduction.setFocusableInTouchMode(true);
		grideView.setFocusable(false);
		grideView.setFocusableInTouchMode(false);
		grideView.setSpacing(15);
	}

	protected void setTeacherDataDetail(TeacherDetailBean bean) {
		teacher_detail_introduction.setText(bean.getTeacher_descb());
		// 教师履历
		if (bean.getResume_count() > 0) {
			TeacherResumeAdapter adapter = new TeacherResumeAdapter(this);
			adapter.setData(bean.getResume_ist());
			adapter.notifyDataSetChanged();
			listview_resume.setAdapter(adapter);
		}
		// 教师证书
		if (bean.getProfile_count() > 0) {
			HorizontalGrideAdapter adapter = new HorizontalGrideAdapter(this,
					imageLoader);
			adapter.setData(bean.getProfile_ist());
			adapter.notifyDataSetChanged();
			grideView.setAdapter(adapter);
		}

	}

	@Override
	public void initData(Context mContext) {
		super.initData(mContext);

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

}
