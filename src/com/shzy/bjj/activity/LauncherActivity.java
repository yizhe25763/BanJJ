package com.shzy.bjj.activity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.bean.InfoBean;
import com.shzy.bjj.bean.InfoResponse;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.ClassTag;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.PreferencesTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.view.MainBottomTabLayout;
import com.umeng.onlineconfig.OnlineConfigAgent;

/**
 * 
 * @brief 程序启动页面
 * @author Fanhao.Yi
 * @date 2015年5月4日上午9:43:41
 * @version V1.0
 */
public class LauncherActivity extends BaseActivity {

	public static String registrationId;
	public static String alias;
	/**
	 * 百度地图定位
	 */
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;

	@Override
	public int bindLayout() {
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		return R.layout.activity_launcher;
	}

	@Override
	public void initView(View view) {
		showAnima(view);
		OnlineConfigAgent.getInstance().updateOnlineConfig(getContext());
		// 获取友盟在线参数 ForcedUpdateVersion
		String value = OnlineConfigAgent.getInstance()
				.getConfigParams(this, "ForcedUpdateVersion").toString().trim();
	}

	public void showAnima(View view) {
		// 添加动画效果
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(4000);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 首次登陆跳转至引导页面
				if (PreferencesTool.getBoolean(getContext(), DataTag.IS_FIRST)) {
					TabFragmentActivity.startActivity(getContext(), 0);
				} else {
					PreferencesTool.putBoolean(getContext(), DataTag.IS_FIRST,
							true);
					getIntentTool().forward(ClassTag.GUIDEACTIVITY);

				}
				AppManager.getAppManager().finishActivity();

			}
		});
		view.setAnimation(animation);
	}

	HashSet h = new HashSet();

	@Override
	public void initData(Context mContext) {
		initLocationData();
		if (mApplication.isLogin()) {
			h.add(DataConst.tags);
			JPushInterface.setAliasAndTags(getContext(), DataConst.tags + "_"
					+ PreferencesTool.getLong(getContext(), DataTag.USERID), h,
					new TagAliasCallback() {

						@Override
						public void gotResult(int arg0, String arg1,
								Set<String> arg2) {
							if (arg0 == 0) {
								pushBind(
										mApplication.getLOGIN_KEY(),
										JPushInterface
												.getRegistrationID(getContext()),
										DataConst.tags,
										DataConst.tags
												+ "_"
												+ PreferencesTool.getLong(
														getContext(),
														DataTag.USERID));
							}

						}
					});

		}
	}

	/**
	 * 用户推送绑定
	 * 
	 * @param login_KEY
	 *            身份识别key
	 * @param registrationId2
	 *            推送注册id
	 * @param tags
	 *            标签组 dev（开发环境） ,prod （正式环境）
	 * @param alias2
	 *            别名 开发环境+下划线+用户id
	 */
	private void pushBind(String login_KEY, String registrationId2,
			String tags, String alias2) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_KEY);
		maps.put(DataTag.REGISTRATIONID, registrationId2);
		maps.put(DataTag.TAGS, tags);
		maps.put(DataTag.ALIAS, alias2);
		HttpTool.post(getContext(), Apis.PUSHBIND, loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
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

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null)
				return;
			DataConst.mCity = location.getCity();
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

	private void getInformationData(String loginKey, int page, int size,
			String ids) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.PAGE, page);
		maps.put(DataTag.SIZE, size);
		maps.put(DataTag.IDS, ids);
		HttpTool.post(getContext(), Apis.GET_USER_INFO, loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							InfoResponse infoResponse = JSON.decode(response,
									InfoResponse.class);
							int count = infoResponse.getCount();
							if (count > 0) {
								List<InfoBean> list = infoResponse.getList();
								MainBottomTabLayout.hasMineMessage = true;
							}
						}
						loading.setVisibility(View.GONE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

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

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return true;

	}

	@Override
	public void onBackPressed() {
	}
}
