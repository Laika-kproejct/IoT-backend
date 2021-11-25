package com.laika.IoT.web;

import com.laika.IoT.exception.errors.CustomJwtRuntimeException;
import com.laika.IoT.exception.errors.RegisterSensorFailedException;
import com.laika.IoT.provider.security.JwtAuthToken;
import com.laika.IoT.provider.security.JwtAuthTokenProvider;
import com.laika.IoT.provider.service.SensorService;
import com.laika.IoT.web.dto.CommonResponse;
import com.laika.IoT.web.dto.RequestIoTSensor;
import com.laika.IoT.web.dto.ResponseIoTSensor;
import com.laika.IoT.web.dto.ResponseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class IoTSensorController {
    private final SensorService sensorService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @GetMapping("/sensor/register")
    public ResponseEntity<CommonResponse> registerSensor(@Valid RequestIoTSensor.Register register) {
        System.out.println("들어왔다"+register.getToken());
        ResponseIoTSensor.Register responseDto = sensorService.register(register.getHomeId(), register.getToken(), register.getType()).orElseThrow(()-> new RegisterSensorFailedException());

        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("등록 성공")
                .list(responseDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/manager/list/home/{homeId}/sensor")
    public ResponseEntity<CommonResponse> listSensor(HttpServletRequest request, @PathVariable("homeId") Long homeId, @PageableDefault Pageable pageable){
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if(token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        Page<ResponseIoTSensor.MySensor> sensors = sensorService.sensorlist(homeId, pageable);
        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .list(sensors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/sensor/update")
    public ResponseEntity<CommonResponse> updateSensor(@Valid RequestIoTSensor.Update updateDto) {
        if(updateDto.getStatus() == null) {
            sensorService.update(updateDto.getToken());
        } else {
            sensorService.update(updateDto.getToken(),updateDto.getStatus());
        }


        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("업데이트 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
