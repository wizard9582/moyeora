package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 방 생성 API ([POST] /api/v1/room) 요청에 필요한 리퀘스트 바디 정의.
 */
@Getter
@Setter
@ApiModel("RoomPostRequest")
public class RoomPostReq {
	@ApiModelProperty(name="방 제목", example="TITLE")
	String title;
	
	@ApiModelProperty(name="방 설명", example="description")
	String description;
	
	@ApiModelProperty(name="방 잠금여부", example="false/true")
	boolean isPrivate;
	
	@ApiModelProperty(name="방 비밀번호", example="ssafy")
	String password;
	
	@ApiModelProperty(name="방 카테고리", example="Mafia")
	String category;
}
