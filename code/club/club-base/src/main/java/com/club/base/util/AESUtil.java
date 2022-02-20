package com.club.base.util;

import com.club.base.constant.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

/**
 * AES的加密和解密
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class AESUtil {

    //算法
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes -
     * @param radix - 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return java.lang.String - 转换后的字符串
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static String binary(byte[] bytes, int radix){
        // 这里的1代表正数
        return new BigInteger(1, bytes).toString(radix);
    }

    /**
     * base64Encode
     * @param bytes - 待编码的byte[]
     * @return java.lang.String - 编码后的base 64 code
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static String base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base64Decode
     * @param base64Code - 待解码的base 64 code
     * @return byte[] - 解码后的byte[]
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null : new Base64().decode(base64Code);
    }

    /**
     * aesEncryptToBytes AES加密
     * @param content - 待加密的内容
     * @param encryptKey - 加密密钥
     * @return byte[] - 加密后的byte[]
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

        return cipher.doFinal(content.getBytes(StringPool.DEFAULT_ENCODING));
    }

    /**
     * AES加密为base 64 code
     * @param content - 待加密的内容
     * @param encryptKey - 加密密钥
     * @return java.lang.String - 加密后的base 64 code
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * aesDecryptByBytes
     * @param encryptBytes - 待解密的byte[]
     * @param decryptKey - 解密密钥
     * @return java.lang.String - 解密后的String
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * aesDecrypt
     * @param encryptStr - 待解密的base 64 code
     * @param decryptKey - 解密密钥
     * @return java.lang.String - 解密后的string
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

}
