package com.ssafy.db.repository;

import com.ssafy.api.response.RoomRes;
import com.ssafy.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssafy.db.entity.Conference;

import java.util.List;
import java.util.Optional;

/**
 * 회의 방 모델 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.
 */
@Repository
public interface RoomRepository extends JpaRepository<Conference, Long> {
    Optional<Conference> findById(Long id);

//    @Query("select c.*, count(conference_id) as count " +
//            "from user_conference as u right join conference as c " +
//            "on u.conference_id = c.id " +
//            "where c.is_active = true " +
//            "group by c.id")
//    List<RoomRes> getRoomsWithCnt();
}
