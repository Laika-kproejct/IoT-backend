package com.laika.IoT.provider.service;

import com.laika.IoT.core.service.ManagerServiceInterface;
import com.laika.IoT.entity.Manager;
import com.laika.IoT.exception.errors.RegisterFailedException;
import com.laika.IoT.repository.ManagerRepository;
import com.laika.IoT.util.SHA256Util;
import com.laika.IoT.web.dto.RequestManger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerService implements ManagerServiceInterface {
    private final ManagerRepository managerRepository;

    @Transactional
    @Override
    public void register(RequestManger.Register registerDto) {
        Manager manager = managerRepository.findByEmail(registerDto.getEmail());
        if(manager != null) {
            // 이미 존재하는 아이디
            throw new RegisterFailedException();
        }
        //salt 생성
        String salt = SHA256Util.generateSalt();
        //sha256으로 솔트와 함께 암호화
        String encryptedPassword = SHA256Util.getEncrypt(registerDto.getPassword(), salt);
        //db에 기입
        manager = Manager.builder()
                .email(registerDto.getEmail())
                .password(encryptedPassword)
                .salt(salt)
                .build();
        managerRepository.save(manager);
    }
}
