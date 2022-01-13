package com.laika.IoT.web.dto;

import com.laika.IoT.entity.SensorDate;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

public class ResponseSensorDate {
    @Builder
    @Data
    public static class MySensorDate{
        private Date date;

        public static MySensorDate of(SensorDate sensorDate){
            return MySensorDate.builder()
                    .date(sensorDate.getTimestamp())
                    .build();
        }
    }

}
