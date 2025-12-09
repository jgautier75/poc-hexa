package com.acme.jga.config;

import com.acme.jga.crypto.encode.CryptoEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.vault.annotation.VaultPropertySource;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Configuration
@VaultPropertySource("${vault_path:dev-secrets/creds}")
public class VaultSecrets {
    @Autowired
    Environment env;

    @Bean
    public CryptoEncoder cryptoEngine() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        CryptoEncoder cryptoEngine = new CryptoEncoder();
        cryptoEngine.initCrypto(Objects.requireNonNull(env.getProperty("cipherKey")));
        return cryptoEngine;
    }
}
