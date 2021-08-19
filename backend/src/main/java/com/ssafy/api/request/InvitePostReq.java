package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 유저 회원가입 API ([POST] /api/v1/users) 요청에 필요한 리퀘스트 바디 정의.
 */
@Getter
@Setter
@ApiModel("InvitePostRequest")
public class InvitePostReq {
	@ApiModelProperty(name="초대 받은 사람", example="ssafy2")
	String toUser;
	@ApiModelProperty(name="초대 보낸 사람", example="ssafy")
	String fromUser;
	@ApiModelProperty(name="유저 소속", example="https://localhost:8083/game/mafia/2")
	String url;

}
