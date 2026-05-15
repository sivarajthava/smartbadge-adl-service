package com.smartbadge.adl.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartbadge.adl.shared.util.DateUtils;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private final Boolean success;
	private final ErrorDetail error;
	private final T data;
	private final String timestamp;

	public static <T> ApiResponse<T> success(T data) {
		return ApiResponse.<T>builder().success(true).data(data).timestamp(DateUtils.currentDate()).build();
	}

	public static ApiResponse<Void> success() {
		return ApiResponse.<Void>builder().success(true).timestamp(DateUtils.currentDate()).build();
	}

	public static <T> ApiResponse<T> error(String message) {
		return ApiResponse.<T>builder().success(false)
				.error(ErrorDetail.builder().errorCode(message).errorMessage(message).build())
				.timestamp(DateUtils.currentDate()).build();
	}

	@Getter
	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ErrorDetail {
		private String errorCode;
		private String errorMessage;
	}
}
