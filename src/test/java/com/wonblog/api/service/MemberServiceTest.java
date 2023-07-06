package com.wonblog.api.service;

import com.wonblog.api.domain.Member;
import com.wonblog.api.dto.request.Login;
import com.wonblog.api.dto.request.Signup;
import com.wonblog.api.exception.AlreadyExistEmail;
import com.wonblog.api.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;


    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void test1() {
        Member member = Member.builder()
                .email("jungkt0x@naver.com")
                .username("정지원")
                .password("1234")
                .build();

        memberRepository.save(member);

        Member findMember = memberRepository.findAll().get(0);

        Assertions.assertEquals(findMember.getEmail(), member.getEmail());
        Assertions.assertEquals(findMember.getUsername(), member.getUsername());
    }

    @Test
    @DisplayName("회원가입시 비밀번호 암호화 테스트")
    public void test2() {
        Signup signup = Signup.builder()
                .email("jungkt0x@naver.com")
                .username("정지원")
                .password("1234")
                .build();

        memberService.signUp(signup);

        Member findMember = memberRepository.findAll().get(0);

        assertNotEquals(findMember.getPassword(), "1234");
    }

    @Test
    @DisplayName("회원가입시 이메일 중복 테스트")
    public void test3() {
        Member member = Member.builder()
                .email("jungkt0x@naver.com")
                .username("정지원")
                .password("1234")
                .build();

        memberRepository.save(member);

        Signup newMember = Signup.builder()
                .email("jungkt0x@naver.com")
                .username("정지원")
                .password("1234")
                .build();

        Assertions.assertThrows(AlreadyExistEmail.class, () -> memberService.signUp(newMember));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void test4() {
        Signup member = Signup.builder()
                .email("jungkt0x@naver.com")
                .username("정지원")
                .password("1234")
                .build();

        memberService.signUp(member);

        Login login = Login.builder()
                .email("jungkt0x@naver.com")
                .password("1234")
                .build();

        memberService.login(login);
        // controller에서 token발급을 해서 응답 값으로 리턴하는데, 이걸 여기서 테스트 하는게 맞는 것인가?
        // 그냥 service - login 메서드 동작 여부만..
    }
}