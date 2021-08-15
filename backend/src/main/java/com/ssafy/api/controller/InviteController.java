package com.ssafy.api.controller;


import com.ssafy.api.request.InvitePostReq;
import com.ssafy.api.request.RelationPostReq;
import com.ssafy.api.response.InviteRes;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Friend;
import com.ssafy.db.entity.Invite;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.FriendRepository;
import com.ssafy.db.repository.InviteRepository;
import com.ssafy.db.repository.UserRepositorySupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * 초대 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "초대 API", tags = {"invite"})
@RestController
@RequestMapping("/api/v1/invite")
public class InviteController {
	/*
	Invite DB
	ID, 보낸 사람, 받는 사람, link, 시각,
	 */

	@Autowired
	UserRepositorySupport userRepositorySupport;

	@Autowired
	InviteRepository inviteRepository;

	@Autowired
	FriendRepository friendRepository;

	@PostMapping()
	@ApiOperation(value = "초대하는 사람의 아이디", notes = "초대 기능 구현")
	public String inviteFriend(@RequestBody InvitePostReq invitePostReq) {
		Optional<User> user = userRepositorySupport.findUserByUserId(invitePostReq.getToUser());
		if(!user.isPresent()) return "fail";

		Calendar cal = Calendar.getInstance();
		Invite invite = new Invite();

		invite.setFromUser(invitePostReq.getFromUser());
		invite.setToUser(invitePostReq.getToUser());
		invite.setMonth(cal.get(Calendar.MONTH)+1);
		invite.setDay(cal.get(Calendar.DATE));
		invite.setHour(cal.get(Calendar.HOUR_OF_DAY));
		invite.setMin(cal.get(Calendar.MINUTE));
		invite.setUrl(invitePostReq.getUrl());

		/*
		초대 받은 사람에게 Json 형태로 메세지 보냄
		  { id :  ,
           fromUser : ,
           toUser : ,
           url :  ,
           hour : ,
           min :  }
		 */
		inviteRepository.save(invite);

		return "success";
	}

	@DeleteMapping("/delete/{inviteId}")
	@ApiOperation(value = "초대 정보 삭제", notes = "초대 메세지를 DB에서 삭제한다.")
	public String deleteInviteMessage(@PathVariable Long inviteId) {
		try {
			inviteRepository.deleteById(inviteId);
		}
		catch (IllegalArgumentException e){
			System.out.println("초대 메세지 삭제 실패");
			return "fail";
		}

		return "success";
	}

	@GetMapping("/list")
	@ApiOperation(value = "header의 token 값 넣어 보냄", notes = "초대 메세지들을 DB에서 가져옴.")
	public List<Invite> getUserInviteList(@ApiIgnore Authentication authentication) {

		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String loginUserId = userDetails.getUsername();

		List<Invite> res = inviteRepository.getInviteByToUser(loginUserId);

		return res;
	}
}
