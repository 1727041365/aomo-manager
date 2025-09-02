package com.yupi.springbootinit.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    
    /**
     * 生成MD5哈希值
     * @param input 输入字符串
     * @return 32位小写十六进制MD5哈希值
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不可用", e);
        }
    }
    
    /**
     * 验证输入是否与MD5哈希值匹配
     * @param input 原始输入
     * @param hash MD5哈希值
     * @return 是否匹配
     */
    public static boolean verify(String input, String hash) {
        return md5(input).equals(hash.toLowerCase());
    }
}