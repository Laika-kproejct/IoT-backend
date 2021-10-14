package com.laika.IoT.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name="Manager")
@Entity
@Getter
@NoArgsConstructor
public class Manager { 
    //관리대상자를 관리하는 관리자 엔티티
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="salt")
    private String salt;

    @Column(name="refreshToken")
    private String refreshToken;

    @OneToMany(mappedBy = "manager")
    private List<Home> homes = new ArrayList<>();

    @Builder
    public Manager(String email, String password, String salt) {
        this.email = email;
        this.password = password;
        this.salt = salt;
    }
    public void addHome(Home home) {
        this.homes.add(home);
    }
    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
