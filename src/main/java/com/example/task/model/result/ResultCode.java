package com.example.task.model.result;

public enum ResultCode {
	SUCCESS("000", "성공"),
    FAILED("100", "알수 없는 실패"), 
	NO_DATA("101", "조회할 데이터가 없습니다."), 
	INTERNAL_SERVER_ERROR("102", "서버 내부 오류"),
	INVALID_INPUT("103", "입력오류"), 
	ALREADY_EXISTS("104", "존재하는 상품입니다.")
	;

	private final String code;

	private final String desc;

	private ResultCode(final String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	@Override
	public String toString() {
		return code.toString();
	}

	public String getDesc() {
		return desc;
	}
}
