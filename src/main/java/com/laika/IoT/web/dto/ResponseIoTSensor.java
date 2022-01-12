package com.laika.IoT.web.dto;

import com.laika.IoT.core.type.SensorType;
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
        private SensorType type;

        public static ResponseIoTSensor.MySensor of(IoTSensor ioTSensor){
            return MySensor.builder()
                    .Sensorid(ioTSensor.getId())
                    .token(ioTSensor.getToken())
                    .timestamp(ioTSensor.getDates().get(ioTSensor.getDates().size()-1).getTimestamp())
                    .type(ioTSensor.getType())
                    .build();
        }
    }

    @Builder
    @Data
    public static class UnregisteredSensor {
        private String token;
        private SensorType type;
        private Date timestamp;

        public static ResponseIoTSensor.UnregisteredSensor of(IoTSensor ioTSensor){
            return UnregisteredSensor.builder()
                    .token(ioTSensor.getToken())
                    .type(ioTSensor.getType())
                    .timestamp(ioTSensor.getDates().get(ioTSensor.getDates().size()-1).getTimestamp())
                    .build();
        }
    }
}
