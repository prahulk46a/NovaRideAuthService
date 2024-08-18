package org.novaride.nova_ride_authservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


//a client application must send a JSON Web Token (JWT) in the authorization header of the HTTP request to your backend API.
// API Gateway validates the token on behalf of your API, so you don't have to add any code in your API to process the authentication.
//JWT is then included in the Authorization header of every subsequent HTTP request made by the client to the server.
@Service
public class JwtService implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> mp = new HashMap<>();
        mp.put("email", "rahulPatil@gmail.com");
        mp.put("phoneNumber", "444442222");
        String result = createToken(mp, "Rahul Patil");
        System.out.println("Generated token is: " + result);
        System.out.println(extractPayload(result, "email").toString());
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
                        .claims(payloads)//payloads are called as claims
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(expiryDate).subject(userName)
                        .signWith(secretKey)
                        .compact();//this compact method creates jwt token into string.
    }

    private Claims extractAllPayloads(String token) {
        // Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        // This can be done but for code reusability seprately made this function(getSignKey())

        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignKey() {
        //This method returns key that is used for authentication of jwt token
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    //This generic method is used to extract particular field/claims of return type T inside the json
    //claim is a specific piece of information contained within the payload
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllPayloads(token);//
        return claimsResolver.apply(claims);
    }


    private Date extracteExpiration(String token) {
        //this callback function will return Date object
        return extractClaim(token, Claims::getExpiration);
    }
    //this method extracts email from token
    private String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //This method checks if the token expiry was before the current time stamp or not ?
    private Boolean isTokenExpired(String token) {
        return extracteExpiration(token).before(new Date());
    }

    private Boolean validateToken(String token,String email) {
        final String userEmailFetchedFromToken = extractEmail(token);
        //checks of extracted token and passed token are equal or not also checks expiry of token
        return (userEmailFetchedFromToken.equals(email)) && !isTokenExpired(token);
    }
    //This will return claims from payload as a object.
    private Object extractPayload(String token, String payloadKey) {
        Claims claim = extractAllPayloads(token);
        return (Object) claim.get(payloadKey);
    }


}
