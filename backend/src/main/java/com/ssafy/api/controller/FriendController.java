package com.ssafy.api.controller;


import com.ssafy.api.request.InvitePostReq;
import com.ssafy.api.request.RelationPostReq;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.db.entity.Friend;
import com.ssafy.db.entity.Invite;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.FriendRepository;
import com.ssafy.db.repository.InviteRepository;
import com.ssafy.db.repository.UserRepositorySupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * 초대 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "친구 API", tags = {"friend"})
@RestController
@RequestMapping("/api/v1/relation")
public class FriendController {

	@Autowired
	UserRepositorySupport userRepositorySupport;

	@Autowired
	FriendRepository friendRepository;

	@Autowired
	InviteRepository inviteRepository;

	@GetMapping()
	@ApiOperation(value = "header의 token 값 넣어 보냄", notes = "현 유저와 맺은 친구 목록 받아옴")
	public List<String> getFriendList(@ApiIgnore Authentication authentication) {

		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String fromUser = userDetails.getUsername();

		List<Friend> friendList = friendRepository.getFriendsByFromUser(fromUser);

		List<String> res = new ArrayList<>();

		for(Friend f : friendList){
			res.add(f.getToUser());
		}

		return res;
	}

	@PostMapping()
	@ApiOperation(value = "header에는 token, body에는 친구 맺고자 하는 사람", notes = "친구 요청하기")
	public String inviteFriend(@ApiIgnore Authentication authentication,@RequestBody RelationPostReq relationPostReq) {
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String fromUser = userDetails.getUsername();
		String toUser = relationPostReq.getToUser();
		Optional<User> user = userRepositorySupport.findUserByUserId(toUser);
		if(!user.isPresent()) return "fail"; //요청한 사용자는 존재하지 않음

		Friend friend = friendRepository.getFriendByFromUserAndToUser(fromUser,toUser);
		if(friend!=null) return "exist"; // 이미 친구 상태

		Invite invite = new Invite();
		invite.setFromUser(fromUser);
		invite.setToUser(toUser);

		inviteRepository.save(invite);
		return "success";
	}


	@PostMapping("/make")
	@ApiOperation(value = "header에는 token", notes = "친구 관계 만들기")
	public String makeFriend(@ApiIgnore Authentication authentication,@RequestBody RelationPostReq relationPostReq) {
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String fromUser = userDetails.getUsername();
		String toUser = relationPostReq.getToUser();

		Friend relation = new Friend();
		relation.setFromUser(fromUser);
		relation.setToUser(toUser);
		friendRepository.save(relation);

		Friend revRelation = new Friend();
		revRelation.setFromUser(toUser);
		revRelation.setToUser(fromUser);

		friendRepository.save(revRelation);

		try {
			inviteRepository.deleteInviteByToUserAndFromUser(toUser, fromUser);
		}catch (Exception e){
			System.out.println("warning : delete friend request");
		}

		try {
			inviteRepository.deleteInviteByToUserAndFromUser(fromUser, toUser);
		}catch (Exception e){
			System.out.println("warning : delete friend request");
		}

		return "success";
	}

	@DeleteMapping("/delete")
	@ApiOperation(value = "친구 요청된 사람, 친구 요청한 사람", notes = "친구 관계 삭제하는 동작")
	public String deleteFriendRelation(@ApiIgnore Authentication authentication,@RequestBody RelationPostReq relationPostReq) {

		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String fromUser = userDetails.getUsername();
		String toUser = relationPostReq.getToUser();

		Friend friend = friendRepository.getFriendByFromUserAndToUser(fromUser,toUser);
		if(friend==null) return "fail";

		friendRepository.deleteFriendByFromUserAndToUser(fromUser,toUser);
		friendRepository.deleteFriendByFromUserAndToUser(toUser, fromUser);

		return "success";
	}

}
