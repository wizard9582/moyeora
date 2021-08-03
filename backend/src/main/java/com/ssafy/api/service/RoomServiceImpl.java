package com.ssafy.api.service;

import com.ssafy.db.entity.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.db.entity.Conference;
import com.ssafy.db.repository.RoomRepository;
import com.ssafy.db.repository.RoomRepositorySupport;


import java.util.ArrayList;
import java.util.List;

/**
 *	회의 방 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("roomService")
public class RoomServiceImpl implements RoomService {
	@Autowired
	RoomRepository roomRepository;
	
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
}
