package com.laika.IoT.web.dto;

import com.laika.IoT.entity.Home;
import com.laika.IoT.entity.IoTSensor;
import com.laika.IoT.entity.Person;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResponseIoTSensor {

    @Builder
    @Data
    public static class Register {
        private String registeredToken;
    }

    @Builder
    @Data
    public  static class MySensor {
        private Long Sensorid;
        private String token;
        private Date timestamp;

        public static ResponseIoTSensor.MySensor of(IoTSensor ioTSensor){
            return MySensor.builder()
                    .Sensorid(ioTSensor.getId())
                    .token(ioTSensor.getToken())
                    .timestamp(ioTSensor.getTimestamp())
                    .build();
        }
    }
}
