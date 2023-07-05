package com.wonblog.api.dto.response;

import lombok.Getter;

@Getter
public class JwtResponse {

    private final String jwt;

    public JwtResponse(String jwt) {
        this.jwt = jwt;
    }
}
