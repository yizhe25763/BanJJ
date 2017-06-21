package com.shzy.bjj.handler;

import org.apache.http.Header;
import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @brief JSONArray回调Handler
 * @author Fanhao.Yi
 * @date 2015年4月28日下午5:10:58
 * @version V1.0
 */
public abstract class JSONArrayHandler extends JsonHttpResponseHandler {

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
		super.onSuccess(statusCode, headers, response);
		success(response);
	}

	public void onFailure(int statusCode, Header[] headers,String responseBody, Throwable e) {
		super.onFailure(statusCode, headers, responseBody, e);
		failure(statusCode, responseBody, e);
	}
	
	public abstract void success(JSONArray jsonArray);
	
	public abstract void failure(int statusCode, String responseBody,Throwable e);

}
