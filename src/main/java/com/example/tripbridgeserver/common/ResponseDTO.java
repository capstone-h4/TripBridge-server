package com.example.tripbridgeserver.common;

import lombok.AllArgsConstructor;
import lombok.Data;

// 응답 객체 생성
@Data
@AllArgsConstructor(staticName = "set")
public class ResponseDTO<D> {
    private boolean result;
    private String message;
    private D data;

    public  static <D> ResponseDTO<D> setSuccess(String message) {
        return ResponseDTO.set(true, message, null);
    }

    public static <D> ResponseDTO<D> setFailed(String message) {
        return ResponseDTO.set(false, message, null);
    }

    public static <D> ResponseDTO<D> setSuccessData(String message, D data) {
        return ResponseDTO.set(true, message, data);
    }

    public static <D> ResponseDTO<D> setFailedData(String message, D data) {
        return ResponseDTO.set(false, message, data);
    }

    public static ResponseDTO<Void> setError(String message) {
        return ResponseDTO.setError(message);
    }
}
