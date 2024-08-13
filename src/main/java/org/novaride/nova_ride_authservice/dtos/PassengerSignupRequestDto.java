package org.novaride.nova_ride_authservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerSignupRequestDto {
    private String email;

    private String password;

    private String phoneNumber;

    private String name;
}
