package com.laika.IoT.core.service;

import com.laika.IoT.web.dto.RequestManger;

import java.util.Optional;

public interface ManagerServiceInterface {
    void register(RequestManger.Register registerDto);
}
