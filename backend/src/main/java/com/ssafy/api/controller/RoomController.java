package com.ssafy.api.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.RoomPostReq;
import com.ssafy.api.response.RoomRes;
import com.ssafy.api.service.RoomService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Conference;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 회의 방 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "회의방 API", tags = {"Conference"})
@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

	@Autowired
	RoomService roomService;
	
	// 회의방 비밀번호 모음집
	static Map<Long, String> passwordZip = new HashMap<>();
	
	static Date currTime;
	
	@PostMapping()
	@ApiOperation(value = "회의 방 생성", notes = "<strong>title과 description</strong>을 작성하여 회의방을 생성한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<RoomRes> createRoom(
			@ApiIgnore Authentication authentication, 
			@RequestBody @ApiParam(value="생성할 방 정보", required = true) RoomPostReq req) {
		
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		Conference conf = new Conference();
		conf.setOwnerId(userDetails.getUser());
		conf.setActive(true);
		conf.setCallStartTime(new Timestamp(System.currentTimeMillis()));
		
		conf.setConferenceCategory(req.getCategory());
		conf.setDescription(req.getDescription());
		conf.setPrivate(req.isPrivate());
		conf.setTitle(req.getTitle());
		
		Conference newRoom = roomService.createRoom(conf);
		
		if(req.isPrivate()) {
			passwordZip.put(newRoom.getId(), req.getPassword());
		}
		
		return ResponseEntity.status(200).body(RoomRes.of(conf));
	}
}
