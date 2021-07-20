package com.ssafy.db.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 컨퍼런스 이력 모델 정의.
 */
@Entity
@Getter
@Setter
@Table(name = "conference_history")
public class ConferenceHistory extends BaseEntity{
	@ManyToOne
    Conference conference;
	
	@ManyToOne
    User user;
	
	short action;
	Timestamp insertedTime;
}
