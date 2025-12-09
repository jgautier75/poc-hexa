package com.acme.jga.crypto.encode;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoEncoder {

    private static final String ALGORITHM = "AES";
    private Cipher encodingCipher;

    public void initCrypto(String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        this.encodingCipher = Cipher.getInstance(ALGORITHM);
        this.encodingCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    }

    public String encode(String toEncode) {
        try {
            byte[] encrypted = encodingCipher.doFinal(toEncode.getBytes());
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

}
