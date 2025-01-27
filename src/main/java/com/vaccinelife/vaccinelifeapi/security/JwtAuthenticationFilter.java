package com.vaccinelife.vaccinelifeapi.security;


import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request); //토큰 정보 가져옴

            if (!jwt.isEmpty() && jwtTokenProvider.validateToken(jwt)) {
                String userId = jwtTokenProvider.getUserIdFromJwt(jwt);

                UserAuthentication authentication = new UserAuthentication(userId, null, null);
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 난 더이상 부가할것이 없음..

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                throw new RuntimeException("인증이 되지 않은 사용자입니다.");
            }
        }catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
//            if(StringUtils.isEmpty(jwt)){
//                request.setAttribute("unauthorization", "401 error 인증키 없음.");
//            }
//            if(jwtTokenProvider.validateToken(jwt)){
                request.setAttribute("unauthorization", "인증이 되지 않은 사용자입니다.");
//            }
        }
        filterChain.doFilter(request,response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (!StringUtils.isEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        throw new RuntimeException("인증되지 않은 사용자입니다.");
        //추상클래스 받는거 확인.
    }
}