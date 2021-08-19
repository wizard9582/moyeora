package com.ssafy.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * 유저 모델 정의.
 */
@Entity
@Getter
@Setter
public class Invite extends BaseEntity{
    String fromUser;
    String toUser;
    String url;
    int month;
    int day;
    int hour;
    int min;
}



