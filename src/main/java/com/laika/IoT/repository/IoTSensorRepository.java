package com.laika.IoT.repository;

import com.laika.IoT.entity.IoTSensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IoTSensorRepository extends JpaRepository<IoTSensor, Long> {
    IoTSensor findByToken(String token);
}
