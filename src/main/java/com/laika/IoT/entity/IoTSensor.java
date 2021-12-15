package com.laika.IoT.entity;

import com.laika.IoT.core.type.SensorType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Table(name="Sensor")
@Entity
@Getter
@NoArgsConstructor
public class IoTSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="token")
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date timestamp = new Date();

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private SensorType type;

    @ManyToOne(targetEntity = Home.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id")
    private Home home;

    @Column(name="registerHome", nullable = false)
    private boolean registerHome = false;

    @Builder
    public IoTSensor(String token, SensorType type) {
        this.token = token;
        this.type = type;
    }
    public void UpdateTimestamp(Date timestamp){
        this.timestamp = timestamp;
    }
    public void updateRegisterHome(boolean bool) {
        this.registerHome = bool;
    }
}
