package com.ssafy.api.controller;

import com.ssafy.api.response.MafiaRes;
import com.ssafy.api.response.RoomRes;
import com.ssafy.api.service.RoomService;
import com.ssafy.db.entity.Mafia;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "마피아 게임 API", tags = {"Conference"})
@RestController
@RequestMapping("/api/v1/mafia")
public class MafiaController {

    @Autowired
    RoomService roomService;

    @GetMapping("/police")
    @ApiOperation(value = "경찰 권한으로 직업 조회", notes = "게임 시작과 동시에 참가자들의 직업을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "방 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<List<MafiaRes>> getRoles(@RequestParam long roomId) {
        List<Mafia> roles = roomService.getRoles(roomId);
        List<MafiaRes> response = new ArrayList<>();

        for(Mafia mafia : roles){
            response.add(MafiaRes.of(mafia.getUserConference().getUser().getId(),mafia.getUserConference().getUser().getUserId(),mafia.getRole()));
        }
        return ResponseEntity.status(200).body(response);
    }
}
