package com.limaila.bms.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.limaila.bms.common.exception.CommonException;

import java.util.Date;
import java.util.Map;

public class JWTUtil {

    private JWTUtil() {
    }

    public static final long DEFAULT_TOKEN_EXPIRE_TIME = 7200 * 1000;

    private static final String DEFAULT_ISSUER = "BMS-SYSTEM";

    private static final String DEFAULT_CLAIM_VALUE = "userKey";

    /**
     * 生成 Token
     *
     * @param claimValue 存进 token claim 中的 value
     * @param secretKey  密钥
     * @return 编码后的 Json Web 令牌
     */
    public static String generateToken(String claimValue, String secretKey) {
        return generateToken(DEFAULT_CLAIM_VALUE, claimValue, secretKey, DEFAULT_ISSUER, DEFAULT_TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成 Token
     *
     * @param claimValue 存进 token claim 中的 value
     * @param secretKey  密钥
     * @return 编码后的 Json Web 令牌
     */
    public static String generateToken(String claimValue, String secretKey, long tokenExpireTime) {
        return generateToken(DEFAULT_CLAIM_VALUE, claimValue, secretKey, DEFAULT_ISSUER, tokenExpireTime);
    }

    /**
     * 生成 Token
     *
     * @param claimName  存进 token claim 中的 key
     * @param claimValue 存进 token claim 中的 value
     * @param secretKey  密钥
     * @return 编码后的 Json Web 令牌
     */
    public static String generateToken(String claimName, String claimValue, String secretKey) {
        return generateToken(claimName, claimValue, secretKey, DEFAULT_ISSUER, DEFAULT_TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成 Token
     *
     * @param claimName  存进 token claim 中的 key
     * @param claimValue 存进 token claim 中的 value
     * @param secretKey  密钥
     * @return 编码后的 Json Web 令牌
     */
    public static String generateToken(String claimName,
                                       String claimValue,
                                       String secretKey,
                                       String issuer
    ) {
        return generateToken(claimName, claimValue, secretKey, issuer, DEFAULT_TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成 Token
     *
     * @param claimName  存进 token claim 中的 key
     * @param claimValue 存进 token claim 中的 value
     * @param secretKey  密钥
     * @return 编码后的 Json Web 令牌
     */
    public static String generateToken(String claimName,
                                       String claimValue,
                                       String secretKey,
                                       String issuer,
                                       long tokenExpireTime
    ) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + tokenExpireTime);
        return signToken(createToken(claimName, claimValue, issuer, now, expireTime), secretKey);
    }

    /**
     * 生成 Token
     *
     * @param claimValue 存进 token claim 中的 value
     * @param secretKey  密钥
     * @return 编码后的 Json Web 令牌
     */
    public static String generateToken(Map<String, ?> claimValue, String secretKey) {
        return generateToken(DEFAULT_CLAIM_VALUE, claimValue, secretKey, DEFAULT_ISSUER, DEFAULT_TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成 Token
     *
     * @param claimName  存进 token claim 中的 key
     * @param claimValue 存进 token claim 中的 value
     * @param secretKey  密钥
     * @return 编码后的 Json Web 令牌
     */
    public static String generateToken(String claimName, Map<String, ?> claimValue, String secretKey) {
        return generateToken(claimName, claimValue, secretKey, DEFAULT_ISSUER, DEFAULT_TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成 Token
     *
     * @param claimName  存进 token claim 中的 key
     * @param claimValue 存进 token claim 中的 value
     * @param secretKey  密钥
     * @return 编码后的 Json Web 令牌
     */
    public static String generateToken(String claimName,
                                       Map<String, ?> claimValue,
                                       String secretKey,
                                       String issuer
    ) {
        return generateToken(claimName, claimValue, secretKey, issuer, DEFAULT_TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成 Token
     *
     * @param claimName  存进 token claim 中的 key
     * @param claimValue 存进 token claim 中的 value
     * @param secretKey  密钥
     * @return 编码后的 Json Web 令牌
     */
    public static String generateToken(String claimName,
                                       Map<String, ?> claimValue,
                                       String secretKey,
                                       String issuer,
                                       long tokenExpireTime
    ) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + tokenExpireTime);
        return signToken(createToken(claimName, claimValue, issuer, now, expireTime), secretKey);
    }

    /**
     * 解析 Token
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 解码的 Json Web 令牌
     */
    public static DecodedJWT resolveToken(String token, String secretKey) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(DEFAULT_ISSUER).build();
            return jwtVerifier.verify(token);
        } catch (JWTDecodeException ex) {
            throw new CommonException("解析 token 解密错误", ex);
        } catch (SignatureVerificationException ex) {
            throw new CommonException("解析 token 签名无效", ex);
        } catch (TokenExpiredException ex) {
            throw new CommonException("解析 token 失效", ex);
        } catch (Throwable ex) {
            throw new CommonException("解析 token 出现异常", ex);
        }
    }

    /**
     * 从Token中提取信息
     *
     * @param decodedJWT 解码的 Json Web 令牌
     * @param claimName  存进 token claim 中的 key
     * @return 提取出的信息
     */
    public static Claim getClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }


    /**
     * 从Token中提取信息
     *
     * @param decodedJWT 解码的 Json Web 令牌
     * @return 提取出的信息
     */
    public static String getClaimAsString(DecodedJWT decodedJWT) {
        return getClaim(decodedJWT, DEFAULT_CLAIM_VALUE).asString();
    }


    /**
     * 从Token中提取信息
     *
     * @param decodedJWT 解码的 Json Web 令牌
     * @param claimName  存进 token claim 中的 key
     * @return 提取出的信息
     */
    public static String getClaimAsString(DecodedJWT decodedJWT, String claimName) {
        return getClaim(decodedJWT, claimName).asString();
    }


    /**
     * 从Token中提取信息
     *
     * @param decodedJWT 解码的 Json Web 令牌
     * @param claimName  存进 token claim 中的 key
     * @return 提取出的信息
     */
    public static Map<String, ?> getClaimAsMap(DecodedJWT decodedJWT, String claimName) {
        return getClaim(decodedJWT, claimName).asMap();
    }

    private static JWTCreator.Builder createToken(String claimName,
                                                  String claimValue,
                                                  String issuer,
                                                  Date issuedTime,
                                                  Date expireTime
    ) {
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(issuedTime);
        builder.withExpiresAt(expireTime);
        builder.withClaim(claimName, claimValue);
        return builder;
    }

    private static JWTCreator.Builder createToken(String claimName,
                                                  Map<String, ?> claimValue,
                                                  String issuer,
                                                  Date issuedTime,
                                                  Date expireTime
    ) {
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(issuedTime);
        builder.withExpiresAt(expireTime);
        builder.withClaim(claimName, claimValue);
        return builder;
    }

    private static String signToken(JWTCreator.Builder builder, String secretKey) {
        return builder.sign(Algorithm.HMAC256(secretKey));
    }
}
