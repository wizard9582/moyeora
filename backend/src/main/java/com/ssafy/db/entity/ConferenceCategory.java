package com.ssafy.db.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 컨퍼런스 카테고리 모델 정의.
 */
@Entity
@Getter
@Setter
@Table(name = "conference_category")
public class ConferenceCategory extends BaseEntity{
    String name;
}
