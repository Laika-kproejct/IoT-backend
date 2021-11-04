package com.laika.IoT.core.service;

import com.laika.IoT.core.type.SensorType;
import com.laika.IoT.web.dto.ResponseIoTSensor;

import java.util.Optional;

public interface SensorServiceInterface {
    Optional<ResponseIoTSensor.Register> register(Long recipientId, String token, SensorType type);
}
