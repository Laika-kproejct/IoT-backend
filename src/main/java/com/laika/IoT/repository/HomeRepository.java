package com.laika.IoT.repository;

import com.laika.IoT.entity.Home;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRepository extends JpaRepository<Home, Long> {
    Home findByAddress(String address);
}
