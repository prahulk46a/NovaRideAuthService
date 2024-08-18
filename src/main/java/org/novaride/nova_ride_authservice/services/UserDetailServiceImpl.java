package org.novaride.nova_ride_authservice.services;

import org.novaride.nova_ride_authservice.entities.Passenger;
import org.novaride.nova_ride_authservice.helpers.AuthPassengerDetails;
import org.novaride.nova_ride_authservice.repositories.PassengerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

//This class is responsible for loading the users in the form of UserDetails object for authentication
//Whenever spring security needs to fetch particular parameter it will fetch from this service
public class UserDetailServiceImpl implements UserDetailsService {
    PassengerRepository passengerRepository;
    public UserDetailServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username/*email*/) throws UsernameNotFoundException {
        Optional<Passenger> passenger=passengerRepository.findPassengerByEmail(username/*email*/);
        if(passenger.isPresent()) {
            return new AuthPassengerDetails(passenger.get());
        }else {
            throw new UsernameNotFoundException("Passenger not found.");
        }
    }

}
