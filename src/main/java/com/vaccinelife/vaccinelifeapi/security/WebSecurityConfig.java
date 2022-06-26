package com.vaccinelife.vaccinelifeapi.security;


import com.vaccinelife.vaccinelifeapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableWebMvc
@Service
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;


    // authenticationManager를 Bean 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/docs/index.html");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin().and()
                .httpBasic().disable() // REST API만을 고려, 기본 설정 해제
                .csrf().disable() // csrf 사용 X
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .mvcMatchers("/docs/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/**").permitAll()
                .mvcMatchers("/api/signup", "/api/login").permitAll()
                .mvcMatchers("/api/mypage/**").permitAll()
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                .and()

                .exceptionHandling()//예외가 발생할 경우
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());//OAuth2AccessDeniedHandler사용  403status로 반응

    }

    // 암호화에 필요한 PasswordEncoder Bean 등록


    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        // 필터 등록
        http
                .headers().frameOptions().sameOrigin().and()
                .httpBasic().disable() // REST API만을 고려, 기본 설정 해제
                .csrf().disable() // csrf 사용 X
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 토큰 기반 인증이므로 세션도 사용 X
//                .and()
//                .formLogin()
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .mvcMatchers(HttpMethod.GET, "/api/**").anonymous()
                .anyRequest().permitAll() // 나머지 요청은 누구나 접근 가능
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter는
        // UsernamePasswordAuthenticationFilter 전에 넣음
    }*/

}