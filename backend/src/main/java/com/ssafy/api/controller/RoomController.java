package com.ssafy.api.controller;

import java.util.HashMap;
import java.util.Map;

import com.ssafy.api.service.RoomServiceImpl;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Conference;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.service.RoomService;

import io.swagger.annotations.Api;
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

	@GetMapping("/roomList")
	@ApiOperation(value = "회의 방 전체 조회", notes = "생성된 방들의 정보를 응답한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> getRoomInfo() {


	}
	// 회의방 비밀번호 모음집
	static Map<Integer, String> passwordZip = new HashMap<>();
	
	
}
