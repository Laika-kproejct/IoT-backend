package com.laika.IoT.provider.service;

import com.laika.IoT.entity.Home;
import com.laika.IoT.entity.Manager;
import com.laika.IoT.provider.security.JwtAuthToken;
import com.laika.IoT.provider.security.JwtAuthTokenProvider;
import com.laika.IoT.repository.HomeRepository;
import com.laika.IoT.repository.ManagerRepository;
import com.laika.IoT.web.dto.RequestManger;
import com.laika.IoT.web.dto.ResponseHome;
import com.laika.IoT.web.dto.ResponseManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("local")
public class ManagerServiceTests {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private HomeRepository homeRepository;

    @Test
    @DisplayName("회원가입 서비스 테스트")
    @Transactional
    void registerTest() {
        //given
        RequestManger.Register dto = RequestManger.Register.builder()
                .email("hello")
                .password("itsmypassword")
                .build();
        //when
        managerService.register(dto);
        //then
        Manager manager = managerRepository.findByEmail(dto.getEmail());
        assertEquals(dto.getEmail(), manager.getEmail());
        System.out.println(manager.getPassword()); // 암호화된 패스워드
    }

    @Transactional
    @Test
    @DisplayName("로그인 서비스 테스트")
    void loginTest() {
        //given
        RequestManger.Register dto = RequestManger.Register.builder()
                .email("hello")
                .password("itsmypassword")
                .build();
        managerService.register(dto);

        RequestManger.Login loginRequest = RequestManger.Login.builder()
                .email("hello")
                .password("itsmypassword")
                .build();

        //when
        ResponseManager.Login loginResponse = managerService.login(loginRequest).orElseGet(()->null);
        System.out.println(loginResponse.getAccessToken());
        System.out.println(loginResponse.getRefreshToken());

        //then
        assertNotNull(loginResponse.getAccessToken());
        assertNotNull(loginResponse.getRefreshToken());
    }

    @Transactional
    @Test
    @DisplayName("토큰 갱신 테스트")
    void refreshTokenTest() {
        //given
        RequestManger.Register dto = RequestManger.Register.builder()
                .email("hello")
                .password("itsmypassword")
                .build();
        managerService.register(dto);

        RequestManger.Login loginRequest = RequestManger.Login.builder()
                .email("hello")
                .password("itsmypassword")
                .build();

        //when
        ResponseManager.Login loginResponse = managerService.login(loginRequest).orElseGet(()->null);
        ResponseManager.Token tokenResponse = managerService.refreshToken(loginResponse.getRefreshToken()).orElseGet(()->null);
        //then
        assertNotNull(tokenResponse.getRefreshToken());
        assertNotNull(tokenResponse.getAccessToken());
        System.out.println(tokenResponse.getAccessToken());
        System.out.println(tokenResponse.getRefreshToken());
    }

    @Transactional
    @Test
    @DisplayName("집 등록 서비스 테스트")
    void registerHomeTest() {
        //given
        String address = "경기도 용인시";
        RequestManger.Register dto = RequestManger.Register.builder()
                .email("hello")
                .password("itsmypassword")
                .build();
        managerService.register(dto);

        //when
        Manager manager = managerRepository.findByEmail(dto.getEmail());
        managerService.registerHome(manager.getEmail(), address);
        Home home = homeRepository.findByAddress(address);
        //then
        assertNotNull(home);
        assertEquals(home.getAddress(), address);
        assertEquals(home.getManager().getHomes().get(0).getAddress(), home.getAddress());
        System.out.println(home.getAddress());
        System.out.println(home.getManager().getEmail());
        System.out.println(home.getManager().getId());
    }
    @Transactional
    @Test
    @DisplayName("집 리스트 테스트")
    void listHomeTest(){
        RequestManger.Register dto = RequestManger.Register.builder()
                .email("hello")
                .password("itsmypassword")
                .build();
        managerService.register(dto);
        Manager manager = managerRepository.findByEmail(dto.getEmail());
        managerService.registerHome(manager.getEmail(), "경기도 용인시");
        managerService.registerHome(manager.getEmail(), "경기도 수원시");
        managerService.registerHome(manager.getEmail(), "경기도 안양시");
        managerService.registerHome(manager.getEmail(), "경기도 서울특별시");
        managerService.registerHome(manager.getEmail(), "경기도 화성시");
        Page<ResponseHome.MyHome> list = managerService.list(dto.getEmail());
        assertNotNull(list);
        for (ResponseHome.MyHome myHome : list) {
            System.out.println(myHome.getHomeId() + myHome.getAddress());
        }
    }
}
