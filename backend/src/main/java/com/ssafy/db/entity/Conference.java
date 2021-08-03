package com.ssafy.db.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * 컨퍼런스 모델 정의.
 */
@Entity
@Getter
@Setter
public class Conference extends BaseEntity{
	@ManyToOne
	@JoinColumn(name="owner_id")
    User ownerId;
    
    String conferenceCategory;
    Timestamp callStartTime;
    Timestamp callEndTime;
    String title;
    String description;
    boolean isActive;
    boolean isPrivate;
}
