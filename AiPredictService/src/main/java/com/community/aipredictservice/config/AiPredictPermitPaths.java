package com.community.aipredictservice.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.community.userservice.config.PermitAllPathsProvider;

@Component
public class AiPredictPermitPaths implements PermitAllPathsProvider {

    @Override
    public List<String> getPermitAllPaths() {
        return Arrays.asList(
                "/api/debate/**",
                "/ws/debate"
        );
    }
}