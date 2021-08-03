package com.ssafy.api.response;

import java.sql.Timestamp;

import com.ssafy.db.entity.Conference;
import com.ssafy.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 방 정보 조회 API ([GET] /api/v1/room) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@ApiModel("RoomResponse")
public class RoomRes {
	@ApiModelProperty(name="Room ID")
	Long id;	
	@ApiModelProperty(name="Room Owner")
	User ownerId;  
	@ApiModelProperty(name="Room Category")
    String category;
	@ApiModelProperty(name="Room Start Time")
    Timestamp callStartTime;
	@ApiModelProperty(name="Room End Time")
    Timestamp callEndTime;	
	@ApiModelProperty(name="Room Title")
    String title;	
	@ApiModelProperty(name="Room Description")
    String description;	
	@ApiModelProperty(name="Room IsActive")
    boolean isActive;	
	@ApiModelProperty(name="Room IsPrivate")
    boolean isPrivate;
	
	public static RoomRes of(Conference conf) {
		RoomRes res = new RoomRes();
		res.setId(conf.getId());
		res.setOwnerId(conf.getOwnerId());
		res.setCategory(conf.getConferenceCategory());
		res.setCallStartTime(conf.getCallStartTime());
		res.setCallEndTime(conf.getCallEndTime());
		res.setTitle(conf.getTitle());
		res.setDescription(conf.getDescription());
		res.setActive(conf.isActive());
		res.setPrivate(conf.isPrivate());
		return res;
	}
}
