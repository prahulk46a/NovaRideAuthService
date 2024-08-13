package org.novaride.nova_ride_authservice.dtos;

import lombok.*;
import org.novaride.nova_ride_authservice.entities.Passenger;

import java.util.Date;

@Getter
@Setter     //we write getters and setters for dto's cause json object is set using dto's
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto {
    private String id;

    private String name;

    private String email;

    private String password; // encrypted password

    private String phoneNumber;

    private Date createdAt;

    //Act as a adapter layer
    public static PassengerDto from(Passenger p) {
        return PassengerDto.builder()
                .id(p.getId().toString())
                .createdAt(p.getCreatedAt())
                .email(p.getEmail())
                .password(p.getPassword())
                .phoneNumber(p.getPhoneNumber())
                .name(p.getName())
                .build();
    }

}
