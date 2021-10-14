package com.laika.IoT.repository;

import com.laika.IoT.entity.Home;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipientRepository extends JpaRepository<Home, Long> {
}
