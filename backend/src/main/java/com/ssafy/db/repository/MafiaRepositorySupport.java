package com.ssafy.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class MafiaRepositorySupport {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QMafia qMafia = QMafia.mafia;
    QUserConference qUserConference = QUserConference.userConference;

    public List<Mafia> getPlayerByRoomId(long roomId){
        List<Mafia> result = jpaQueryFactory.select(qMafia).from(qMafia)
                .where(qMafia.userConference.conference.id.eq(roomId)).fetch();
        return result;
    }

    @Transactional
    public void deleteMafia(long roomId){
        jpaQueryFactory.delete(qMafia).where(qMafia.userConference.conference.id.eq(roomId)).execute();
    }

    @Transactional
    public long deleteByUCID(UserConference uc){
        return jpaQueryFactory.delete(qMafia).where(qMafia.userConference.id.eq(uc.getId())).execute();
    }

    @Transactional
    public void killUser(long userId, long conferenceId) {
        UserConference isIn = jpaQueryFactory.select(qUserConference).from(qUserConference)
                .where(qUserConference.conference.id.eq(conferenceId)
                        .and(qUserConference.user.id.eq(userId))).fetchOne();
        jpaQueryFactory.update(qMafia)
                .where(qMafia.userConference.id.eq(isIn.getId()))
                .set(qMafia.status , 1)
                .execute();

    }
}