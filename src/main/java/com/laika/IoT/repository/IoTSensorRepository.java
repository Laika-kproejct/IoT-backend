package com.laika.IoT.repository;

import com.laika.IoT.entity.Home;
import com.laika.IoT.entity.IoTSensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IoTSensorRepository extends JpaRepository<IoTSensor, Long> {
    IoTSensor findByToken(String token);
    Page<IoTSensor> findByHome(Home home, Pageable pageable);

    @Query
    Page<IoTSensor> findByRegisterHomeFalse(Pageable pageable);
}
