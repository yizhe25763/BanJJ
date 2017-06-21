package com.shzy.bjj.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.TabFragmentActivity;
import com.shzy.bjj.activity.WebViewActivity;
import com.shzy.bjj.activity.home.AppointmentTimeActivity;
import com.shzy.bjj.activity.home.CallPhoneActivity;
import com.shzy.bjj.activity.home.LocationActivity;
import com.shzy.bjj.bean.AdsResult;
import com.shzy.bjj.bean.InfoBean;
import com.shzy.bjj.bean.InfoResponse;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.fragment.base.BaseFragment;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.PreferencesTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.ADInfo;
import com.shzy.bjj.view.CycleViewPager;
import com.shzy.bjj.view.MainBottomTabLayout;
import com.shzy.bjj.view.CycleViewPager.ImageCycleViewListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 
 * @brief 首页 banner的点击事件实现在BannerImagePageAdapter中
 * @author Fanhao.Yi
 * @date 2015年5月5日下午3:02:41
 * @version V1.0
 */

@SuppressLint("InflateParams")
public class HomeFragment extends BaseFragment implements OnClickListener {

	private ImageButton locationImage;
	private TextView titleTxt;
	private TextView callPhone;
	private TextView mLocationAddress;

	private List<ImageView> views = new ArrayList<ImageView>();
	private CycleViewPager cycleViewPager;
	private List<ADInfo> infos = new ArrayList<ADInfo>();

	private String learningURL = "http://page.banjiajia.cn/course";
	// private String welfareURL = "http://page.banjiajia.cn/fuli";
	private String welfareURL = "http://test8.eachbaby.com/fuli/";

	//

	/**
	 * 判断是否需要更新 0 不存在更新 1强制更新 2非强制更新
	 */
	private int isUpdate = 0;
	private Context mContext;

	private String appVersion;
	private String onLineConfing;
	private String value = "";

	@Override
	public int bindLayout() {
		return R.layout.fragment_home;
	}

	@Override
	public void initView(View view) {
		locationImage = $(R.id.action_back);
		titleTxt = $(R.id.action_title);
		callPhone = $(R.id.action_right);
		mLocationAddress = $(R.id.location);
		cycleViewPager = (CycleViewPager) getActivity().getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);

	}

	@SuppressLint("NewApi")
	@Override
	public void initData(Context mContext) {
		// 初始化标题
		initBanner();
		configImageLoader();
		// initialize();
		OnlineConfigAgent.getInstance().updateOnlineConfig(getActivity());
		OnlineConfigAgent.getInstance().setDebugMode(true);
		// 获取友盟在线参数 ForcedUpdateVersion
		value = OnlineConfigAgent.getInstance()
				.getConfigParams(getActivity(), "ForcedUpdateVersion")
				.toString().trim();
		// 获取渠道号
		Bundle bundle = MyApplication.getMetaData(getContext());
		String channel = String.valueOf(bundle.get("UMENG_CHANNEL"));
		if (!StringTool.isNoBlankAndNoNull(channel)) {
			channel = "";
		}
		HttpTool.channelID = channel;
		// 获取本机版本号
		try {
			appVersion = getActivity().getPackageManager().getPackageInfo(
					getActivity().getPackageName(), 0).versionName;
			HttpTool.APPVERSION_VALUES = appVersion;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (StringTool.isNoBlankAndNoNull(value)
				&& StringTool.isNoBlankAndNoNull(appVersion)) {
			if (Integer.parseInt(channelVersion(appVersion)) < Integer
					.parseInt(channelVersion(value))) {// 强制更新
				isUpdate = 1;
			} else {// 非强制更新S
				isUpdate = 2;
			}
		}
		if (PreferencesTool.getBoolean(getActivity(), "forcedUpdating")) {
		} else {
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			UmengUpdateAgent.forceUpdate(getActivity());
			UmengUpdateAgent.update(getActivity());
		}
		/**
		 * 监听检测更新的结果
		 */
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes:
					// UmengUpdateAgent
					// .showUpdateDialog(getActivity(), updateInfo);
					break;
				case UpdateStatus.No:
					break;

				}
			}
		});
		/**
		 * 监听对话框按键操作
		 */
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

			@Override
			public void onClick(int status) {
				switch (status) {
				case UpdateStatus.Update:
					break;
				case UpdateStatus.NotNow:
					if (isUpdate == 1) {
						Toast.makeText(getActivity(), "请更新最新版本后使用",
								Toast.LENGTH_SHORT).show();
						PreferencesTool.putBoolean(getActivity(),
								"forcedUpdating", false);
						AppManager.getAppManager().AppExit(getActivity());
					} else if (isUpdate == 2) {
						PreferencesTool.putBoolean(getActivity(),
								"forcedUpdating", true);
					}

					break;
				}
			}
		});

	}

	private String channelVersion(String value) {
		String str = value.substring(0, 1);
		String str1 = value.substring(2, 3);
		String str2 = value.substring(4, 5);
		StringBuffer strs = new StringBuffer();
		strs.append(str);
		strs.append(str1);
		strs.append(str2);
		return strs.toString().trim();
	}

	/**
	 * 初始化广告数据
	 */
	private void initBanner() {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.APPBM, "2");
		maps.put(DataTag.ADPOSITIONBM, "3002");
		maps.put(DataTag.UID, 0);

		HttpTool.bannerPost(getActivity(), maps, new StringHandler(loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					AdsResult result = JSON.decode(response, AdsResult.class);
					if (result != null) {
						int count = result.getCount();
						if (result.getCarousel_value() > 0) {
							DataConst.BannerTime = result.getCarousel_value() * 1000;
						}
						if (count > 0) {
							int length = result.getAds().size();
							infos.clear();
							views.clear();
							for (int i = 0; i < length; i++) {
								ADInfo info = new ADInfo();
								info.setUrl(result.getAds().get(i).getPic_url());
								info.setPage_url(result.getAds().get(i)
										.getPage_url());
								infos.add(info);
							}
							views.add(getImageView(getActivity()
									.getApplicationContext(),
									infos.get(infos.size() - 1).getUrl()));
							for (int i = 0; i < infos.size(); i++) {
								views.add(getImageView(getActivity()
										.getApplicationContext(), infos.get(i)
										.getUrl()));
							}
							MobclickAgent.onEvent(getContext(), "D0001");
							views.add(getImageView(getActivity()
									.getApplicationContext(), infos.get(0)
									.getUrl()));
							int size = infos.size();
							if (size <= 1) {
								cycleViewPager.setScrollable(false);
								cycleViewPager.setCycle(false);
								cycleViewPager.setWheel(false);
							} else {
								cycleViewPager.setScrollable(true);
								cycleViewPager.setCycle(true);
								cycleViewPager.setWheel(true);
							}
							cycleViewPager.setData(views, infos,
									mAdCycleViewListener);
							cycleViewPager.setTime(result.getCarousel_value() * 1000);
							cycleViewPager.setIndicatorCenter();

						}
					}
				}
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});

	}

	@SuppressLint("NewApi")
	private void initialize() {
	}

	private static ImageView getImageView(Context context, String url) {
		ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		ImageLoader.getInstance().displayImage(url, imageView);
		return imageView;
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				MobclickAgent.onEvent(getContext(), "D0001");
				WebViewActivity.startActivity(getContext(), info.getPage_url(),
						0);
			}
		}

	};

	/**
	 * 配置ImageLoder
	 */
	private void configImageLoader() {
		// 初始化ImageLoader
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity().getApplicationContext())
				.defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void initListener() {
		/**
		 * 拨打电话
		 */
		$(R.id.home_learning).setOnClickListener(this);
		$(R.id.home_order).setOnClickListener(this);
		$(R.id.home_teach).setOnClickListener(this);
		$(R.id.home_welfare).setOnClickListener(this);
		$(R.id.home_personal).setOnClickListener(this);
		locationImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent().setClass(getActivity(),
						LocationActivity.class));
			}
		});
		callPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CallPhoneActivity.startActivity(getActivity());
			}
		});
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
		mLocationAddress.setText(DataConst.mCity);
		MobclickAgent.onEvent(getContext(), "A0007");
		if (mApplication.isLogin()) {
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 约起来
		case R.id.home_order:
			getIntentTool().forward(AppointmentTimeActivity.class);
			MobclickAgent.onEvent(getContext(), "A0001");
			break;
		// 早教团队
		case R.id.home_teach:
			TabFragmentActivity.startActivity(getContext(), 1);
			MobclickAgent.onEvent(getActivity(), "A0004");

			break;
		// 私人订制
		case R.id.home_personal:
			ToastTool.toastMessage(getActivity(), R.string.home_learns);
			MobclickAgent.onEvent(getContext(), "A0002");

			break;
		// 课程介绍
		case R.id.home_learning:
			WebViewActivity.startActivity(getContext(), learningURL, 0, "课程体系");
			MobclickAgent.onEvent(getContext(), "A0003");

			break;
		// 蹭福利
		case R.id.home_welfare:
			WebViewActivity.startActivity(getContext(), welfareURL, 0);
			MobclickAgent.onEvent(getContext(), "A0005");
			break;

		default:
			break;
		}
	}

}
