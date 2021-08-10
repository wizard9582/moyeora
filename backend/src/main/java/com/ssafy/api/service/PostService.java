package com.ssafy.api.service;

import com.ssafy.api.request.PostRegisterPostReq;
import com.ssafy.db.entity.Post;
import com.ssafy.db.entity.User;

import java.util.List;


public interface PostService {
    Post createPost(User user,PostRegisterPostReq postInfo);
    List<Post> getAllPost();
    List<Post> getAllNotice();
    void updatePostByPostId(PostRegisterPostReq postInfo, Long postId);
    void deletePostByPostId(Long postId);
    Post getPost(Long postId);
    List<Post> getPostByUserId(long id);
}
