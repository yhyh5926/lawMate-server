package com.edu.springboot.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("200", "SUCCESS", data);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    // 수정됨: ConsultController 등에서 문자열 메시지만으로 실패 처리를 할 때 사용하는 fail 메서드 추가
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>("400", message, null);
    }
    // 원본: (해당 메서드가 없었음)
}