package com.laika.IoT.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

public class RequestManger {

    @Builder
    @Data
    public static class Register {
        @NotEmpty(message = "이메일이 비어있음")
        private String email;
        @NotEmpty(message = "비밀번호 입력이 되어있지 않음")
        private String password;
    }
    @Builder
    @Data
    public static class Login {
        @NotEmpty(message = "이메일이 비어있음")
        private String email;
        @NotEmpty(message = "비밀번호 입력이 되어있지 않음")
        private String password;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshFirebase {
        @NotEmpty(message = "토큰값을 넣어주세요")
        private String token;
    }
}
