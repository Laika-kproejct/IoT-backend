package com.laika.IoT.web;

import com.laika.IoT.entity.Home;
import com.laika.IoT.exception.errors.CustomJwtRuntimeException;
import com.laika.IoT.exception.errors.LoginFailedException;
import com.laika.IoT.provider.security.JwtAuthToken;
import com.laika.IoT.provider.security.JwtAuthTokenProvider;
import com.laika.IoT.provider.service.ManagerService;
import com.laika.IoT.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
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
    @PostMapping("/manager/register/home")
    public ResponseEntity<CommonResponse> registerHome(@Valid @RequestBody RequestHome.Register registerDto, HttpServletRequest request) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if(token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        managerService.registerHome(email, registerDto.getAddress());
        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/manager/list/home")
    public ResponseEntity<CommonResponse> listHome(HttpServletRequest request, @PageableDefault Pageable pageable){
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if(token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        Page<ResponseHome.MyHome> homes = managerService.list(email, pageable);
        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .list(homes)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/manager/fcm/refresh")
    public ResponseEntity<CommonResponse> fcmRefresh(HttpServletRequest request, @RequestBody RequestManger.RefreshFirebase requestDto){
        String token = jwtAuthTokenProvider.resolveToken(request).orElseThrow(()->new CustomJwtRuntimeException());
        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token);
        String email = jwtAuthToken.getData().getSubject();

        managerService.refreshFcmToken(email, requestDto.getToken());

        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/dev/test")
    public ResponseEntity<CommonResponse> requestTest(@RequestParam double val) {
        System.out.println("들어왔다" + val);
        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
