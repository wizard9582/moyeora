package com.ssafy.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.*;
import com.ssafy.socket.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

/**
 * 유저 추천 디비 쿼리 생성을 위한 구현 정의.
 */
@Repository
public class RecommandRepositorySupport {
	 @Autowired
	 private JPAQueryFactory jpaQueryFactory;
	 QUser qUser = QUser.user;

	public List<String> getRecommandFriendList(String name){
		String str =name+"%";
	 	List<User> userList = jpaQueryFactory.select(qUser).from(qUser).where(qUser.userId.like(str)).limit(5).fetch();

	 	List<String> result = new ArrayList<>();

	 	for(User u : userList){
	 		result.add(u.getUserId());
		}

	 	return result;
	}


}
