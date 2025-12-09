package com.acme.jga.crypto.decode;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoDecoder {

    private static final String ALGORITHM = "AES";
    private final Cipher decodingCipher;

    public CryptoDecoder(String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        this.decodingCipher = Cipher.getInstance(ALGORITHM);
        this.decodingCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    }

    public String decode(String toDecode) {
        try {
            byte[] decoded = this.decodingCipher.doFinal(Base64.getDecoder().decode(toDecode));
            return new String(decoded);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
