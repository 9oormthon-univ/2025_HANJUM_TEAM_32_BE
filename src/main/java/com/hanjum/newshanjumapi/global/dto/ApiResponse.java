package com.hanjum.newshanjumapi.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private final Boolean isSuccess;
    private final String code;
    private final String message;

    // result 필드가 null일 경우, JSON 직렬화 시 포함하지 않음
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // --- 정적 팩토리 메소드 ---

    /**
     * 성공 시 사용하는 응답 생성 메소드
     * @param result 반환할 데이터
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, "COMMON200", "성공입니다.", result);
    }

    /**
     * 성공 시 사용하는 응답 생성 메소드 (데이터가 없는 경우)
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> onSuccessWithNoData() {
        return new ApiResponse<>(true, "COMMON200", "성공입니다.", null);
    }

    /**
     * 실패 시 사용하는 응답 생성 메소드
     * @param code 에러 코드 (ErrorStatus Enum 등 활용)
     * @param message 에러 메시지
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
}