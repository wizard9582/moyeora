package com.ssafy.api.service;

import com.ssafy.api.response.RoomRes;
import com.ssafy.db.entity.Conference;
import com.ssafy.kurento.RoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.db.repository.RoomRepository;
import com.ssafy.db.repository.RoomRepositorySupport;

import java.util.*;

/**
 *	회의 방 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("roomService")
public class RoomServiceImpl implements RoomService {
	@Autowired
	RoomRepository roomRepository;

	@Autowired
	RoomManager roomManager;

	@Autowired
	RoomRepositorySupport roomRepositorySupport;

	@Override
	public Conference createRoom(Conference conf) {
		return roomRepository.save(conf);
	}


	@Override
	public List<RoomRes> findRooms() {
		List<Conference> conferenceList = roomRepository.findAll();
		List<RoomRes> roomList = new ArrayList<>();

		for(Conference c : conferenceList){
			int count = 0;
			if(roomManager.rooms.size()!=0 &&roomManager.rooms.get(c.getId()+"").getParticipants()!=null ){
				count = roomManager.rooms.get(c.getId()+"").getParticipants().size();
			}
			roomList.add(RoomRes.of(c,null,count));
		}
		return roomList;
	}
}
