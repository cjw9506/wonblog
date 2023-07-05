package com.wonblog.api.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;


import java.util.Base64;

@Getter
@ConfigurationProperties(prefix = "wonblog")
public class AppConfig {

    private byte[] jwtKey;

    public void setJwtKey(String jwtKey) {
        this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }

}
