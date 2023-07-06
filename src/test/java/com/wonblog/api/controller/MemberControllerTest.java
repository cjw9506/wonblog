package com.wonblog.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonblog.api.config.AppConfig;
import com.wonblog.api.dto.request.Login;
import com.wonblog.api.dto.request.Signup;
import com.wonblog.api.dto.response.JwtResponse;
import com.wonblog.api.repository.MemberRepository;
import com.wonblog.api.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AppConfig appConfig;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void test1() throws Exception {
        Signup request = Signup.builder()
                .email("jungkt0x@naver.com")
                .username("정지원")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 중복 테스트")
    public void test2() throws Exception{
        Signup request = Signup.builder()
                .email("jungkt0x@naver.com")
                .username("정지원")
                .password("1234")
                .build();

        memberService.signUp(request);

        Signup newRequest = Signup.builder()
                .email("jungkt0x@naver.com")
                .username("정지원")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(newRequest);

        mockMvc.perform(post("/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void test3() throws Exception {
        Signup request = Signup.builder()
                .email("jungkt0x@naver.com")
                .username("정지원")
                .password("1234")
                .build();

        memberService.signUp(request);

        Login login = Login.builder()
                .email("jungkt0x@naver.com")
                .password("1234")
                .build();

        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 시 토큰 발급 테스트")
    public void test4() throws Exception {
        // 회원 가입 요청 생성
        Signup request = Signup.builder()
                .email("jungkt0x@naver.com")
                .username("정지원")
                .password("1234")
                .build();

        // 회원 가입 수행
        memberService.signUp(request);

        // 로그인 요청 생성
        Login login = Login.builder()
                .email("jungkt0x@naver.com")
                .password("1234")
                .build();

        // 예상 응답 생성
        long curTime = System.currentTimeMillis();
        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());
        String expectedJws = Jwts.builder()
                .setSubject(String.valueOf(1))
                .signWith(key)
                .setExpiration(new Date(curTime + 300000))
                .setIssuedAt(new Date(curTime))
                .compact();
        JwtResponse expectedResponse = new JwtResponse(expectedJws);

        // 로그인 요청 및 토큰 발급 테스트
        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists());
    }

}