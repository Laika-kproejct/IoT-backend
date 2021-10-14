package com.laika.IoT.web.dto;

import lombok.Builder;
import lombok.Data;

public class ResponseIoTSensor {

    @Builder
    @Data
    public static class Register {
        private String registeredToken;
    }
}
