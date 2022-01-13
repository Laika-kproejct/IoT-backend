package com.laika.IoT.provider.service;

import com.google.firebase.FirebaseException;
import com.laika.IoT.core.security.role.Role;
import com.laika.IoT.core.service.SensorServiceInterface;
import com.laika.IoT.core.type.SensorType;
import com.laika.IoT.entity.*;
import com.laika.IoT.exception.errors.NotFoundHomeException;
import com.laika.IoT.exception.errors.NotFoundSensorException;
import com.laika.IoT.exception.errors.RegisterSensorFailedException;
import com.laika.IoT.provider.security.JwtAuthToken;
import com.laika.IoT.provider.security.JwtAuthTokenProvider;
import com.laika.IoT.repository.IoTSensorRepository;
import com.laika.IoT.repository.HomeRepository;
import com.laika.IoT.repository.SensorDateRepository;
import com.laika.IoT.util.FCMUtil;
import com.laika.IoT.web.dto.RequestIoTSensor;
import com.laika.IoT.web.dto.ResponseIoTSensor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorService implements SensorServiceInterface {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final HomeRepository homeRepository;
    private final IoTSensorRepository ioTSensorRepository;
    private final SensorDateRepository sensorDateRepository;
    private final FCMUtil fcmUtil;

    @Transactional
    @Override
    public Optional<ResponseIoTSensor.Register> register(Long recipientId, String token, SensorType type) {
        //관리대상자 존재여부 확인
        Home home = homeRepository.findById(recipientId).orElseThrow(()->new RegisterSensorFailedException());
        //센서 등록 여부 확인
        IoTSensor sensor = ioTSensorRepository.findByToken(token);
        if(sensor == null) throw new RegisterSensorFailedException();
        if(sensor.isRegisterHome()) throw new RegisterSensorFailedException();

        // 홈에 센서등록 및 센서 이스레지스터드홈 true
        sensor.updateRegisterHome(true);
        sensor.setHome(home);
        home.addSensor(sensor);
        //dto
        ResponseIoTSensor.Register dto = ResponseIoTSensor.Register.builder()
                .registeredToken(token)
                .build();
        return Optional.ofNullable(dto);
    }

    @Transactional
    @Override
    public Page<ResponseIoTSensor.MySensor> sensorlist(Long homeId, Pageable pageable) {
        // 홈
        Home home = homeRepository.findById(homeId).orElseThrow(() -> new NotFoundHomeException());
        //센서와 날짜들 출력
        Page<IoTSensor> sensors = ioTSensorRepository.findByHome(home, pageable);
        return sensors.map(ResponseIoTSensor.MySensor::of);
    }
    @Transactional
    @Override
    public void update(String token) {
        //토큰으로 센서 검색
        IoTSensor sensor = ioTSensorRepository.findByToken(token);
        //실패시 예외 발생 처리
        if(sensor == null){
            throw new NotFoundSensorException();
        }
        //해당 센서 타임스탬프 업데이트
//        System.out.println(sensor.getTimestamp());
//        sensor.UpdateTimestamp(new Date());
//        System.out.println(sensor.getTimestamp());

        SensorDate sensorDate = SensorDate.builder()
                .ioTSensor(sensor)
                .timestamp(new Date())
                .build();
        sensor.updateSensorDate(sensorDate);

        // IN으로 변경
        Home home = sensor.getHome();
        if(home != null) home.updateStatus(true);
    }
    @Transactional
    @Override
    public void update(String token, String status) {
        //토큰으로 센서 검색
        IoTSensor sensor = ioTSensorRepository.findByToken(token);
        //실패시 예외 발생 처리
        if(sensor == null){
            throw new NotFoundSensorException();
        }
        //해당 센서 타임스탬프 업데이트

        SensorDate sensorDate = SensorDate.builder()
                .ioTSensor(sensor)
                .timestamp(new Date())
                .build();
        sensor.updateSensorDate(sensorDate);

        //홈 인아웃 변경
        Home home = sensor.getHome();

        if(status.equals("IN")){
            home.updateStatus(true);
        }else
            home.updateStatus(false);
    }

    @Transactional
    @Override
    public void check(LocalDateTime localDateTime) {
        List<Home> homeList = homeRepository.findAll();
        List<Home> warningHomeList = new ArrayList<>();
        //홈 검사
        for (Home home : homeList) {
            if(!home.isNotEmpty()){
                //외출 중일 경우
                continue;
            }
            List<IoTSensor> sensorList = home.getSensors();
            //최신 날짜
            if (!sensorList.isEmpty()) {
                int newestIndex = sensorList.get(0).getDates().size()-1;
                Date newest = sensorList.get(0).getDates().get(newestIndex).getTimestamp();
                for (IoTSensor sensor : sensorList) {
                    newestIndex = sensor.getDates().size()-1;
                    if (sensor.getDates().get(newestIndex).getTimestamp().after(newest)) {
                        //최신일 경우
                        newest = sensor.getDates().get(newestIndex).getTimestamp();
                    }
                }
                System.out.println(newest);
                // 확인 시각과 최신 시각 비교
                Duration duration = Duration.between(newest.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        localDateTime);

                System.out.println("현재시간 :::::"+localDateTime);
                System.out.println("아까시간 :::::"+newest.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                System.out.println("시간 :::::"+duration.getSeconds());
                //하루 86400초
                if (duration.getSeconds() > 120) { // 확인용 2분 이상 업데이틍 안된경우
                //if (duration.getSeconds() > 86400) {
                //if (duration.getSeconds() > 60) { // 확인용 1분이상 업데이트 안됐을 경우
                    //하루 이상 업데이트가 안됐을 경우
                    warningHomeList.add(home);
                    //test하고 지우기
                    System.out.println(home.getAddress());
                }
            }
        }
        //메니저에게 알리기
        if (!warningHomeList.isEmpty()) {
            for (Home warningHome : warningHomeList) {
                Manager manager = warningHome.getManager();

                List<String> tokens = new ArrayList<>();
                for (FirebaseToken firebaseToken : manager.getTokens()){
                    tokens.add(firebaseToken.getToken());
                }
                String title = "집 비상!";
                String body = warningHome.getAddress() + "확인해주세요.";
                try{
                    fcmUtil.sendTokens(tokens,title,body);
                }
                catch (FirebaseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Transactional
    @Override
    public void registerUnregisteredSensor(String token, SensorType type) {
        String newToken = token;
        if(newToken == null) {
            //토큰이 없으므로 발급해주기
            Date expiredDate = Date.from(LocalDateTime.now().plusYears(2).atZone(ZoneId.systemDefault()).toInstant()); // 토큰은 2년 유효
            JwtAuthToken accessToken = jwtAuthTokenProvider.createAuthToken(String.valueOf(type), Role.ADMIN.getCode(), expiredDate);  //토큰 발급
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
                .type(type)
                .build();
        newSensor = ioTSensorRepository.save(newSensor);
    }

    @Override
    public Page<ResponseIoTSensor.UnregisteredSensor> getUnregisteredSensorList(Pageable pageable) {
        Page<IoTSensor> sensors = ioTSensorRepository.findByRegisterHomeFalse(pageable);
        return sensors.map(ResponseIoTSensor.UnregisteredSensor::of);
    }
}
