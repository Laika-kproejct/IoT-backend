package com.laika.IoT.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//관리 대상자 집
@Table(name="Home")
@Entity
@Getter
@NoArgsConstructor
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "home")
    private List<IoTSensor> sensors = new ArrayList<IoTSensor>();

    @OneToMany(mappedBy = "home")
    private List<Person> personList = new ArrayList<>();

    @ManyToOne(targetEntity = Manager.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Builder
    public Home(String address, Manager manager) {
        this.address = address;
        this.manager = manager;
    }

    public void addSensor(IoTSensor sensor) {
        this.sensors.add(sensor);
    }
    public void addPerson(Person person) {
        this.personList.add(person);
    }

}
