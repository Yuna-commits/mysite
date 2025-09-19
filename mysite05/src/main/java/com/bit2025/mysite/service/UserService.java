package com.bit2025.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bit2025.mysite.repository.UserRepository;
import com.bit2025.mysite.vo.UserVo;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void join(UserVo userVo) {
		// password encoding
		String password = userVo.getPassword();
		String passwordEncoded = passwordEncoder.encode(password);
		userVo.setPassword(passwordEncoded);
		
		userRepository.insert(userVo);
	}

	public UserVo getUser(Long id) {
		// 모든 정보 조회 : update
		return userRepository.findById(id);
	}
	
	public UserVo getUser(String email) {
		// login, email check
		return userRepository.findByEmail(email, UserVo.class);
	}
	
	public UserVo getUser(String email, String password) {
		// login interceptor
		return userRepository.findByEmailAndPassword(email, password);
	}

	public void updateUser(UserVo userVo) {
		userRepository.update(userVo);
	}

}
