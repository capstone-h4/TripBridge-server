package com.example.tripbridgeserver.common;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Component

// access 토큰 : 1시간, refresh 토큰 : 하루
public class JwtProvider {

    @Value("${spring.security.jwt.access.expired}")
    private Long accessTokenExpired;

    @Value("${spring.security.jwt.refresh.expired}")
    private Long refreshTokenExpired;

    @Value("${spring.security.jwt.access.secret}")
    private String accessSecretKey;

    @Value("${spring.security.jwt.refresh.secret}")
    private String refreshSecretKey;


    // access 토큰 생성
    public String createAccessToken(String userId, List<String> roles) {

        LocalDateTime expired = LocalDateTime.now()
                .plusSeconds(accessTokenExpired);

        return Jwts.builder()
                .setSubject(userId)
                .claim("roles", roles)
                .setIssuedAt(java.sql.Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(java.sql.Timestamp.valueOf(expired))
                .signWith(SignatureAlgorithm.HS256, accessSecretKey)
                .compact();
    }

    // refresh 토큰 생성 (access 토큰 만료 시 사용)
    public String createRefreshToken(String userId, List<String> roles) {

        LocalDateTime expired = LocalDateTime.now()
                .plusSeconds(refreshTokenExpired);

        return Jwts.builder()
                .setSubject(userId)
                .claim("roles", roles)
                .setIssuedAt(java.sql.Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(java.sql.Timestamp.valueOf(expired))
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();
    }

    // access 토큰 유효성 체크
    public boolean validateAccessToken() {
        return validateToken(resolveAccessToken(), accessSecretKey);
    }


    // access 토큰 유효성 체크
    public boolean validateAccessToken(String jwtToken) {
        return validateToken(jwtToken, accessSecretKey);
    }

    // 토큰 유효성 체크
    public boolean validateRefreshToken(String jwtToken) {
        return validateToken(jwtToken, refreshSecretKey);
    }



    // 토큰 유효한지 체크 (만료일자로)
    private boolean validateToken(String jwtToken, String scret) {
        try {
            if (StringUtils.isEmpty(jwtToken)) return false;
            Jws<Claims> claims = Jwts.parser().setSigningKey(scret).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        } catch (SignatureException | MalformedJwtException e) {
            log.info(e.getMessage());
            return false;
        }
    }

    // 헤더에서 access 토큰 조회
    public String resolveAccessToken() {
        HttpServletRequest request = HttpRequestUtil.getRequest();
        if (request.getHeader("authorization") != null) {
            if (request.getHeader("authorization").startsWith("Bearer ")) {
                return request.getHeader("authorization").substring(7);
            } else {
                throw new MalformedJwtException("유효하지 않은 토큰형식입니다.");
            }
        }

        return null;
    }

    // 헤더에서 refresh 토큰 조회
    public String resolveRefreshToken() {
        HttpServletRequest request = HttpRequestUtil.getRequest();
        if (request.getHeader("refreshToken") != null)
            return request.getHeader("refreshToken").substring(7);
        return null;
    }

    // 사용자 아이디 추출
    public String getUserIdFromAccessToken(String token) {
        try {
            return parseToken(accessSecretKey ,token).getBody().getSubject();
        } catch (SignatureException | MalformedJwtException e) {
            return null;
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    // 토큰 파싱
    private Jws<Claims> parseToken(String secret, String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
    }


}
