package com.sweetNet.until;

import java.io.Serializable;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.AuthException;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	/* 1hr */
	private static final long EXPIRATION_TIME = 60 * 60 * 1000;
	
	/* JWT SECRET KEY & PUBLIC KEY */
	private static final String TOKEN_SECRET = "sugardaddy";
	private static final String PUBLIC_KEY = "givememoney";

	/**
	 * 簽發JWT
	 */
	public static String generateToken(HashMap<String, String> userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("publicKey", PUBLIC_KEY);
		claims.put("memUuid", userDetails.get("memUuid"));

		return JwtTokenUtils.createToken(claims);

	}

	/**
	 * 簽發JWT，60分鐘過期
	 * 
	 * @param **username**
	 * @param **password**
	 * @return
	 */
	public static String createToken(Map<String, Object> claims) {
		try {
			String publicKey = String.valueOf(claims.get("publicKey"));
			String memUuid = String.valueOf(claims.get("memUuid"));

			// set time
			Date date = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

			// add secret key
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

			// set header
			Map<String, Object> header = new HashMap<>(2);
			header.put("Type", "Jwt");
			header.put("alg", "HS256");

			// return JWTtoken
			return JWT.create().withHeader(header).withClaim("publicKey", publicKey).withClaim("memUuid", memUuid)
					.withExpiresAt(date).sign(algorithm);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 驗證JWT
	 */
	public static Boolean validateToken(String token) throws AuthException, SignatureException {
		Boolean success = false;
		try {
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(token);
			String publicKey = jwt.getClaim("publicKey").asString();

			if (publicKey.equals(PUBLIC_KEY)) {
				success = true;
			}
		} catch (MalformedJwtException e) {
			throw new AuthException("Invalid JWT token");
		} catch (ExpiredJwtException e) {
			throw new AuthException("Expired JWT token");
		} catch (UnsupportedJwtException e) {
			throw new AuthException("Unsupported JWT token");
		} catch (IllegalArgumentException e) {
			throw new AuthException("JWT token compact of handler are invalid");
		}
		return success;
	}

	/**
	 * 取得JWT內的UserData
	 * 
	 * @param 	**token**
	 * @return	**memUuid**
	 */
	public static String getJwtMemUuid(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(token);
			String memUuid = jwt.getClaim("memUuid").asString();
			return memUuid;
		} catch (Exception e) {
			return null;
		}
	}

}