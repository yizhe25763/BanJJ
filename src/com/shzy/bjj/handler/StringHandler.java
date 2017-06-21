package com.shzy.bjj.handler;

import org.apache.http.Header;

import android.util.Log;
import android.view.View;

import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 
 * @brief String回调Handler
 * @author Fanhao.Yi
 * @date 2015年5月11日上午11:56:08
 * @version V1.0
 */
public abstract class StringHandler extends TextHttpResponseHandler {
	private View loading;

	public StringHandler(View load) {
		this.loading = load;
	}


	@Override
	public void onFailure(int statusCode, Header[] headers,
			String responseBody, Throwable e) {
		try {
			failure(statusCode, responseBody, e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (loading != null) {
			loading.setVisibility(View.GONE);
		}
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, String response) {
		try {
			success(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void success(String response);

	public abstract void failure(int statusCode, String responseBody,
			Throwable e);

}
