package com.laika.IoT.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Table(name="FirebaseToken")
@Entity
@Getter
@NoArgsConstructor
public class FirebaseToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="token")
    private String token;

    @ManyToOne(targetEntity = Manager.class, fetch = FetchType.LAZY)
    @JoinColumn(name="manager_id")
    private Manager manager;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date timestamp = new Date();

    public void updateDate() {
        this.timestamp = new Date();
    }
    @Builder
    public FirebaseToken(Manager manager, String token) {
        this.manager = manager;
        this.token = token;
    }
}
