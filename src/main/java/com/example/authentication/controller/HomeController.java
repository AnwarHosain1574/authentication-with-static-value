package com.example.authentication.controller;

import com.example.authentication.model.JwtRequest;
import com.example.authentication.response.JwtResponse;
import com.example.authentication.service.UserService;
import com.example.authentication.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @Autowired
    private JWTUtility utility;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserService service;

    @GetMapping("/")
    public String home(){
        return "this is anwar's practice.";
    }

    @PostMapping("authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try{
            manager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUserName(),
                            jwtRequest.getPassword()
                    )
            );
        }catch (BadCredentialsException e){
            e.printStackTrace();
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = service.loadUserByUsername(jwtRequest.getUserName());

        final String token = utility.generateToken(userDetails);

        return new JwtResponse(token);
    }

}
