package com.ssafy.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class MafiaRepositorySupport {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QMafia qMafia = QMafia.mafia;
    QUserConference qUserConference = QUserConference.userConference;

    @Autowired
    MafiaRepository mafiaRepository;

    public List<Mafia> getPlayerByRoomId(long roomId){
        List<Mafia> result = jpaQueryFactory.select(qMafia).from(qMafia)
                // 현재 게임 방에 참여하고 살아있는 사람
                .where(qMafia.userConference.conference.id.eq(roomId)
                        .and(qMafia.status.eq(0)))
                .fetch();
        return result;
    }

    @Transactional
    public void deleteMafia(long roomId){
        List<UserConference> ucList = jpaQueryFactory.select(qUserConference)
                .from(qUserConference)
                .where(qUserConference.id.eq(roomId)).fetch();

        for(UserConference uc : ucList){
            jpaQueryFactory.delete(qMafia)
                    .where(qMafia.userConference.id.eq(uc.getId())).execute();
        }
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
    @Transactional
    public Optional<Mafia> getMafiaInConference (long conferenceId) {
        return Optional.ofNullable(jpaQueryFactory.select(qMafia).from(qMafia)
                .where(qMafia.userConference.conference.id.eq(conferenceId)).fetchFirst());
    }

    @Transactional
    public void saveRole(Mafia mafia){
        Mafia isIn = jpaQueryFactory.select(qMafia).from(qMafia).where(qMafia.userConference.id.eq(mafia.getUserConference().getId())).fetchOne();
        if(isIn == null)
            mafiaRepository.save(mafia);
        else{
            jpaQueryFactory.update(qMafia)
                            .where(qMafia.userConference.id.eq(mafia.getUserConference().getId()))
                            .set(qMafia.status,0)
                            .set(qMafia.role,mafia.getRole()).execute();
        }
    }
}
