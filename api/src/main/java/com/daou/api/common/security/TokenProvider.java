package com.daou.api.common.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.TokenDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {

	private final Key key;
	@Value(value = "${jwt.access-token-expire-sec}")
	private long ACCESS_TOKEN_EXPIRE_SEC;
	@Value(value = "${jwt.refresh-token-expire-sec}")
	private long REFRESH_TOKEN_EXPIRE_SEC;

	public TokenProvider(@Value(value = "${jwt.secret-key}") String secretKey) {
		byte[] decodedKey = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(decodedKey);
	}

	public TokenDto generateTokenDto(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		long now = new Date().getTime();

		//AccessToken 생성
		Date accessTokenExpireIn = new Date(now + ACCESS_TOKEN_EXPIRE_SEC);
		String accessToken = Jwts.builder()
			.setSubject(authentication.getName())
			.claim(AuthConst.AUTH_KEY, authorities)
			.setExpiration(accessTokenExpireIn)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

		String refreshToken = Jwts.builder()
			.setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_SEC))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

		return TokenDto.builder()
			.grantType(AuthConst.BEARER_TYPE)
			.accessToken(accessToken)
			.accessTokenExpiresIn(accessTokenExpireIn.getTime())
			.refreshToken(refreshToken)
			.build();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);

		if (Objects.isNull(claims.get(AuthConst.AUTH_KEY))) {
			throw new CommonException(ExceptionCode.INVALID_TOKEN_NO_AUTHORITY);
		}

		Collection<? extends GrantedAuthority> authorities = Arrays.stream(
				claims.get(AuthConst.AUTH_KEY).toString().split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		UserDetails principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build()
				.parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}
