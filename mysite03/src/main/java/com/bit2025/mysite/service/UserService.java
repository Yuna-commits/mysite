package com.bit2025.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit2025.mysite.repository.UserRepository;
import com.bit2025.mysite.vo.UserVo;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public void join(UserVo userVo) {
		userRepository.insert(userVo);
	}

	public UserVo getUser(Long id) {
		// 모든 정보 조회
		return userRepository.findById(id);
	}

	public UserVo getUser(UserVo userVo) {
		// id, name 조회
		return userRepository.findByEmailAndPassword(userVo.getEmail(), userVo.getPassword());
	}

	public void updateUser(UserVo userVo) {
		userRepository.update(userVo);
	}

}
