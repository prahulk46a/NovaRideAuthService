package org.novaride.nova_ride_authservice.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.novaride.nova_ride_authservice.services.JwtService;
import org.novaride.nova_ride_authservice.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Filter can be called either before or after servlet execution. When a request is dispatched to a servlet, the RequestDispatcher may forward it to another servlet.
//There’s a possibility that the other servlet also has the same filter. In such scenarios, the same filter gets invoked multiple times.
//But, we might want to ensure that a specific filter is invoked only once per request. When a request goes through the filter chain,
// we might want some of the authentication actions to happen only once for the request.
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
//this filter can be attached to any of corresposnding request at any point of time because of SecurityContext storing it
    private final JwtService jwtService;
    private final RequestMatcher uriMatcher = new AntPathRequestMatcher("/api/v1/auth/validate", HttpMethod.GET.name());
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;

    public JwtAuthFilter(JwtService jwtService, UserDetailServiceImpl userDetailServiceImpl) {
        this.jwtService = jwtService;
        this.userDetailServiceImpl = userDetailServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=null;
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("JWT")) {
                    token = cookie.getValue();
                    System.out.println(token);
                }
            }
        }
        //User not provided any jwt token in cookie
        if(token != null) {
            // user has not provided any jwt token hence request should not go forward
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = jwtService.extractEmail(token);
        //whether user exist with this email also we should implement validate token from our jwt service
        if(email != null) {
            UserDetails userDetails=userDetailServiceImpl.loadUserByUsername(email);
            if(jwtService.validateToken(token, userDetails.getUsername())){
                //In a typical authentication flow, after verifying a user’s credentials (e.g., username and password), you would populate this token with a collection of authorities.
                //creation of this obj don't need any validation it will do internally itself.
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);

                //whaterver request object that is converted into spring boot compatible object here
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //This will is to make sure that the authentication remembered by spring security to authenticate at any point of time.So no need to manually do same process again
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        //is used to pass the HttpServletRequest and HttpServletResponse objects to the next filter in the chain.
        // It ensures that the request and response are processed by the next filter, or by the target servlet,
        // if this is the last filter in the chain.
        System.out.println("Forwarding req");
        filterChain.doFilter(request, response);
    }
    //this request will be send to other srvices after successfully applying all filers
    protected boolean shouldNotFilter(HttpServletRequest request) {
        RequestMatcher matcher = new NegatedRequestMatcher(uriMatcher);
        return matcher.matches(request);
    }


}
