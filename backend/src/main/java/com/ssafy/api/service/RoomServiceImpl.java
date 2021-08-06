package com.ssafy.api.service;

import com.ssafy.db.entity.Conference;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserConference;
import com.ssafy.db.repository.UserConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.db.entity.Conference;
import com.ssafy.db.repository.RoomRepository;
import com.ssafy.db.repository.RoomRepositorySupport;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *	회의 방 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("roomService")
public class RoomServiceImpl implements RoomService {
	@Autowired
	RoomRepository roomRepository;

	@Autowired
	UserConferenceRepository ucRepository;
	
	@Autowired
	RoomRepositorySupport roomRepositorySupport;

	@Override
	public Conference createRoom(Conference conf) {
		return roomRepository.save(conf);
	}


	@Override
	public List<Conference> findRooms() {
		return roomRepository.findAll();
	}

	@Override
	public Optional<Conference> findByRoomId(Long roomId) {
		Optional<Conference> con = roomRepositorySupport.findByRoomId(roomId);
		return con;
	}

	@Override
	public Optional<List<User>> findUserByRoomId(Long roomId) {
		Optional<List<User>> list = roomRepositorySupport.findUserByRoomId(roomId);
		return list;
	}

	@Override
	public UserConference enter(UserConference uc) {
		return ucRepository.save(uc);
	}

	@Override
	public void leave(UserConference uc) {
		roomRepositorySupport.deleteUserConf(uc.getUser(),uc.getConference());
	}
}
