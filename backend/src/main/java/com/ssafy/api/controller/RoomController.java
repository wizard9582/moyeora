package com.ssafy.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.service.RoomService;

import io.swagger.annotations.Api;

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
	static Map<Integer, String> passwordZip = new HashMap<>();
	
	
}
