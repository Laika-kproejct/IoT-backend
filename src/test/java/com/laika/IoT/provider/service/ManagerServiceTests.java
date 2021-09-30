package com.laika.IoT.provider.service;

import com.laika.IoT.entity.Manager;
import com.laika.IoT.repository.ManagerRepository;
import com.laika.IoT.web.dto.RequestManger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("local")
public class ManagerServiceTests {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private ManagerRepository managerRepository;

    @Test
    @DisplayName("회원가입 서비스 테스트")
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
}
