package com.vaccinelife.vaccinelifeapi.security;

import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
    private final UserRepository userRepository;
    private static final String JWT_SECRET = "secretKey";
    // 토큰 유효시간 120분
    private static final int JWT_EXPIRATION_MS = 604800000;
    private final UserService userService;

    // 객체 초기화, secretKey를 Base64로 인코딩
//    @PostConstruct
//    protected void init() {
//        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
//    }

    // JWT 토큰 생성
    public String createToken(Authentication  authentication) {
        String username = authentication.getName();

        Claims claims = Jwts.claims().setSubject((String) authentication.getPrincipal());

        claims.put("id", username);
//        claims.put("roles", userRole);
//        claims.put("nickname", user.getNickname());
//        claims.put("isVaccine", user.getIsVaccine());
//        claims.put("type", user.getType());
//        claims.put("degree", user.getDegree());
//        claims.put("gender", user.getGender());
//        claims.put("age", user.getAge());
//        claims.put("disease", user.getDisease());
//        claims.put("afterEffect", user.getAfterEffect());


        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + JWT_EXPIRATION_MS)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                // 사용할 암호화 알고리즘과
                // signature에 들어갈 secret값 세팅
                .compact();
    }

    // 토큰에서 회원 정보 추출
    public String getUserIdFromJwt(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }



}
