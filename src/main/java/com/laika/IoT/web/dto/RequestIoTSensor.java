package com.laika.IoT.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class RequestIoTSensor {

    @Builder
    @Data
    public static class Register {
        private String token;
        @NotNull(message = "관리대상자 번호를 입력해주세요")
        private Long homeId;
    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update{
        @NotNull(message = "토큰을 발급 해주세요")
        private String token;
    }
}
