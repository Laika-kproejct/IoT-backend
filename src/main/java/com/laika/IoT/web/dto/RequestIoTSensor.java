package com.laika.IoT.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

public class RequestIoTSensor {

    @Builder
    @Data
    public static class Register {
        private String token;
        @NotNull(message = "관리대상자 번호를 입력해주세요")
        private Long recipientId;
    }
}
