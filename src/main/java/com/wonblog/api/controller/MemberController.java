package com.wonblog.api.controller;

import com.wonblog.api.config.AppConfig;
import com.wonblog.api.dto.request.Login;
import com.wonblog.api.dto.request.Signup;
import com.wonblog.api.dto.response.JwtResponse;
import com.wonblog.api.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AppConfig appConfig;

    @PostMapping("/members/signup")
    public void signup(@RequestBody @Valid Signup signup) {
        memberService.signUp(signup);
    }

    @PostMapping("/members/login")
    public JwtResponse login(@RequestBody @Valid Login login) {
        long curTime = System.currentTimeMillis();
        Long memberId = memberService.login(login);

        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        String jws = Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .signWith(key)
                .setExpiration(new Date(curTime + 300000))
                .setIssuedAt(new Date(curTime))
                .compact();


        return new JwtResponse(jws);
    }
}
