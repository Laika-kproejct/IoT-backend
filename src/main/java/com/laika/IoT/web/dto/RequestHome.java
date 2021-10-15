package com.laika.IoT.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class RequestHome {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        @NotNull(message = "주소 넣어주세요")
        private String address;
    }
}
