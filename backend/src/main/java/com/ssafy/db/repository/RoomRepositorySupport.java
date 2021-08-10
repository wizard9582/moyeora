package com.ssafy.db.repository;

import com.ssafy.api.response.RoomRes;
import com.ssafy.db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * 회의 방 모델 관련 디비 쿼리 생성을 위한 구현 정의.
 */
@Repository
public class RoomRepositorySupport{
	 @Autowired
	 private JPAQueryFactory jpaQueryFactory;
	 QConference qCon = QConference.conference;
	 QUser qUser = QUser.user;
	 QUserConference qUC = QUserConference.userConference;

	 public Optional<Conference> findByRoomId(Long roomId){
		 Conference conf = jpaQueryFactory.select(qCon).from(qCon)
				 .where(qCon.id.eq(roomId)).fetchOne();
		 if(conf == null) return Optional.empty();
		 return Optional.ofNullable(conf);
	 }

	public Optional<List<User>> findUserByRoomId(Long roomId){
		List<User> list = jpaQueryFactory.select(qUser).from(qUC)
				.where(qUC.conference.id.eq(roomId)).fetch();
		if(list == null) return Optional.empty();
		return Optional.ofNullable(list);
	}

	@Transactional
	public Long deleteUserConf(User user, Conference conf){
		Long result = jpaQueryFactory.delete(qUC).where(qUC.conference.eq(conf).and(qUC.user.eq(user))).execute();
		return result;
	}

	@Transactional
	public long popRoom(long id){
	 	long result = jpaQueryFactory.update(qCon)
				.where(qCon.id.eq(id))
				.set(qCon.isActive, false)
				.set(qCon.callEndTime, new Timestamp(System.currentTimeMillis())).execute();
	 	return result;
	}

	public List<Conference> getActiveRoom(){
	 	List<Conference> result = jpaQueryFactory.select(qCon).from(qCon).where(qCon.isActive.eq(true)).fetch();
	 	return result;
	}

//	public List<RoomRes> getActiveRoomWithCnt(){
//		List<RoomRes> result = jpaQueryFactory.selectFrom(qCon).select(qUC.conference.count().as("count"))
//								.leftJoin(qUC.conference, qCon)
//								.on(qUC.id.eq(qCon.id)).where(qCon.isActive.eq(true)).groupBy(qCon.id).fetch();
//		return result;
//	}
}
