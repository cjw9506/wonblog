package com.wonblog.api.service;

import com.wonblog.api.crypto.PasswordEncoder;
import com.wonblog.api.domain.Member;
import com.wonblog.api.dto.request.Login;
import com.wonblog.api.dto.request.Signup;
import com.wonblog.api.exception.AlreadyExistEmail;
import com.wonblog.api.exception.InvalidLoginInformation;
import com.wonblog.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(Signup signup) {

        Optional<Member> userOptional = memberRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistEmail();
        }

        String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

        Member member = Member.builder()
                .email(signup.getEmail())
                .username(signup.getUsername())
                .password(encryptedPassword)
                .build();

        memberRepository.save(member);
    }

    @Transactional
    public Long login(Login login) {
        Member member = memberRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new InvalidLoginInformation());

        boolean matches = passwordEncoder.matches(login.getPassword(), member.getPassword());
        if (!matches) {
            throw new InvalidLoginInformation();
        }
        return member.getId();

    }
}
