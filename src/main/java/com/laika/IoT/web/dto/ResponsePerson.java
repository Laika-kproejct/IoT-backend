package com.laika.IoT.web.dto;

import com.laika.IoT.entity.Person;
import lombok.Builder;
import lombok.Data;

public class ResponsePerson {

    @Builder
    @Data
    public static class Person {
        private Long id;
        private String name;

        public static Person of(com.laika.IoT.entity.Person person) {
            return Person.builder()
                    .id(person.getId())
                    .name(person.getName())
                    .build();
        }
    }
}
