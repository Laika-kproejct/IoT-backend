package com.laika.IoT.core.service;

import com.laika.IoT.core.type.SensorType;
import com.laika.IoT.web.dto.ResponseIoTSensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SensorServiceInterface {
    Optional<ResponseIoTSensor.Register> register(Long recipientId, String token, SensorType type);
    Page<ResponseIoTSensor.MySensor> sensorlist(Long homeId, Pageable pageable);
}
