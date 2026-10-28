package com.community.communityservice.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.community.userservice.config.PermitAllPathsProvider;

import com.community.userservice.config.PermitAllPathsProvider;



@Component
public class CommServicePermitPaths implements PermitAllPathsProvider {

    @Override
    public List<String> getPermitAllPaths() {
        return Arrays.asList(
                "/api/community/**",
                "/api/car-apply/**",
                "/api/article/**"
        );
    }
}