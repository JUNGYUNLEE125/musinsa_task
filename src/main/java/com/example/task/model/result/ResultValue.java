package com.example.task.model.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultValue {

	private String resultCode = ResultCode.SUCCESS.toString();
	private String resultMessage = ResultCode.SUCCESS.getDesc();

	@JsonInclude(value = Include.NON_NULL)
	private Object resultData = null;

	public ResultValue(ResultCode resultCode, Object data) {
		this.resultCode = resultCode.toString();
		this.resultMessage = resultCode.getDesc();
		this.resultData = data;
	}
}