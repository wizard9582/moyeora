package com.ssafy.api.controller;


import com.ssafy.db.entity.ConferenceHistory;
import com.ssafy.db.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.PostRegisterPostReq;
import com.ssafy.api.service.PostService;
import com.ssafy.api.request.UserLoginPostReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.response.UserLoginPostRes;
import com.ssafy.api.response.UserRes;
import com.ssafy.api.service.UserService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepositorySupport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	PostService postService;

	@Autowired
	UserService userService;
	
	@PostMapping()
	@ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value="회원가입 정보", required = true) UserRegisterPostReq registerInfo) {
		
		//임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
		User user = userService.createUser(registerInfo);
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

	@GetMapping("/history")
	@ApiOperation(value = "회원 본인 정보 조회", notes = "로그인한 회원 본인의 정보를 응답한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<List<ConferenceHistory>> getUserHistory(@ApiIgnore Authentication authentication) {
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		User user = userDetails.getUser();
		List<ConferenceHistory> result = userService.getUserHistory(user.getId());
		return ResponseEntity.status(200).body(result);
	}
	
	@GetMapping("/me")
	@ApiOperation(value = "회원 본인 정보 조회", notes = "로그인한 회원 본인의 정보를 응답한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<UserRes> getUserInfo(@ApiIgnore Authentication authentication) {
		/**
		 * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
		 * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
		 */
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		User user = userService.getUserByUserId(userId);
		
		return ResponseEntity.status(200).body(UserRes.of(user));
	}
	
	@GetMapping("/{userId}")
	@ApiOperation(value = "유저정보 (존재하는 회원 확인용)", notes = "사용 가능한 ID인지 알려준다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 409, message = "이미 존재하는 사용자 ID"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<BaseResponseBody> getUsers(@PathVariable String userId) {
		/**
		 * 존재하는 회원인지 회원 확인용
		 * 이미 존재하는 유저인 경우, 409 에러({"statusCode": "409", "message": "이미 존재하는 사용자 ID 입니다"}) 발생.
		 */
		User user = userService.getUserByUserId(userId);
		if(user != null) {
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 존재하는 사용자 ID 입니다."));
		}
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "사용 가능한 ID 입니다."));
	}
	
	@PatchMapping("/{userId}")
	@ApiOperation(value = "유저 정보 수정", notes = "유저 정보를 수정한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<BaseResponseBody> patchUser(@ApiIgnore Authentication authentication, @PathVariable String userId, @RequestBody User userInfo) {
		/**
		 * 유저 정보를 수정한다.
		 * 
		 */
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String loginUserId = userDetails.getUsername();
		if(loginUserId != userId)
			return ResponseEntity.status(405).body(BaseResponseBody.of(405, "권한 없음"));
		
		User user = userService.getUserByUserId(userId);
		user.setDepartment(userInfo.getDepartment());
		user.setPosition(userInfo.getPosition());
		user.setName(userInfo.getName());
		
		long result = userService.updateUserByUserId(user, userId);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
	
	@DeleteMapping("/{userId}")
	@ApiOperation(value = "유저 정보 삭제", notes = "유저 정보를 삭제한다.(탈퇴)") 
    @ApiResponses({
        @ApiResponse(code = 204, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<BaseResponseBody> deleteUser(@ApiIgnore Authentication authentication, @PathVariable String userId) {
		/**
		 * 유저 정보를 삭제한다.
		 * 회원 탈퇴
		 */
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String loginUserId = userDetails.getUsername();
		if(loginUserId != userId)
			return ResponseEntity.status(405).body(BaseResponseBody.of(405, "권한 없음"));
		
		long result = userService.deleteUserByUserId(userId);
		return ResponseEntity.status(200).body(BaseResponseBody.of(204, "Success"));
	}

}
