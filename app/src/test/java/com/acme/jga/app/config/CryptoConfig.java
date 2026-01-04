package com.acme.jga.app.config;

import com.acme.jga.crypto.encode.CryptoEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class CryptoConfig {

    @Bean
    public CryptoEncoder cryptoEncoder() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return new CryptoEncoder("1c9e1cfbe63844b1a0772aea4cba5gg6");
    }

}
