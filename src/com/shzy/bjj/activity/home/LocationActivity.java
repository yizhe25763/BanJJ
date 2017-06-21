package com.shzy.bjj.activity.home;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.LauncherActivity.MyLocationListenner;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

public class LocationActivity extends BaseActivity {

	private TextView mAddress;
	private TextView mAddressDebug;

	private TextView mLocationAddress;
	public static boolean isDebug =false;

	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;

	@Override
	public int bindLayout() {
		return R.layout.activity_location;
	}

	@Override
	public void initView(View view) {
		mAddress = $(R.id.address);
		mAddressDebug = $(R.id.address_debug);
		mLocationAddress = $(R.id.location_address);

	}

	@Override
	public void initData(Context mContext) {
		loading.setVisibility(View.VISIBLE);
		initLocationData();
		if(isDebug){
			mAddressDebug.setVisibility(View.VISIBLE);
		}else{
			mAddressDebug.setVisibility(View.GONE);
		}
	}

	@Override
	public void initListener() {
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				AppManager.getAppManager().finishActivity();

			}
		});
		mAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				DataConst.mCity = "西安市";
				AppManager.getAppManager().finishActivity();
			}
		});
		mAddressDebug.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				DataConst.mCity = "上海市";
				AppManager.getAppManager().finishActivity();
			}
		});

	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null)
				return;
			if (StringTool.isNoBlankAndNoNull(location.getCity())) {
				mLocationAddress.setText(location.getCity());
			} else {
				mLocationAddress.setText("未定位到所在城市，请稍后重试");
			}
			loading.setVisibility(View.GONE);

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	public void initLocationData() {
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setAddrType("all");// 获取详细地址需添加此行代码
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

}
