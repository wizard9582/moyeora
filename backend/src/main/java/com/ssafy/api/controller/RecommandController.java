package com.ssafy.api.controller;



import com.ssafy.db.repository.RecommandRepositorySupport;
import com.ssafy.socket.HelloMessage;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 유저 추천 목록 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "친구 API", tags = {"Friend"})
@RestController
@RequestMapping("/api/v1/search")
public class RecommandController {

	@Autowired
	RecommandRepositorySupport recommandRepositorySupport;

	@GetMapping()
	@ApiOperation(value = "검색하고자 하는 아이디", notes = "유사 아이디 추천")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
    })
	public List<String> getFriendList(@RequestParam String name) {
		System.out.println("name : "+name);
		List<String> recommandFriendList = recommandRepositorySupport.getRecommandFriendList(name);

		return recommandFriendList;
	}

}
