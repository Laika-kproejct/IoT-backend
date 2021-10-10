package com.laika.IoT.provider.service;

import com.laika.IoT.core.security.role.Role;
import com.laika.IoT.core.service.ManagerServiceInterface;
import com.laika.IoT.entity.Manager;
import com.laika.IoT.exception.errors.CustomJwtRuntimeException;
import com.laika.IoT.exception.errors.LoginFailedException;
import com.laika.IoT.exception.errors.RegisterFailedException;
import com.laika.IoT.provider.security.JwtAuthToken;
import com.laika.IoT.provider.security.JwtAuthTokenProvider;
import com.laika.IoT.repository.ManagerRepository;
import com.laika.IoT.util.SHA256Util;
import com.laika.IoT.web.dto.RequestManger;
import com.laika.IoT.web.dto.ResponseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerService implements ManagerServiceInterface {
    private final ManagerRepository managerRepository;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

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

    @Transactional
    @Override
    public Optional<ResponseManager.Login> login(RequestManger.Login loginDto) {
        //아이디로 어드민 꺼내기
        Manager manager = managerRepository.findByEmail(loginDto.getEmail());
        if(manager == null) {
            throw new LoginFailedException();
        }
        //솔트 꺼내기
        String salt = manager.getSalt();
        //솔트와 로그인 디티오 조합해서 암호화
        String encryptedPassword = SHA256Util.getEncrypt(loginDto.getPassword(), salt);
        //비교
        manager = managerRepository.findByEmailAndPassword(loginDto.getEmail(), encryptedPassword);
        ResponseManager.Login login = null;
        if(manager != null) {
            //로그인 성공
            String refreshToken = createRefreshToken((manager.getEmail()));
            login = ResponseManager.Login.builder()
                    .accessToken(createAccessToken(manager.getEmail()))
                    .refreshToken(refreshToken)
                    .build();

            manager.changeRefreshToken(refreshToken); // 로그인 때마다 리프레시 토큰 업데이트
        } else {
            //로그인 실패
            throw new LoginFailedException();
        }
        return Optional.ofNullable(login);
    }

    @Transactional
    @Override
    public Optional<ResponseManager.Token> refreshToken(String token) {
        if(token == null || token.equals("null")) {
            throw new CustomJwtRuntimeException();
        }
        //db에서 리프레시 토큰으로 어드민을 꺼낸다.
        Manager manager = managerRepository.findByRefreshToken(token);
        //없으면 실패
        if(manager == null) {
            throw new CustomJwtRuntimeException();
        }
        //디비에 있는 토큰이랑 다르면 실패, 즉 현재는 한 기기에만 로그인 가능, 여러 기기에 하려면 엔티티를 별도로 생성해야할듯
        if(!manager.getRefreshToken().equals(token)) {
            throw new CustomJwtRuntimeException();
        }
        //토큰 유효 검증
        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token);
        if(!jwtAuthToken.validate() || !jwtAuthToken.getData().get("role").equals(Role.ADMIN.getCode())) {
            return Optional.empty(); // 실패
        }

        String id = String.valueOf(jwtAuthToken.getData().get("id"));
        String accessToken = createAccessToken(id); // 액세스토큰 재발급

        ResponseManager.Token newToken = ResponseManager.Token.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .build();
        return Optional.ofNullable(newToken);
    }

    @Override
    public String createAccessToken(String id) {
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(2).atZone(ZoneId.systemDefault()).toInstant()); // 토큰은 2분만 유지되도록 설정, 2분 후 refresh token
        JwtAuthToken accessToken = jwtAuthTokenProvider.createAuthToken(id, Role.ADMIN.getCode(), expiredDate);  //토큰 발급

        return accessToken.getToken();
    }

    @Override
    public String createRefreshToken(String id) {
        Date expiredDate = Date.from(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant()); // refresh토큰은 유효기간이 1년
        JwtAuthToken refreshToken = jwtAuthTokenProvider.createAuthToken(id, Role.ADMIN.getCode(), expiredDate);  //토큰 발급
        return refreshToken.getToken();
    }
}
