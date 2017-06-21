package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class CommentListBean implements Serializable {

	private int result;
	private int comment_count;
	private List<commentList> comment_ist;
	private int error_code;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public List<commentList> getComment_ist() {
		return comment_ist;
	}
	public void setComment_ist(List<commentList> comment_ist) {
		this.comment_ist = comment_ist;
	}
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	

}
