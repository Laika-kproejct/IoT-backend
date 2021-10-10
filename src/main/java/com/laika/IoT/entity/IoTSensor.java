package com.laika.IoT.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne(targetEntity = Recipient.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Recipient recipient;

    @Builder
    public IoTSensor(String token, Recipient recipient) {
        this.token = token;
        this.recipient = recipient;
    }
}
