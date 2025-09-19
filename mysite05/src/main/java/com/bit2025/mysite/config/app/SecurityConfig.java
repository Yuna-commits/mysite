package com.bit2025.mysite.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import com.bit2025.mysite.repository.UserRepository;
import com.bit2025.mysite.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return webSecurity -> webSecurity.httpFirewall(new DefaultHttpFirewall());
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.formLogin(configurer -> {
				configurer
					// 사용자 정의 로그인 페이지 사용
					.loginPage("/user/login")
					// 로그인 url 등록
					.loginProcessingUrl("/user/auth")
					// 로그인 파라미터 명시
					.usernameParameter("email")
					.passwordParameter("password")
					// 성공 시 메인으로 리다이렉트
					.defaultSuccessUrl("/");
			})
			.authorizeHttpRequests(registry -> {
				// Access Control List
				registry
					// 권한이 필요한 페이지 등록
					.requestMatchers(new RegexRequestMatcher("^/admin/?.*", null)).authenticated()
					.requestMatchers(new RegexRequestMatcher("^/user/update$", null)).authenticated()
					.requestMatchers(new RegexRequestMatcher("^/board/?(write|delete|modify|reply).*$", null)).authenticated()
					.anyRequest().permitAll();
			});
		
		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		// DB에서 사용자 정보를 가져옴
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}
	
	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return new UserDetailsServiceImpl(userRepository);
	}
	
}
