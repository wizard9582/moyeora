package com.ssafy.db.repository;

import com.ssafy.db.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 초대 모델 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.
 */
@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> getFriendsByFromUser(String fromUser);
    Friend getFriendByFromUserAndToUser(String fromUser,String toUser);
    void  deleteFriendByFromUserAndToUser(String fromUser,String toUser);
}
