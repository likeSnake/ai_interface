package com.jcl.aidemo.Util;

import com.jcl.aidemo.bean.User;
import com.jcl.aidemo.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Random;


public class RandomStringGenerator {
    // 可选的字符集：大写字母、小写字母和数字
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    // 设置密钥
    private static final String SECRET_KEY = "ocGNPIbsToOHRnYXxWGdXpMVKHDmxLzFqgxUfasYgzHfOzZmFUCIOabppYFJRfvvhZKeQBNumPWeVKUqRGXbOzHNERgavuFUtDxxvmGKpgIFXcqSzThHyuxg";
    private static final String REFRESH_SECRET_KEY = "LEjFQnVZyyHPhCOikaVBvYwDyCIvazwfJezbCUPgODoAJrZbtjvUcnsmiGGdmQutAqjQPRssDgRHptzlYMfhfkZHEhhBdwWYhfQULvOKoqVrmBxfqEFkxfqP";
    private static final long ACCESS_TOKEN_EXPIRATION = 3600000;    // 刷新令牌一小时
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 访问令牌有效期七天

    // 生成指定长度的随机字符串
    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 从字符集中随机选择一个字符
            int index = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(index));
        }

        return stringBuilder.toString();
    }

    // 生成访问令牌（短期有效）
    public static String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getPhone_number())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))  // 设置过期时间为1小时
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // 生成刷新令牌（长期有效）
    public static String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getPhone_number())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION)) // 七天
                .signWith(SignatureAlgorithm.HS512, REFRESH_SECRET_KEY)
                .compact();
    }

    // 刷新访问令牌
    public static String refreshAccessToken(String refreshToken,UserService userService) {
        // 验证刷新令牌
        Claims claims = Jwts.parser()
                .setSigningKey(REFRESH_SECRET_KEY)
                .parseClaimsJws(refreshToken)
                .getBody();

        String userPhone = claims.getSubject();
        User user = userService.getUserByPhone(userPhone);

        // 生成新的访问令牌
        return generateAccessToken(user);
    }

    // 验证令牌是否过期
    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("验证令牌已过期");
            return null; // 捕获到过期异常，表示令牌已过期
        } catch (Exception e) {
            System.out.println("验证令牌不存在");
            return null; // 其他异常视为令牌无效
        }
    }

    // 验证刷新令牌是否过期
    public static Claims parseRefreshToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(REFRESH_SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("刷新令牌已过期");
            return null; // 捕获到过期异常，表示令牌已过期
        } catch (Exception e) {
            System.out.println("刷新令牌不存在");
            return null; // 其他异常视为令牌无效
        }
    }
}
