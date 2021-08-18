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
@ApiModel("PostRegisterPostRequest")
public class PostRegisterPostReq {
	@ApiModelProperty(name="공지사항", example="post id")
	Long id;
	@ApiModelProperty(name="공지사항", example="공지사항일 경우 값은 1, 일반게시물일 경우 0을 표기함")
	Integer notice;
	@ApiModelProperty(name="게시물 제목", example="게시물 제목 입니다.")
	String title;
	@ApiModelProperty(name="게시물 내용", example="게시물 내용 입니다.")
	String description;
	@ApiModelProperty(name="유저 아이디", example="ssafy")
	String userId;
	@ApiModelProperty(name="유저 아이디", example="ssafy")
	String date;
}
