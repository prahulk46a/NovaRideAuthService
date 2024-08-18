package org.novaride.nova_ride_authservice.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.novaride.nova_ride_authservice.dtos.AuthRequestDto;
import org.novaride.nova_ride_authservice.dtos.AuthResponseDto;
import org.novaride.nova_ride_authservice.dtos.PassengerDto;
import org.novaride.nova_ride_authservice.dtos.PassengerSignupRequestDto;
import org.novaride.nova_ride_authservice.services.AuthService;
import org.novaride.nova_ride_authservice.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Value("${cookie.expiry}")
    private int cookieExpiry;

    private final AuthenticationManager authenticationManager;
    private AuthService authService;
    private final JwtService jwtService;
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signUp(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerDto response = authService.signupPassenger(passengerSignupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    //We need to check whether password is correct or not and after that
    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        if(authentication.isAuthenticated()) {
            String jwtToken = jwtService.createToken(authRequestDto.getEmail());

            ResponseCookie cookie = ResponseCookie.from("JwtToken", jwtToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(cookieExpiry)
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return new ResponseEntity<>(AuthResponseDto.builder().success(true).build(), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }




}
