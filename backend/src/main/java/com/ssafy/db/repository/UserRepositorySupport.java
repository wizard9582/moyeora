package com.ssafy.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.ConferenceHistory;
import com.ssafy.db.entity.QConferenceHistory;
import com.ssafy.db.entity.QUser;
import com.ssafy.db.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 유저 모델 관련 디비 쿼리 생성을 위한 구현 정의.
 */
@Repository
public class UserRepositorySupport {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QUser qUser = QUser.user;
    QConferenceHistory qHistory = QConferenceHistory.conferenceHistory;

    public Optional<User> findUserByUserId(String userId) {
        User user = jpaQueryFactory.select(qUser).from(qUser)
                .where(qUser.userId.eq(userId)).fetchOne();
        if(user == null) return Optional.empty();
        return Optional.ofNullable(user);
    }
    
    public long updateUserByUserId(User userInfo, String userId) {
    	long result = jpaQueryFactory.update(qUser)
    			.where(qUser.userId.eq(userId))
    			.set(qUser.department, userInfo.getDepartment())
    			.set(qUser.position, userInfo.getPosition())
    			.set(qUser.name, userInfo.getName()).execute();
    	return result;
    }
    
    public long deleteUserByUserId(String userId) {
    	long result = jpaQueryFactory.delete(qUser).where(qUser.userId.eq(userId)).execute();
    	return result;
    }

    public List<ConferenceHistory> getUserHistory(long id){
        List<ConferenceHistory> result = jpaQueryFactory.select(qHistory).from(qHistory)
                                        .where(qHistory.user.id.eq(id)).fetch();
        return result;
    }
}
