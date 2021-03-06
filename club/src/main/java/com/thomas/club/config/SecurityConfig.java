package com.thomas.club.config;

import com.thomas.club.security.filter.ApiCheckFilter;
import com.thomas.club.security.filter.ApiLoginFilter;
import com.thomas.club.security.handler.ApiLoginFailHandler;
import com.thomas.club.security.handler.ClubLoginSuccessHandler;
import com.thomas.club.security.service.ClubUserDetailsService;
import com.thomas.club.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {      // 별도의 시큐리티 설정 클래스 , 핵심 클래스

    // Remember me 설정
    @Autowired
    private ClubUserDetailsService userDetailsService;

    // 패스워드 암호화
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
        http.authorizeRequests()
            .antMatchers("/sample/all").permitAll()
            .antMatchers("/sample/member").hasRole("USER")
            .antMatchers("/sample/admin").hasRole("ADMIN");
        */

        // 인가,인증에 문제 발생하면, 로그인 화면
        http.formLogin();

        // <form> 태그에서는 CSRF 토큰이 권장 , REST 방식 등에서는 CSRF 토큰을 발행하지 않는 경우도 있다.
        http.csrf().disable();

        // CSRF 토큰을 사용할 때는 반드시 POST 방식으로만 로그아웃을 처리한다.
        // CSRF 토큰을 비활성화 시키면 GET ('/logout') 으로도 로그아웃이 처리된다.
        http.logout();

        // google cloud platform  ->  API 및 서비스  ->  사용자 인증 정보
        http.oauth2Login().successHandler(successHandler());

        // Remember Me
        http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService);     // 7일

        // ApiCheckFilter 를 UsernamePasswordAuthenticationFilter 이전에 동작하도록 설정
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public ClubLoginSuccessHandler successHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    // 기본적으로 가장 마지막 필터로 작동
    // '/notes/..' 로 시작하는 경우에만 동작하도록 설정
    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter("/notes/**/*", jwtUtil());
    }

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager());

        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

/*  UserDetailsService 를 사용하면, 아래 인증에 대한 임시 코드는 필요 없음
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 사용자 계정은 user1
        auth.inMemoryAuthentication().withUser("user1")
                // 1111 패스워드 인코딩 결과 - 테스트 코드 참고
                .password("$2a$10$gXklK.tDOEbOtE5zX7.mTuykYZKnPauAVGZcPP0sqU.ch1ACXIx3S")
                .roles("USER");
    }
*/

}