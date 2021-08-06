package com.ssafy.api.service;

import com.ssafy.api.response.RoomRes;
import com.ssafy.db.entity.Conference;

import java.util.List;

/**
 *	회의 방 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface RoomService {

	Conference createRoom(Conference conf);

    List<RoomRes> findRooms();

}
