package org.novaride.nova_ride_authservice.dtos;

import lombok.*;

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

}
