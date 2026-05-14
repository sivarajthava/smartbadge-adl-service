package com.smartbadge.adl.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private final Boolean success;
	private final ErrorDetail error;
	private final T data;

	public static <T> ApiResponse<T> success(T data) {
		return ApiResponse.<T>builder().success(true).data(data).build();
	}

	public static ApiResponse<Void> success() {
		return ApiResponse.<Void>builder().success(true).build();
	}

	public static <T> ApiResponse<T> error(String message) {
		return ApiResponse.<T>builder().success(false)
				.error(ErrorDetail.builder().errorCode(message).errorMessage(message).build()).build();
	}

	@Getter
	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ErrorDetail {
		private String errorCode;
		private String errorMessage;
	}
}
