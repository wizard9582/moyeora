package com.ssafy.api.controller;

import java.sql.Timestamp;
import java.util.*;

import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserConference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ssafy.api.request.RoomPostReq;
import com.ssafy.api.response.RoomRes;
import com.ssafy.api.service.RoomService;
import com.ssafy.common.auth.SsafyUserDetails;
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
	
	@GetMapping("/roomList")
	@ApiOperation(value = "회의 방 전체 조회", notes = "생성된 방들의 정보를 응답한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<List<RoomRes>> getRoomInfo() {
		return ResponseEntity.status(200).body(roomService.findRooms());
	}

	@GetMapping("/{roomId}")
	@ApiOperation(value = "회의 방 번호로 조회", notes = "방의 정보와 참가자 목록을 가져온다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "방 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<RoomRes> getRoomById(@PathVariable Long roomId) {
		Optional<Conference> conf = roomService.findByRoomId(roomId);
		if(!conf.isPresent())
			return ResponseEntity.status(404).body(new RoomRes());
		Optional<List<User>> member = roomService.findUserByRoomId(roomId);
		RoomRes response = RoomRes.of(conf.get(), member.get(), member.get().size());
		return ResponseEntity.status(200).body(response);
	}

	@GetMapping("/enter")
	@ApiOperation(value = "방에 입장", notes = "방의 정보와 참가자 목록을 가져온다. (room/{roomId}와 동일 결과 리턴")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<RoomRes> enterRoom(@ApiIgnore Authentication authentication, @RequestParam Long roomId) {
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		User user = userDetails.getUser();
		Conference conf = roomService.findByRoomId(roomId).get();
		UserConference uc = new UserConference();
		uc.setUser(user);
		uc.setConference(conf);
		roomService.enter(uc);

		return getRoomById(roomId);
	}

	@DeleteMapping("/leave")
	@ApiOperation(value = "방에서 나감", notes = "방에서 나간다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "방 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<BaseResponseBody> leaveRoom(@ApiIgnore Authentication authentication, @RequestParam Long roomId) {
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		User user = userDetails.getUser();
		Conference conf = roomService.findByRoomId(roomId).get();
		UserConference uc = new UserConference();
		uc.setUser(user);
		uc.setConference(conf);
		roomService.leave(uc);

		// 나간 사람이 방장이라면
		if(conf.getOwnerId().getId() == user.getId()){
			roomService.popRoom(roomId);
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "방장 권한으로 방을 나오며 닫았습니다."));
		}

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "방에서 나왔습니다."));
	}
	
	@PostMapping()
	@ApiOperation(value = "회의 방 생성", notes = "<strong>title과 description</strong>을 작성하여 회의방을 생성한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<Conference> createRoom(
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

		return ResponseEntity.status(200).body(conf);
	}
}
