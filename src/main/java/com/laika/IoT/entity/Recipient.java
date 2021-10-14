package com.laika.IoT.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//관리 대상자
@Table(name="Recipient")
@Entity
@Getter
@NoArgsConstructor
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "recipient")
    private List<IoTSensor> sensors = new ArrayList<IoTSensor>();

    @ManyToOne(targetEntity = Manager.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public void addSensor(IoTSensor sensor) {
        this.sensors.add(sensor);
    }

}
