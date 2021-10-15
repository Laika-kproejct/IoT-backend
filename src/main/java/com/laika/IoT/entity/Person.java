package com.laika.IoT.entity;

import com.laika.IoT.web.dto.ResponsePerson;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name="Person")
@Entity
@Getter
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name")
    private String name;

    @ManyToOne(targetEntity = Home.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id")
    private Home home;

    @Builder
    public Person(String name, Home home) {
        this.name = name;
        this.home = home;
    }
}
