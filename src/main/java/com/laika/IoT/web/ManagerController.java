package com.laika.IoT.web;

import com.laika.IoT.exception.errors.CustomJwtRuntimeException;
import com.laika.IoT.exception.errors.LoginFailedException;
import com.laika.IoT.provider.service.ManagerService;
import com.laika.IoT.web.dto.CommonResponse;
import com.laika.IoT.web.dto.RequestManger;
import com.laika.IoT.web.dto.ResponseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping("/manager/register")
    public ResponseEntity<CommonResponse> requestRegister(@Valid @RequestBody RequestManger.Register registerDto) {

        managerService.register(registerDto);

        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/manager/login")
    public ResponseEntity<CommonResponse> requestLogin(@Valid @RequestBody RequestManger.Login loginDto) {

        ResponseManager.Login manager = managerService.login(loginDto).orElseThrow(()->new LoginFailedException());

        HashMap<String, Object> map = new HashMap<>();
        map.put("accessToken", manager.getAccessToken());
        map.put("refreshToken", manager.getRefreshToken());

        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .list(map)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/manager/refreshToken")
    public ResponseEntity<CommonResponse> refreshToken(@RequestBody Map<String, String> payload) {
        ResponseManager.Token token = managerService.refreshToken(payload.get("refreshToken")).orElseThrow(()->new CustomJwtRuntimeException());

        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .list(token)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
