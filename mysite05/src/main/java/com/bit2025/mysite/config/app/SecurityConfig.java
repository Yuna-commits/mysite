package com.bit2025.mysite.config.app;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import com.bit2025.mysite.repository.UserRepository;
import com.bit2025.mysite.security.UserDetailsServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
			// 인증되지 않은 사용자는 로그인 페이지로 리다이렉트
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
					.defaultSuccessUrl("/")
					// 파라미터와 함께 리다이렉트 응답하는 방식
					// .failureUrl("/user/login?result=fail")
					// email 표시를 위해 핸들링
					.failureHandler(new AuthenticationFailureHandler() {

						@Override
						public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
								AuthenticationException exception) throws IOException, ServletException {
							request.setAttribute("email", request.getParameter("email"));
							request
								// 현 위치가 필터이기 때문에 DispatcherServlet으로 포워딩(jsp x)
								.getRequestDispatcher("/user/login")
								.forward(request, response);
						}
						
					});
			})
			.logout(configurer -> {
				configurer
					.logoutUrl("/user/logout")
					.logoutSuccessUrl("/");
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
