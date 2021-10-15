package com.laika.IoT.web.dto;

import com.laika.IoT.entity.Home;
import com.laika.IoT.entity.Person;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ResponseHome {
    @Builder
    @Data
    public static class MyHome {
        private Long homeId;
        private String address;
        private List<ResponsePerson.Person> personList;

        public static MyHome of(Home home) {
            List<ResponsePerson.Person> personListDto = new ArrayList<>();
            for (Person person : home.getPersonList()) {
                ResponsePerson.Person personDto = ResponsePerson.Person.of(person);
                personListDto.add(personDto);
            }
            return MyHome.builder()
                    .homeId(home.getId())
                    .address(home.getAddress())
                    .personList(personListDto)
                    .build();
        }
    }
}
