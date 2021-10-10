package com.laika.IoT.provider.service;

import com.laika.IoT.core.security.role.Role;
import com.laika.IoT.core.service.SensorServiceInterface;
import com.laika.IoT.entity.IoTSensor;
import com.laika.IoT.entity.Recipient;
import com.laika.IoT.exception.errors.RegisterSensorFailedException;
import com.laika.IoT.provider.security.JwtAuthToken;
import com.laika.IoT.provider.security.JwtAuthTokenProvider;
import com.laika.IoT.repository.IoTSensorRepository;
import com.laika.IoT.repository.RecipientRepository;
import com.laika.IoT.web.dto.ResponseIoTSensor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorService implements SensorServiceInterface {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final RecipientRepository recipientRepository;
    private final IoTSensorRepository ioTSensorRepository;

    @Transactional
    @Override
    public Optional<ResponseIoTSensor.Register> register(Long recipientId, String token) {
        //관리대상자 존재여부 확인
        Recipient recipient = recipientRepository.findById(recipientId).orElseThrow(()->new RegisterSensorFailedException());
        //
        String newToken = token;
        if(newToken == null) {
            //토큰이 없으므로 발급해주기
            Date expiredDate = Date.from(LocalDateTime.now().plusYears(2).atZone(ZoneId.systemDefault()).toInstant()); // 토큰은 2년 유효
            JwtAuthToken accessToken = jwtAuthTokenProvider.createAuthToken(String.valueOf(recipientId), Role.ADMIN.getCode(), expiredDate);  //토큰 발급
            newToken = accessToken.getToken();
        }
        //토큰 중복 여부 확인
        IoTSensor sensor = ioTSensorRepository.findByToken(newToken);
        if(sensor != null) {
            throw new RegisterSensorFailedException();
        }
        //등록
        IoTSensor newSensor = IoTSensor.builder()
                .token(newToken)
                .recipient(recipient)
                .build();
        newSensor = ioTSensorRepository.save(newSensor);
        recipient.addSensor(newSensor);
        //response dto
        ResponseIoTSensor.Register responseIoTSensor = ResponseIoTSensor.Register.builder()
                .registeredToken(newSensor.getToken())
                .build();
        return Optional.ofNullable(responseIoTSensor);
    }
}
