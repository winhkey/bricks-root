/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks-root)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bricks.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.KeyFactory.getInstance;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static java.util.Locale.US;
import static java.util.Optional.ofNullable;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static org.bricks.constants.Constants.NumberConstants.NUMBER_1;
import static org.bricks.constants.Constants.StringConstants.ENGLISH_CHARS;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bricks.enums.SignatureSalt;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 加密工具
 *
 * @author fuzy
 *
 */
@Slf4j
@UtilityClass
public class SecurityUtils
{

    /**
     * RSA
     */
    @UtilityClass
    public static class RsaUtils
    {

        /**
         * 签名
         *
         * @param salt 盐值
         * @param msgBuf 签名值
         * @param privateKeyStr 秘钥
         * @return 字节组
         */
        public static byte[] sign(SignatureSalt salt, byte[] msgBuf, String privateKeyStr)
        {
            try
            {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(getDecoder().decode(privateKeyStr));
                PrivateKey privateKey = getInstance("RSA").generatePrivate(keySpec);
                Signature signature = Signature.getInstance(salt.getValue());
                signature.initSign(privateKey);
                signature.update(msgBuf);
                return signature.sign();
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
            return msgBuf;
        }

        /**
         * 校验签名
         *
         * @param salt 盐值
         * @param msgBuf 字符
         * @param sign 签名
         * @param publicKeyStr 秘钥
         * @return 验签是否成功
         */
        public static boolean verify(SignatureSalt salt, byte[] msgBuf, byte[] sign, String publicKeyStr)
        {
            try
            {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(getDecoder().decode(publicKeyStr));
                PublicKey publicKey = getInstance("RSA").generatePublic(keySpec);
                Signature signature = Signature.getInstance(salt.getValue());
                signature.initVerify(publicKey);
                signature.update(msgBuf);
                return signature.verify(sign);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
            return false;
        }

    }

    /**
     * DES
     */
    @UtilityClass
    public static class DesUtils
    {

        /**
         * des
         */
        private static final String DES = "DES";

        /**
         * 解密
         *
         * @param src 密文
         * @return 明文
         */
        public static String decode(String src)
        {
            return decode(src, ENGLISH_CHARS);
        }

        /**
         * 解密
         *
         * @param src 密文
         * @param key key
         * @return 明文
         */
        public static String decode(String src, String key)
        {
            byte[] bytes = null;
            try
            {
                bytes = decrypt(hex2byte(src.getBytes(UTF_8)), key.getBytes(UTF_8));
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
            return ofNullable(bytes).map(String::new)
                    .orElse("");
        }

        /**
         * 加密
         *
         * @param src 明文
         * @return 密文
         */
        public static String encode(String src)
        {
            return encode(src, ENGLISH_CHARS);
        }

        /**
         * 加密
         *
         * @param src 明文
         * @param key key
         * @return 密文
         */
        public static String encode(String src, String key)
        {
            byte[] bytes = null;
            try
            {
                bytes = encrypt(src.getBytes(UTF_8), key.getBytes(UTF_8));
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
            return ofNullable(bytes).map(DesUtils::byte2hex)
                    .orElse("");
        }

        private static byte[] encrypt(byte[] src, byte[] key)
        {
            return process(ENCRYPT_MODE, src, key);
        }

        private static byte[] decrypt(byte[] src, byte[] key)
        {
            return process(DECRYPT_MODE, src, key);
        }

        private static byte[] process(int type, byte[] src, byte[] key)
        {
            byte[] bytes = null;
            try
            {
                SecureRandom sr = new SecureRandom();
                DESKeySpec dks = new DESKeySpec(key);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
                SecretKey secretKey = keyFactory.generateSecret(dks);
                Cipher cipher = Cipher.getInstance(DES);
                cipher.init(type, secretKey, sr);
                bytes = cipher.doFinal(src);
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
            return bytes;
        }

        private static String byte2hex(byte[] b)
        {
            StringBuilder hs = new StringBuilder();
            for (byte value : b)
            {
                String temp = Integer.toHexString(value & 0XFF);
                if (temp.length() == NUMBER_1)
                {
                    hs.append('0');
                }
                hs.append(temp);
            }
            return hs.toString()
                    .toUpperCase(US);

        }

        private static byte[] hex2byte(byte[] b)
        {
            if (b.length % 2 != 0)
            {
                throw new IllegalArgumentException("length not even");
            }
            byte[] b2 = new byte[b.length / 2];
            for (int n = 0; n < b.length; n += 2)
            {
                String item = new String(b, n, 2, UTF_8);
                b2[n / 2] = (byte) Integer.parseInt(item, 16);
            }
            return b2;
        }

    }

    /**
     * HmacSHA256
     */
    @UtilityClass
    public static class HmacSha256Utils
    {

        /**
         * 加密
         *
         * @param message 明文
         * @param key key
         * @param base64 是否base64转码
         * @return 密文
         */
        public static String encode(String message, String key, boolean base64)
        {
            String result = "";
            try
            {
                Mac mac = Mac.getInstance("HmacSHA256");
                SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(UTF_8), "HmacSHA256");
                mac.init(secretKey);
                byte[] bytes = mac.doFinal(message.getBytes(UTF_8));
                result = base64 ? getEncoder().encodeToString(bytes) : new String(bytes);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
            return result;
        }

    }

    /**
     * aes
     */
    @UtilityClass
    public static class AesUtils
    {

        /**
         * 加密
         *
         * @param dataBytes 明文数据
         * @param keyBytes 密钥
         * @return 密文数据
         */
        public static byte[] encrypt(byte[] dataBytes, byte[] keyBytes)
        {
            try
            {
                Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
                int blockSize = cipher.getBlockSize();
                int length = dataBytes.length;
                int mode = length % blockSize;
                if (mode != 0)
                {
                    length = length + (blockSize - mode);
                }
                byte[] plaintext = new byte[length];
                System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
                SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
                cipher.init(ENCRYPT_MODE, keySpec);
                return cipher.doFinal(plaintext);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
            return new byte[0];
        }

        /**
         * 解密
         *
         * @param encryptBytes 密文数据
         * @param keyBytes 密钥
         * @return 明文数据
         */
        public static byte[] decrypt(byte[] encryptBytes, byte[] keyBytes)
        {
            try
            {
                Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
                SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
                cipher.init(DECRYPT_MODE, keySpec);
                return cipher.doFinal(encryptBytes);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
            return new byte[0];
        }

    }

}
