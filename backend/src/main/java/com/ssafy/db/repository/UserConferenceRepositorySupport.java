package com.ssafy.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.*;
import com.ssafy.db.entity.ConferenceHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 유저 모델 관련 디비 쿼리 생성을 위한 구현 정의.
 */
@Repository
public class UserConferenceRepositorySupport {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QUserConference qUserConference = QUserConference.userConference;

    @Autowired
    UserConferenceRepository ucRepository;

    public List<UserConference> getUserConferenceByRoomNum(String roomNum){
        Long id = Long.parseLong(roomNum);

        List<UserConference> res = jpaQueryFactory.select(qUserConference).distinct().from(qUserConference)
                .where(qUserConference.conference.id.eq(id)).fetch();
        return res;
    }

    public UserConference enter(UserConference uc){
        UserConference isIn = jpaQueryFactory.select(qUserConference).from(qUserConference)
                                        .where(qUserConference.conference.id.eq(uc.getConference().getId())
                                                .and(qUserConference.user.id.eq(uc.getUser().getId()))).fetchOne();
        if(isIn == null){
            return ucRepository.save(uc);
        }
        return uc;
    }
}
