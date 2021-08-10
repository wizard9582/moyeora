package com.ssafy.api.service;

import com.ssafy.db.entity.ConferenceHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRepositorySupport;

import java.util.List;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserRepositorySupport userRepositorySupport;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();
		user.setUserId(userRegisterInfo.getId());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		user.setName(userRegisterInfo.getName());
	    user.setDepartment(userRegisterInfo.getDepartment());
	    user.setPosition(userRegisterInfo.getPosition());
		return userRepository.save(user);
	}

	@Override
	public User getUserByUserId(String userId) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
		if(!userRepositorySupport.findUserByUserId(userId).isPresent())
			return null;
		User user = userRepositorySupport.findUserByUserId(userId).get();
		return user;
	}

	@Override
	public long updateUserByUserId(User userInfo, String userId) {
		return userRepositorySupport.updateUserByUserId(userInfo, userId);
	}

	@Override
	public long deleteUserByUserId(String userId) {
		return userRepositorySupport.deleteUserByUserId(userId);
	}

	@Override
	public List<ConferenceHistory> getUserHistory(long id) {
		return userRepositorySupport.getUserHistory(id);
	}
}
