package com.ssafy.api.controller;


import com.ssafy.api.request.PostRegisterPostReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.response.UserRes;
import com.ssafy.api.service.PostService;
import com.ssafy.api.service.UserService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Post;
import com.ssafy.db.entity.User;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "게시물 API", tags = {"Post"})
@RestController
@RequestMapping("/api/v1/post")
public class PostController {
	@Autowired
	PostService postService;
	@Autowired
	UserService userService;


	@DeleteMapping("/{postId}")
	@ApiOperation(value = "postId", notes = "헤더에 JWT 넣고 파라미터로 게시물 id를 보내게 되면 게시물의 id로 게시물을 삭제한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 400, message = "실패, 글쓴이가 아님"),
	})
	public ResponseEntity<BaseResponseBody> deletePost(@ApiIgnore Authentication authentication, @PathVariable Long postId) {
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		User user = userService.getUserByUserId(userId);

		Post post = postService.getPost(postId);

		if(!post.getUser().getUserId().equals(user.getUserId())){
			return ResponseEntity.status(200).body(BaseResponseBody.of(400, "권한이 없습니다."));
		}

		postService.deletePostByPostId(postId);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

	@PatchMapping("/update/{postId}")
	@ApiOperation(value = "postId", notes = "게시물을 수정한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
	})
	public ResponseEntity<BaseResponseBody> updatePost(@PathVariable Long postId,@RequestBody PostRegisterPostReq postInfo) {
		postService.updatePostByPostId(postInfo,postId);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

	@GetMapping("/posts/{id}")
	@ApiOperation(value = "id", notes = "특정 유저가 쓴 게시물만 보여주게 한다.")
	public List<Post> getUserPosts(@PathVariable String id){
		return postService.getPostByUserId(Long.parseLong(id));
	}

	@GetMapping("/")
	@ApiOperation(value = "", notes = "게시물을 보여주게 한다.")
	public List<PostRegisterPostReq> getPosts(){
		return postService.getAllPost();
	}

	@GetMapping("/notice")
	@ApiOperation(value = "", notes = "공지사항을 보여주게 한다.")
	public List<PostRegisterPostReq> getNotice(){
		return postService.getAllNotice();
	}

	@PostMapping("/register")
	@ApiOperation(value = "", notes =  "유저가 게시물을 등록하게 한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
	})
	public ResponseEntity<BaseResponseBody> registerPost(@RequestBody PostRegisterPostReq postInfo){
		User user = userService.getUserByUserId(postInfo.getUserId());
		postService.createPost(user,postInfo);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

	@GetMapping("/writer/{postId}")
	@ApiOperation(value = "postId", notes =  "글쓴이인지 판별한다")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 400, message = "실패"),
	})
	public ResponseEntity<BaseResponseBody> getValidWriter(@ApiIgnore Authentication authentication, @PathVariable Long postId){
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		User user = userService.getUserByUserId(userId);

		if(!postService.getPost(postId).getUser().getUserId().equals(user.getUserId())){
			return ResponseEntity.status(200).body(BaseResponseBody.of(400, "글쓴이 아님"));
		}

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "성공"));
	}
}
