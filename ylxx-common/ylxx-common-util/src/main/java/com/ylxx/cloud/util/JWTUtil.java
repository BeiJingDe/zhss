package com.ylxx.cloud.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import cn.hutool.core.date.DateUtil;

public class JWTUtil {

	/**
	 * 
	* @Title: createToken
	* @Description: 生成token
	* @param username
	* @param secret
	* @param expiresTime: 单位毫秒
	* @return
	 */
    public static String createToken(String username, String secret, long expiresTime) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                //到期时间
                .withExpiresAt(DateUtil.date(System.currentTimeMillis() + expiresTime))
                //创建一个新的JWT，并使用给定的算法进行标记
                .sign(algorithm);
    }

    /**
     * 
    * @Title: verify
    * @Description: 校验 token 是否正确
    * @param token
    * @param username
    * @param secret
    * @return
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //在token中附带了username信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            //验证 token
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 
    * @Title: getUsername
    * @Description: 获取tocken中的用户名
    * @param token
    * @return
     */
    public static String getUsername(String token) {
        try {
            // TODO 临时开发处理
            if("token".equalsIgnoreCase(token)){
                return "dev";
            }
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
    
}
