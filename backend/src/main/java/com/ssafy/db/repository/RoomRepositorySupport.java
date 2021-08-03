package com.ssafy.db.repository;

import com.ssafy.db.entity.Conference;
import com.ssafy.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.QConference;

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


}
