package com.community.aipredictservice.service;

import com.community.aipredictservice.dto.DebateRequest;
import com.community.aipredictservice.dto.DebateResponse;

public interface DebateService {

    DebateResponse startDebate(DebateRequest request);
}