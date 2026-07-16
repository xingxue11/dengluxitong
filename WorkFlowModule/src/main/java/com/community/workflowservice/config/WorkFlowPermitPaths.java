package com.community.workflowservice.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.community.userservice.config.PermitAllPathsProvider;

@Component
public class WorkFlowPermitPaths implements PermitAllPathsProvider {

    @Override
    public List<String> getPermitAllPaths() {
        return Arrays.asList(
                "/api/community/flows/**",
                "/api/community/workflow/**"
        );
    }
}