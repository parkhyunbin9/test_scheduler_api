package com.daou.api.common.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

import com.daou.api.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenUtils {

	private static final String secretKey = "ZGFvdS10ZXN0LWp3dC1hdXRoZW50aWZpY2F0aW9uLXVzZXItb25seS1wYXNzLWF1dGgtcGxlYXNlLWdvLXdsbC1wbGVhc2UtMjM1aHMK";
	private static final long TOKEN_EXPRIE_SEC = 1000 * 60 * 30;

	public static String generateToken(User user) {

		return Jwts.builder()
			.setSubject(user.getUsername())
			.setHeader(createHeader())
			.setClaims(createClaims(user))
			.setExpiration(setExpireDate())
			.signWith(SignatureAlgorithm.HS256, createSigningKey())
			.compact();
	}

	public static boolean isValidToken(String token) {

		try {
			Claims claims = getClaimsFromToken(token);
			log.info("expireTime = {}", claims.getExpiration());
			log.info("username = {}", claims.get("username"));
			log.info("role = {}", claims.get("role"));
			return true;
		} catch (ExpiredJwtException e) {
			log.error("Token Expired", e);
			return false;
		} catch (JwtException e) {
			log.error("Token Tampered");
			return false;
		} catch (NullPointerException e) {
			log.error("Token is null");
			return false;
		}

	}

	public static String getTokenFromHeader(String header) {
		return header.split(" ")[1];
	}

	private static Date setExpireDate() {
		return new Date(System.currentTimeMillis() + (TOKEN_EXPRIE_SEC));
	}

	private static Map<String, Object> createHeader() {
		Map<String, Object> header = new HashMap<>();

		header.put("typ", "JWT");
		header.put("alg", "HS256");
		header.put("regDate", System.currentTimeMillis());
		return header;
	}

	private static Map<String, Object> createClaims(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", user.getUsername());
		claims.put("role", user.getRole());
		return claims;
	}

	private static Key createSigningKey() {
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
		return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
	}

	private static Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
			.parseClaimsJws(token).getBody();
	}

	private static String getUsernamefromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return (String)claims.get("username");
	}

	private static String getRoleFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return (String)claims.get("role");
	}

}
