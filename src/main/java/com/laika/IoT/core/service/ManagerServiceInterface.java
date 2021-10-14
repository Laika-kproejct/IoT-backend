package com.laika.IoT.core.service;

import com.laika.IoT.web.dto.RequestManger;
import com.laika.IoT.web.dto.ResponseManager;

import java.util.Optional;

public interface ManagerServiceInterface {
    void register(RequestManger.Register registerDto);
    Optional<ResponseManager.Login> login(RequestManger.Login loginDto);
    Optional<ResponseManager.Token> refreshToken(String token);
    void registerHome(String managerEmail, String address);
    String createAccessToken(String id);
    String createRefreshToken(String id);
}
