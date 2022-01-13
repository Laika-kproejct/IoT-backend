package com.laika.IoT.repository;

import com.laika.IoT.entity.IoTSensor;
import com.laika.IoT.entity.SensorDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDateRepository extends JpaRepository<SensorDate,Long> {
}
