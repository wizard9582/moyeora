package com.ssafy.db.repository;

import com.ssafy.db.entity.MatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Long> {
    //List<MatchHistory> getMatchHistoriesBy
}
