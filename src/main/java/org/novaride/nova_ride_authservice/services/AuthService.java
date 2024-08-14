package org.novaride.nova_ride_authservice.services;

import org.novaride.nova_ride_authservice.dtos.PassengerDto;
import org.novaride.nova_ride_authservice.dtos.PassengerSignupRequestDto;
import org.novaride.nova_ride_authservice.entities.Passenger;
import org.novaride.nova_ride_authservice.repositories.PassengerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PassengerRepository passengerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    //Bydefault spring doesn't handle BCryptPasswordEncoder class we need to create bean as it has too variable argument constructors.
    //we configure this seprately inside configuration folder.
    public AuthService(PassengerRepository passengerRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.passengerRepository = passengerRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;

    }

    public PassengerDto signupPassenger(PassengerSignupRequestDto passengerSignupRequestDto) {
        Passenger passenger = Passenger.builder()
                .email(passengerSignupRequestDto.getEmail())
                .name(passengerSignupRequestDto.getName())
                .password(bCryptPasswordEncoder.encode(passengerSignupRequestDto.getPassword())) // TODO: Encrypt the password
                .phoneNumber(passengerSignupRequestDto.getPhoneNumber())
                .build();

        Passenger newPassenger = passengerRepository.save(passenger);

        return PassengerDto.from(newPassenger);
    }
}
