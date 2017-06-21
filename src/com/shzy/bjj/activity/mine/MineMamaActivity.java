package com.shzy.bjj.activity.mine;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.adapter.BabyListAdapter;
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.bean.OrderBean;
import com.shzy.bjj.bean.TaskDayOrderBean;
import com.shzy.bjj.bean.UserBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.DialogUtils;
import com.shzy.bjj.tools.FileUtil;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.PreferencesTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.CircularImage;
import com.shzy.bjj.view.CustomListView;

public class MineMamaActivity extends BaseActivity implements OnClickListener {
	private TableRow mine_detail_baby_add;
	private CircularImage mamaCircularImage;
	private EditText mine_detail_realname_name;
	private EditText mine_detail_number_name;
	private EditText mine_detail_phone_name;

	private CustomListView listView;
	private BabyListAdapter adapter;
	private Button mama_exchange;

	private File cameraFile;
	private File cropFile;
	private String base64 = "";

	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	public static boolean isRefresh = false;

	private String name;
	private String Identity;
	private String phone;

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, MineMamaActivity.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_mama;
	}

	@Override
	public void initView(View view) {
		isRefresh = false;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.mine_head_img)
				.showImageOnFail(R.drawable.mine_head_img).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		cropFile = new File(Environment.getExternalStorageDirectory(), "jpg");
		action_title.setText(R.string.mine_detail_title);
		action_right.setText(R.string.mine_detail_edit);
		mine_detail_baby_add = $(R.id.mine_detail_baby_add);
		mamaCircularImage = $(R.id.head_img);
		mine_detail_realname_name = $(R.id.mine_detail_realname_name);
		mine_detail_number_name = $(R.id.mine_detail_number_name);
		mine_detail_phone_name = $(R.id.mine_detail_phone_name);

		listView = $(R.id.listview);
		adapter = new BabyListAdapter(this, imageLoader,false);
		listView.setAdapter(adapter);
		mama_exchange = $(R.id.mama_exchange);
	}

	@Override
	public void initData(Context mContext) {
		mama_exchange.setVisibility(View.GONE);
		if (mApplication.isLogin()) {
			if (!HttpTool.checkNetwork(getContext())) {
				loading.setVisibility(View.GONE);
				return;
			}
			loading.setVisibility(View.VISIBLE);
			isRefresh = false;
			getUserInfo(mApplication.getLOGIN_KEY());
		} else {
			loading.setVisibility(View.GONE);
		}
		action_right.setText("保存");
	}

	@Override
	public void initListener() {
		mine_detail_number_name.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mine_detail_number_name.setText("");
				}
				return false;
			}
		});
		// mine_detail_number_name.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// mine_detail_number_name.setText("");
		// }
		// });
		action_back.setOnClickListener(this);
		action_right.setOnClickListener(this);
		mamaCircularImage.setOnClickListener(this);
		mine_detail_baby_add.setOnClickListener(this);
		mama_exchange.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				TextView baby_detail_name = (TextView) view
						.findViewById(R.id.baby_detail_name);
				BabyBean bean = (BabyBean) baby_detail_name.getTag();
				if (bean != null) {
					MineBabyActivity.startActivity(MineMamaActivity.this,
							false, bean);
				}

			}
		});
	}

	@Override
	public void resume() {

		if (mApplication.isLogin() && isRefresh) {
			if (!HttpTool.checkNetwork(getContext())) {
				loading.setVisibility(View.GONE);
				return;
			}
			isRefresh = false;
			loading.setVisibility(View.VISIBLE);
			getUserInfo(mApplication.getLOGIN_KEY());
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.action_right:
			String name = mine_detail_realname_name.getText().toString().trim();
			// String numbere = mine_detail_number_name.getText().toString()
			// .trim();
			// if (numbere.contains("*")) {
			// numbere = Identity;
			// }
			// if (numbere.length() > 0 && numbere.length() != 18) {
			// ToastTool.toastMessage(getContext(), "请输入正确的身份证号码");
			// return;
			// } else {
			// numbere = "";
			// }
			if (!StringTool.isNoBlankAndNoNull(name)) {
				ToastTool.toastMessage(getContext(), "请填写真实姓名");
				return;
			}
			loading.setVisibility(View.VISIBLE);
			modifyUserInfo(mApplication.getLOGIN_KEY(), base64, name, "", 0, "");
			break;

		case R.id.head_img:
			cameraFile = DialogUtils.showImageDialog(this);
			break;
		case R.id.head_img_baby:

			break;
		case R.id.mine_detail_baby_add:
			MineBabyActivity.startActivity(this, true, null);
			break;

		case R.id.mama_exchange:
			LoginOutActivity.startActivity(getContext());
			// logout(mApplication.getLOGIN_KEY());
			break;
		default:
			break;
		}
	}

	public void logout() {
		mApplication.setLogin(false);
		mApplication.setLOGIN_KEY("");
		PreferencesTool.putString(this, DataTag.LOGINKEY, "");
		AppManager.getAppManager().finishActivity();
	}

	/**
	 * 退出登录
	 * 
	 * @param loginKey
	 */
	private void logout(String loginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);

		HttpTool.post(getContext(), Apis.USER_LOGOUT, loading,maps, new StringHandler(
				null) {

			@Override
			public void success(String response) {
				logout();
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});
	}

	private void getUserInfo(String loginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);

		HttpTool.post(getContext(), Apis.GET_USER,loading, maps, new StringHandler(
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
		name = userBean.getName();
		if (StringTool.isNoBlankAndNoNull(name)) {
			mine_detail_realname_name.setText(name);
			mine_detail_realname_name.setSelection(name.length());
		}
		// Identity = userBean.getIdentity();
		// if (StringTool.isNoBlankAndNoNull(Identity)) {
		// mine_detail_number_name.setText(Identity.substring(0, 6)
		// + "************");
		// }
		phone = userBean.getPhone();
		if (StringTool.isNoBlankAndNoNull(userBean.getPhone())) {
			mine_detail_phone_name.setText(phone.substring(0, 4) + "*******");
		}
		String imageUrl = userBean.getPic_url();
		if (StringTool.isNoBlankAndNoNull(imageUrl)) {
			imageLoader.displayImage(imageUrl, mamaCircularImage, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							mamaCircularImage
									.setImageResource(R.drawable.mine_head_img);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

						}
					}, null);
		} else {
			mamaCircularImage.setImageResource(R.drawable.mine_head_img);
		}
		List<BabyBean> list = userBean.getBaby_list();
		if (list != null) {
			adapter.setData(list);
			adapter.notifyDataSetChanged();
		}
		mama_exchange.setVisibility(View.VISIBLE);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param loginKey
	 * @param pic
	 *            ：头像图片数据（ jpg 文件，数据格式是经过 base64 编码的图片数据）
	 */
	private void modifyUserInfo(String loginKey, String pic, String name,
			String identity, int sex, String city) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put("pic", pic);
		maps.put("name", name);
		maps.put("identity", identity);
		maps.put("sex", sex);
		maps.put("city", city);
		HttpTool.post(getContext(), Apis.MODIFY_USER,loading, maps, new StringHandler(
				loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					ToastTool.toastMessage(MineMamaActivity.this, "修改成功");
					AppManager.getAppManager().finishActivity();
				}
				loading.setVisibility(View.GONE);
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 拍照
		
		if (requestCode == DialogUtils.REQUEST_CODE_CAMERA
				&& FileUtil.isValid(cameraFile) && resultCode == RESULT_OK) {
			Log.e("DialogUtils.REQUEST_CODE_CAMERA", cameraFile+"");
			DialogUtils.startPhotoCrop(this, Uri.fromFile(cameraFile), 80,
					cropFile);
			cameraFile = null;
			// 系统图片库-选择图片
		} else if (requestCode == DialogUtils.REQUEST_CODE_IMAGE
				&& data != null && resultCode == RESULT_OK) {
			DialogUtils.startPhotoCrop(this, data.getData(), 80, cropFile);
			// 裁剪图片
		} else if (requestCode == DialogUtils.REQUEST_CODE_CROP && data != null
				&& resultCode == RESULT_OK) {
			Bitmap bitmap = BitmapFactory
					.decodeFile(cropFile.getAbsolutePath());
			if (bitmap != null) {
				// 设置妈妈头像
				mamaCircularImage.setImageBitmap(bitmap);
				base64 = DialogUtils.imgToBase64(bitmap);

			} else {
				ToastTool.toastMessage(this, "用户存储空间不足，请整理空间后上传!");
			}
		}
	}

}
