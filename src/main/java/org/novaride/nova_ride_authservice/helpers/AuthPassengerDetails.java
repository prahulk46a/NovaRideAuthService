package org.novaride.nova_ride_authservice.helpers;


import org.novaride.modelentity.models.Passenger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//Statefull Authentication user details saved at db and checked at time of login
//Need of class? => Because spring security works on UserDetails polymorphic type(all classes that implement UserDetails) for auth
//why only UserDetails type?=> Cause it will be hard to manage if everyone has his/her methods
public class AuthPassengerDetails extends Passenger implements UserDetails {          //Using this interface spring security will handle authentication and authorization part
    String username;                            //this could be any one of unique identifier ex - username, email, id
    String password;
    public AuthPassengerDetails(Passenger passenger) {
        this.username = passenger.getEmail();
        this.password = passenger.getPassword();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
