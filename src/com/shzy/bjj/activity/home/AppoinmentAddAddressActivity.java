package com.shzy.bjj.activity.home;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.map.MyPoiOverlay;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

/**
 * 地图自动补全页面
 * 
 */
public class AppoinmentAddAddressActivity extends BaseActivity {

	private AutoCompleteTextView edittext_search;
	MKPlanNode stNode;
	MKSearch mSearch;
	private MapView mMapView;
	private View btn_search;
	ArrayList<MKPoiInfo> list_MKPoiInfo = new ArrayList<MKPoiInfo>();
	String[] arr_MKPoiInfo = new String[] {};
	private ArrayAdapter<String> adapter;
	private int resultCode = 0;
	private double lat;
	private double log;
	// LocationClient mLocClient;
	// public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	/**
	 * 地址信息
	 */
	private String mAddress;
	/**
	 * 城市
	 */
	private String mCityName;

	private String address;

	public void searchButtonProcess(View v) {
		mSearch.poiSearchInCity(DataConst.mCity, edittext_search.getText().toString());
	}

	@Override
	public int bindLayout() {
		return R.layout.baidu_daohang_new;
	}

	@Override
	public void initView(View view) {
		mMapView = (MapView) findViewById(R.id.bmapView);
		edittext_search = (AutoCompleteTextView) findViewById(R.id.edittext_search);
	}

	@Override
	public void initData(Context mContext) {
		action_title.setText("服务地点");
		action_right.setText("保存");
		address = getIntent().getStringExtra("address");
		// edittext_search.setText(address);
		MyApplication app = (MyApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(getApplicationContext());
			app.mBMapManager.init(new MyApplication.MyGeneralListener());
		}
		mSearch = new MKSearch();
		stNode = new MKPlanNode();
		stNode.name = edittext_search.getText().toString();

		mSearch.init(app.mBMapManager, new MKSearchListener() {

			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,
					int arg1) {

			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0,
					int arg1) {

			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
					int arg2) {

			}

			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				try {
					if (error != 0 || res == null) {
						list_MKPoiInfo.clear();
						show();
						return;
					}
					if (res.getCurrentNumPois() > 0) {
						MyPoiOverlay poiOverlay = new MyPoiOverlay(
								AppoinmentAddAddressActivity.this, mMapView,
								mSearch);
						poiOverlay.setData(res.getAllPoi());
						System.err.println(res.getAllPoi());
						list_MKPoiInfo.clear();
						for (MKPoiInfo info : res.getAllPoi()) {
							if (info.ePoiType == 0) {
								list_MKPoiInfo.add(info);
							}
						}
						mMapView.getOverlays().clear();
						mMapView.getOverlays().add(poiOverlay);
						mMapView.refresh();
						for (MKPoiInfo info : res.getAllPoi()) {
							if (info.pt != null) {
								mMapView.getController().animateTo(info.pt);
								lat = info.pt.getLatitudeE6() / 1E6;
								log = info.pt.getLongitudeE6() / 1E6;
								mAddress = info.address + "," + info.name;
								break;
							}
						}
						show();
					} else if (res.getCityListNum() > 0) {
						String strInfo = "在";
						for (int i = 0; i < res.getCityListNum(); i++) {
							strInfo += res.getCityListInfo(i).city;
							strInfo += ",";

						}
						strInfo += "找到结果";
						Toast.makeText(AppoinmentAddAddressActivity.this,
								strInfo, Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			}

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {

			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {

			}

			@Override
			public void onGetAddrResult(MKAddrInfo res, int error) {

			}
		});

		mSearch.setPoiPageCapacity(30);
	}

	@Override
	public void initListener() {
		action_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				if (StringTool.isNoBlankAndNoNull(mAddress)) {
					Intent intent = new Intent();
					intent.putExtra("address", mAddress);
					intent.putExtra("lat", lat);
					intent.putExtra("log", log);
					setResult(resultCode, intent);
					AppManager.getAppManager().finishActivity();
				}

			}
		});
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				AppManager.getAppManager().finishActivity();
			}
		});
		edittext_search.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				if (ButtonTool.isFastClick()) {
					return;
				}
			}
		});
		edittext_search.addTextChangedListener(new TextWatcher() {

			private ArrayAdapter<String> adapter;

			public void afterTextChanged(Editable editable) {

				if (adapter != null) {
					adapter.notifyDataSetChanged();
					adapter.notifyDataSetInvalidated();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				searchButtonProcess(edittext_search);

			}

		});
	}

	protected void show() {
		if (list_MKPoiInfo == null || list_MKPoiInfo.isEmpty()) {
			arr_MKPoiInfo = new String[] {};
		} else {
			final int size = list_MKPoiInfo.size();
			arr_MKPoiInfo = new String[size];
			for (int i = 0; i < list_MKPoiInfo.size(); i++) {
				arr_MKPoiInfo[i] = list_MKPoiInfo.get(i).name + "\n"
						+ list_MKPoiInfo.get(i).address;
			}
		}

		adapter = new ArrayAdapter<String>(AppoinmentAddAddressActivity.this,
				R.layout.item_address, R.id.contentTextView, arr_MKPoiInfo) {

			private Filter f;

			@Override
			public Filter getFilter() {
				if (f == null) {
					f = new Filter() {

						@Override
						protected synchronized FilterResults performFiltering(
								CharSequence c) {
							ArrayList<Object> suggestions = new ArrayList<Object>();
							for (String adr : arr_MKPoiInfo) {
								suggestions.add(adr);

							}
							FilterResults filterResults = new FilterResults();
							filterResults.values = suggestions;
							filterResults.count = suggestions.size();
							return filterResults;
						}

						@Override
						protected synchronized void publishResults(
								CharSequence c, FilterResults results) {
							if (results.count > 0) {
								adapter.notifyDataSetChanged();
							} else {
								adapter.notifyDataSetInvalidated();
							}

						}

					};
				}
				return f;
			}

		};
		edittext_search.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		adapter.notifyDataSetInvalidated();

	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

}
