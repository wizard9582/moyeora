package com.ssafy.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.QMafia;
import com.ssafy.db.entity.UserConference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class MafiaRepositorySupport {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QMafia qMafia = QMafia.mafia;

    @Transactional
    public long deleteByUCID(UserConference uc){
        return jpaQueryFactory.delete(qMafia).where(qMafia.userConference.id.eq(uc.getId())).execute();
    }
}
