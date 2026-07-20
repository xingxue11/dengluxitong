package com.community.userservice.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserPermitPaths implements PermitAllPathsProvider {

    @Override
    public List<String> getPermitAllPaths() {
        return Arrays.asList(
                "/api/vcode/**"
        );
        
    }
}