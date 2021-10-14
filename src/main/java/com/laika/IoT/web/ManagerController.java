package com.laika.IoT.web;

import com.laika.IoT.provider.service.ManagerService;
import com.laika.IoT.web.dto.CommonResponse;
import com.laika.IoT.web.dto.RequestManger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
    @GetMapping("/dev/test")
    public ResponseEntity<CommonResponse> requestRegister(@RequestParam double val){
        System.out.println("들어왔다" + val);
        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
