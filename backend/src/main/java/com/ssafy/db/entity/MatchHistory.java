package com.ssafy.db.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 게임 전적 테이블
 * 회원 정보
 * 게임 타입
 * 총 게임 횟수
 * 이긴 횟수
 *
 */
@Entity
@Getter
@Setter
@Table(name = "match_history")
public class MatchHistory extends BaseEntity {

    @OneToOne
    User user;

    String type;
    int total;
    int win;
}

