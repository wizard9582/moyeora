package com.ssafy.db.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 유저-컨퍼런스 모델 정의.
 */
@Entity
@Getter
@Setter
@Table(name = "user_conference")
public class UserConference extends BaseEntity{
	@ManyToOne
    Conference conference;
	
	@ManyToOne
    User user;
}
