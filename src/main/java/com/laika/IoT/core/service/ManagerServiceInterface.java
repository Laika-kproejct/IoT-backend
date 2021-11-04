package com.laika.IoT.core.service;

import com.laika.IoT.entity.Home;
import com.laika.IoT.web.dto.RequestManger;
import com.laika.IoT.web.dto.ResponseHome;
import com.laika.IoT.web.dto.ResponseIoTSensor;
import com.laika.IoT.web.dto.ResponseManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ManagerServiceInterface {
    void register(RequestManger.Register registerDto);
    Optional<ResponseManager.Login> login(RequestManger.Login loginDto);
    Optional<ResponseManager.Token> refreshToken(String token);
    void registerHome(String managerEmail, String address);
    Page<ResponseHome.MyHome> list(String email, Pageable pageable);
    Page<ResponseIoTSensor.MySensor> sensorlist(String email, Pageable pageable);
    String createAccessToken(String id);
    String createRefreshToken(String id);
}
