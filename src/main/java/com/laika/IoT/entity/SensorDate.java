package com.laika.IoT.entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Table(name = "SensorDate")
@Entity
@Getter
@NoArgsConstructor
public class SensorDate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date timestamp = new Date();

    @ManyToOne(targetEntity = IoTSensor.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "Sensor_id")
    private IoTSensor ioTSensor;

    @Builder
    public SensorDate(Date timestamp, IoTSensor ioTSensor){
        this.timestamp = timestamp;
        this.ioTSensor = ioTSensor;
    }

}
