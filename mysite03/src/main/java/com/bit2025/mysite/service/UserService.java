package com.bit2025.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit2025.mysite.repository.UserRepository;
import com.bit2025.mysite.vo.UserVo;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public void join(UserVo userVo) {
		userRepository.insert(userVo);
	}

	public UserVo getUser(Long id) {
		// 모든 정보 조회 : update
		return userRepository.findById(id);
	}
	
	public UserVo getUser(String email) {
		return userRepository.findByEmail(email);
	}
	
	public UserVo getUser(String email, String password) {
		// login
		return userRepository.findByEmailAndPassword(email, password);
	}

	public void updateUser(UserVo userVo) {
		userRepository.update(userVo);
	}

}
