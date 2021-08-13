package com.ssafy.api.service;

import com.ssafy.api.response.RoomRes;
import com.ssafy.db.entity.*;

import com.ssafy.db.repository.*;
import com.ssafy.kurento.Room;
import com.ssafy.kurento.RoomManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.util.*;


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
	RoomManager roomManager;

	@Autowired
	UserConferenceRepository ucRepository;

	@Autowired
	RoomRepositorySupport roomRepositorySupport;

	@Autowired
	ConferenceHistoryRepository chRepository;

	@Autowired
	UserConferenceRepositorySupport ucRepositorySupport;

	@Autowired
	MafiaRepositorySupport mafiaRepositorySupport;

	@Override
	public Conference createRoom(Conference conf) {
		return roomRepository.save(conf);
	}


	@Override
	public List<RoomRes> findRooms() {
		List<Conference> conferenceList = roomRepositorySupport.getActiveRoom();
		List<RoomRes> roomList = new ArrayList<>();
		for(Conference c : conferenceList){
			int count = 0;
			try{
				if (roomManager != null && roomManager.rooms != null && roomManager.rooms.size() != 0 && roomManager.rooms.get(c.getId() + "").getParticipants() != null) {
					count = roomManager.rooms.get(c.getId() + "").getParticipants().size();
				}
			}catch (Exception e){
				System.out.println("findRooms: "+e);
			}
			finally {
				Optional<Mafia> mafia =  mafiaRepositorySupport.getMafiaInConference(c.getId());
				if (count == 10) {
					roomList.add(RoomRes.of(c, null, count, "full"));
				}else if (mafia.isPresent()) {
					roomList.add(RoomRes.of(c, null, count, "playing"));
				} else{
					roomList.add(RoomRes.of(c, null, count));
				}
			}

		}
		return roomList;
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
		return ucRepositorySupport.enter(uc);
	}

	@Override
	public void leave(UserConference uc) {
		UserConference result = roomRepositorySupport.getUCID(uc);
		if(result != null)
			mafiaRepositorySupport.deleteByUCID(result);
		roomRepositorySupport.deleteUserConf(uc.getUser(),uc.getConference());
		ConferenceHistory ch = new ConferenceHistory();
		ch.setUser(uc.getUser());
		ch.setConference(uc.getConference());
		ch.setInsertedTime(new Timestamp(System.currentTimeMillis()));
		chRepository.save(ch);
	}

	@Override
	public long popRoom(long id) {
		return roomRepositorySupport.popRoom(id);
	}
}
