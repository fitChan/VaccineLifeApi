package com.vaccinelife.vaccinelifeapi.security;

import com.vaccinelife.vaccinelifeapi.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String secretKey = "";

    // 토큰 유효시간 120분
    private long tokenValidTime = 120 * 60 * 1000L;
    private final UserDetailsServiceImpl userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String userPk, Long id, Set<UserRole> roles, String nickname, Boolean isVaccine, String type, int degree, String gender, String age, String disease, String afterEffect) {
        Claims claims = Jwts.claims().setSubject(userPk);
        // claim : JWT payload 에 저장되는 정보단위
        //토큰으로 넘겨줄 user data 저장 -> 프론트에서 token decode 해서 사용
        claims.put("id",id);
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장
        claims.put("nickname", nickname);
        claims.put("isVaccine",isVaccine);
        claims.put("type",type);
        claims.put("degree",degree);
        claims.put("gender",gender);
        claims.put("age",age);
        claims.put("disease",disease);
        claims.put("afterEffect",afterEffect);


        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)
                // 사용할 암호화 알고리즘과
                // signature에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옴. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }



}
