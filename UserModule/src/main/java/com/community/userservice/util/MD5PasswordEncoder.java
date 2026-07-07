package com.community.userservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MD5PasswordEncoder implements PasswordEncoder {

    private static final Logger logger = LoggerFactory.getLogger(MD5PasswordEncoder.class);

    @Override
    public String encode(CharSequence rawPassword) {
        String encoded = MD5Util.encrypt(rawPassword.toString());
        logger.debug("MD5 encode: rawPassword={}, encoded={}", rawPassword, encoded);
        return encoded;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String encodedRawPassword = MD5Util.encrypt(rawPassword.toString());
        boolean result = encodedRawPassword.equals(encodedPassword);
        logger.info("MD5 matches: rawPassword={}, encodedInput={}, encodedStored={}, result={}", 
                    rawPassword, encodedRawPassword, encodedPassword, result);
        return result;
    }
}