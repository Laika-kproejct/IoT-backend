package com.laika.IoT.repository;


import com.laika.IoT.entity.Dog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DogRepositoryTests {
    @Autowired
    private DogRepository dogRepository;

    @DisplayName("H2-JPA 연동 테스트")
    @Test
    @Transactional
    public void saveDogTest() {
        //given
        Dog dog = Dog.builder()
                .name("dog1")
                .age(10)
                .build();

        dog = dogRepository.save(dog);
        //when
        Dog findDog = dogRepository.getById(dog.getId());
        //then

        assertEquals("dog1", findDog.getName());
        assertEquals(10, findDog.getAge());
    }
}
