package com.ssafy.api.service;

import com.ssafy.db.entity.Conference;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserConference;
import com.ssafy.db.repository.UserConferenceRepository;

import java.util.List;
import java.util.Optional;

/**
 *	회의 방 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface RoomService {

	Conference createRoom(Conference conf);

    List<Conference> findRooms();

    Optional<Conference> findByRoomId(Long roomId);

    Optional<List<User>> findUserByRoomId(Long roomId);

    UserConference enter(UserConference uc);

    void leave(UserConference uc);

}
