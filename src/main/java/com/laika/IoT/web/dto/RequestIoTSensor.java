package com.laika.IoT.web.dto;

import com.laika.IoT.core.type.SensorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class RequestIoTSensor {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        private String token;
        @NotNull(message = "관리대상 번호를 입력해주세요")
        private Long homeId;
        @NotNull(message = "센서 타입을 입력해주세요(가속도 센서 등)")
        private SensorType type;

    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update{
        @NotNull(message = "토큰을 발급 해주세요")
        private String token;
        private String status;
    }
}
