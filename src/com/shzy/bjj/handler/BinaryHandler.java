package com.shzy.bjj.handler;

import org.apache.http.Header;

import com.loopj.android.http.BinaryHttpResponseHandler;

/**
 * 
 * @brief 文件下载二进制回调Handler
 * @author Fanhao.Yi
 * @date 2015年4月28日下午5:10:44
 * @version V1.0
 */
public abstract class BinaryHandler extends BinaryHttpResponseHandler {

    @Override
    public String[] getAllowedContentTypes() {
        return new String[]{".*"};
    }
    
	@Override
	public void onProgress(int bytesWritten, int totalSize) {
		super.onProgress(bytesWritten, totalSize);
		progress(bytesWritten, totalSize);
	}
	
	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
		success(headers,binaryData);
	}
	
	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		
		failure(statusCode,headers,errorResponse,e);
	}
	
	public abstract void progress(int bytesWritten, int totalSize);
	
	public abstract void success(Header[] headers,byte[] binaryData);
	
	public abstract void failure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e);

}
