package com.ssafy.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.MatchHistory;
import com.ssafy.db.entity.QMatchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class MatchHistoryRepositorySupport {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QMatchHistory qMatchHistory = QMatchHistory.matchHistory;

    @Autowired
    MatchHistoryRepository matchHistoryRepository;

    @Transactional
    public List<MatchHistory> getMatchHistory(long userId) {
        List<MatchHistory> history = jpaQueryFactory.select(qMatchHistory).from(qMatchHistory)
                .where(qMatchHistory.user.id.eq(userId)).fetch();
        return history;
    }


    @Transactional
    public MatchHistory getMatchHistoryByRole(long userId, String role) {
        MatchHistory mh = jpaQueryFactory.select(qMatchHistory).from(qMatchHistory)
                .where(qMatchHistory.user.id.eq(userId)
                .and(qMatchHistory.type.eq(role))).fetchOne();
        return mh;
    }

    @Transactional
    public long updateTotalMatchHistory(MatchHistory mh, long userId, String role) {
        long result = jpaQueryFactory.update(qMatchHistory)
                .where(qMatchHistory.user.id.eq(userId)
                .and(qMatchHistory.type.eq(role)))
                .set(qMatchHistory.total, mh.getTotal()+1)
                .execute();
        return result;
    }

    @Transactional
    public long updateWinMatchHistory(MatchHistory mh, long userId, String role) {
        long result = jpaQueryFactory.update(qMatchHistory)
                .where(qMatchHistory.user.id.eq(userId)
                        .and(qMatchHistory.type.eq(role)))
                .set(qMatchHistory.win, mh.getWin()+1).execute();
        return result;
    }

}
