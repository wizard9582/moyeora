package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 마피아 게임 정의.
 * 회의 정보 (회의 방, 유저 아이디 포함)
 * 해당 역할
 * 죽음 여부
 *
 * 게임이 끝나면 같은 회의방에서 다시시작을 고려하여 delete 작업을 수행해야 한다.
 */
@Entity
@Getter
@Setter
@Table(name = "mafia")
public class Mafia extends BaseEntity {
    @OneToOne
    UserConference userConference;

    String role;
    int status;
}
