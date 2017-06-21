package com.shzy.bjj.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.WebViewActivity;
import com.shzy.bjj.activity.login.LoginActivity;
import com.shzy.bjj.activity.mine.MineAppointmentActivity;
import com.shzy.bjj.activity.mine.MineBabyListActivity;
import com.shzy.bjj.activity.mine.MineInfoActivity;
import com.shzy.bjj.activity.mine.MineIntegralActivity;
import com.shzy.bjj.activity.mine.MineMamaActivity;
import com.shzy.bjj.activity.mine.MineOpinionActivity;
import com.shzy.bjj.activity.mine.MineVoucherActivity;
import com.shzy.bjj.activity.mine.PitTestActivity;
import com.shzy.bjj.activity.order.EvaluateActivity;
import com.shzy.bjj.bean.InfoBean;
import com.shzy.bjj.bean.InfoResponse;
import com.shzy.bjj.bean.UserBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.fragment.base.BaseFragment;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.CircularImage;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @brief 我的
 * @author Fanhao.Yi
 * @date 2015年5月5日下午4:44:26
 * @version V1.0
 */
public class MineFragment extends BaseFragment implements OnClickListener {
	// 头像
	private CircularImage userImage;
	// 用戶名
	private TextView user_nameTextView;
	private RelativeLayout mineRelativeLayout;
	// 我的代金券
	private TableRow mine_voucherTableRow;
	// 我的信息
	private TableRow mine_infoTableRow;
	// 我的积分
	private TableRow mine_integralTableRow;
	// 预约人信息管理
	private TableRow mine_appointment_manageTableRow;
	// 推荐给好友
	private TableRow mine_shareTableRow;
	// 关于
	private TableRow mine_aboutTableRow;
	// 意见反馈
	private TableRow mine_opinionTableRow;
	// 服务声明
	private TableRow mine_service_declarationTableRow;
	// 宝宝信息管理
	private TableRow mine_babyTableRow;
	private TextView nums;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	@Override
	public int bindLayout() {
		return R.layout.fragment_mines;
	}

	@Override
	public void onResume() {

		super.onResume();
		MobclickAgent.onEvent(getContext(), "A0010");

		if (mApplication.isLogin()) {
			loading.setVisibility(View.GONE);
			getUserInfo(mApplication.getLOGIN_KEY());
			getInformationData(mApplication.getLOGIN_KEY(), 1, 100, "");

			user_nameTextView.setVisibility(View.VISIBLE);
		} else {
			nums.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
			userImage.setImageResource(R.drawable.mine_head_img);
			user_nameTextView.setText(null);
		}
	}

	private List<InfoBean> list;

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
						list.clear();
						if (StringTool.isNoBlankAndNoNull(response)) {
							InfoResponse infoResponse = JSON.decode(response,
									InfoResponse.class);
							int count = infoResponse.getCount();
							if (count > 0) {
								int num = 0;
								for (int i = 0; i < count; i++) {
									if (infoResponse.getList().get(i)
											.getState() == 0) {
										num++;
									}

								}
								if(num==0){
									nums.setVisibility(View.GONE);
								}else{
									nums.setVisibility(View.VISIBLE);
									nums.setText(num+"");
								}
							
							} else {
								$(R.id.no_message).setVisibility(View.VISIBLE);

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
	public void initView(View view) {

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.mine_head_img)
				.showImageOnFail(R.drawable.mine_head_img).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		// 初始化标题
		action_title.setText(R.string.mine_title_content);
		// 隐藏左侧按钮
		action_back.setVisibility(View.INVISIBLE);
		mineRelativeLayout = $(R.id.mine);
		userImage = $(R.id.head_img);
		user_nameTextView = $(R.id.user_name);
		// user_nameTextView.setVisibility(View.GONE);
		mine_voucherTableRow = $(R.id.mine_voucher_table);
		mine_infoTableRow = $(R.id.mine_info);
		mine_integralTableRow = $(R.id.mine_integral);
		mine_appointment_manageTableRow = $(R.id.mine_appointment_manage);
		mine_aboutTableRow = $(R.id.mine_about);
		mine_shareTableRow = $(R.id.mine_share);
		mine_opinionTableRow = $(R.id.mine_opinion);
		mine_service_declarationTableRow = $(R.id.mine_service_declaration);
		mine_babyTableRow = $(R.id.mine_baby_message);
		nums = $(R.id.comment_count);

	}

	@Override
	public void initData(Context mContext) {
		list = new ArrayList<InfoBean>();

	}

	@Override
	public void initListener() {
		mineRelativeLayout.setOnClickListener(this);
		userImage.setOnClickListener(this);
		mine_voucherTableRow.setOnClickListener(this);
		mine_infoTableRow.setOnClickListener(this);
		mine_integralTableRow.setOnClickListener(this);
		mine_appointment_manageTableRow.setOnClickListener(this);
		mine_aboutTableRow.setOnClickListener(this);
		mine_shareTableRow.setOnClickListener(this);
		mine_opinionTableRow.setOnClickListener(this);
		mine_service_declarationTableRow.setOnClickListener(this);
		mine_babyTableRow.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mine_baby_message:
			if (!mApplication.isLogin())
				LoginActivity.startActivity(getContext());
			else
				MineBabyListActivity.startActivity(getContext());
			break;
		case R.id.mine:
		case R.id.head_img:
			if (!mApplication.isLogin())
				LoginActivity.startActivity(getContext());
			else
				MineMamaActivity.startActivity(getContext());
			break;

		case R.id.mine_voucher_table:
			// 我的代金券
			if (!mApplication.isLogin())
				LoginActivity.startActivity(getContext());
			else
				MineVoucherActivity.startActivity(getContext());
			break;
		case R.id.mine_info:
			if (!mApplication.isLogin())
				LoginActivity.startActivity(getContext());
			else
				MineInfoActivity.startActivity(getContext());
			break;
		case R.id.mine_integral:
			if (!mApplication.isLogin())
				LoginActivity.startActivity(getContext());
			else
				MineIntegralActivity.startActivity(getContext());
			break;
		case R.id.mine_appointment_manage:
			if (!mApplication.isLogin())
				LoginActivity.startActivity(getContext());
			else
				MineAppointmentActivity.startActivity(getContext());
			break;
		case R.id.mine_about:
			WebViewActivity.startActivity(getContext(),
					"http://www.banjiajia.cn/aboutus.html", 0, "关于我们");
			break;
		case R.id.mine_share:

			break;
		case R.id.mine_opinion:
			MineOpinionActivity.startActivity(getContext());
			break;
		case R.id.mine_service_declaration:
			 WebViewActivity.startActivity(getContext(), DataConst.URLS, 0,
			 "服务声明");
//			EvaluateActivity.startActivity(getContext(), null);
			break;
		default:
			break;
		}
	}

	private void getUserInfo(String loginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);

		HttpTool.post(getActivity(), Apis.GET_USER, maps, new StringHandler(
				loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					UserBean userBean = JSON.decode(response, UserBean.class);
					if (userBean != null) {
						setViewData(userBean);
					}
				}
				loading.setVisibility(View.GONE);
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});
	}

	private void setViewData(UserBean userBean) {
		user_nameTextView.setText(userBean.getPhone().toString().trim());
		String imageUrl = userBean.getPic_url();
		if (StringTool.isNoBlankAndNoNull(imageUrl)) {
			imageLoader.displayImage(imageUrl, userImage, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							userImage
									.setImageResource(R.drawable.mine_head_img);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

						}
					}, null);
		} else {
			userImage.setImageResource(R.drawable.mine_head_img);
		}
	}
}
