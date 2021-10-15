package com.laika.IoT.repository;

import com.laika.IoT.entity.Home;
import com.laika.IoT.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeRepository extends JpaRepository<Home, Long> {
    Home findByAddress(String address);
}
