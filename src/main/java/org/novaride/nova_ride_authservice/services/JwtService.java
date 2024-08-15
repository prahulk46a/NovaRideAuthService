package org.novaride.nova_ride_authservice.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.claims;

@Service
public class JwtService implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> mp = new HashMap<>();
        mp.put("email", "a@b.com");
        mp.put("phoneNumber", "9999999999");
        String result = createToken(mp, "Rahul");
        System.out.println("Generated token is: " + result);
    }

    @Value("${jwt.expiry}")
    private int Expiry;

    @Value("${jwt.secret}")
    private String SECRET;


    private String createToken(Map<String, Object> payloads,String userName) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Expiry*1000L);
        SecretKey secretKey= Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                        .claims(payloads)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(expiryDate).subject(userName)
                        .signWith(secretKey)
                        .compact();//this compact method creates jwt token into string.

    }



}
