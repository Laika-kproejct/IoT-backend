package com.laika.IoT.repository;

import com.laika.IoT.entity.FirebaseToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirebaseTokenRepository extends JpaRepository<FirebaseToken, Long> {
    FirebaseToken findByToken(String token);
}
