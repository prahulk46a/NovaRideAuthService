package org.novaride.nova_ride_authservice.services;

import org.novaride.nova_ride_authservice.dtos.PassengerDto;
import org.novaride.nova_ride_authservice.dtos.PassengerSignupRequestDto;
import org.novaride.nova_ride_authservice.entities.Passenger;
import org.novaride.nova_ride_authservice.repositories.PassengerRepository;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PassengerRepository passengerRepository;
    //private BCryptPasswordEncoder bCryptPasswordEncoder;


    public AuthService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;

    }

    public PassengerDto signupPassenger(PassengerSignupRequestDto passengerSignupRequestDto) {
        Passenger passenger = Passenger.builder()
                .email(passengerSignupRequestDto.getEmail())
                .name(passengerSignupRequestDto.getName())
                .password(passengerSignupRequestDto.getPassword()) // TODO: Encrypt the password
                .phoneNumber(passengerSignupRequestDto.getPhoneNumber())
                .build();

        Passenger newPassenger = passengerRepository.save(passenger);

        return PassengerDto.from(newPassenger);
    }
}
