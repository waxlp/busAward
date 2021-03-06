package com.zz80z.busAward.common.utils;

import java.io.Serializable;

public class Result<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 错误码. */
    private Integer errno;
    /** 具体的内容. */
    private String [] data;
	public Integer getErrno() {
		return errno;
	}
	public void setErrno(Integer errno) {
		this.errno = errno;
	}
	public String[] getData() {
		return data;
	}
	public void setData(String[] data) {
		this.data = data;
	}
	public Result(Integer errno, String[] data) {
		super();
		this.errno = errno;
		this.data = data;
	}
	public Result() {
		super();
	}
}
