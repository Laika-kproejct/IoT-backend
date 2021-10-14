package com.laika.IoT.web.dto;

import lombok.Builder;
import lombok.Data;

public class ResponseManager {
    @Builder
    @Data
    public static class Login {
        private String accessToken;
        private String refreshToken;
    }
    @Builder
    @Data
    public static class Token {
        private String accessToken;
        private String refreshToken;
    }
}
