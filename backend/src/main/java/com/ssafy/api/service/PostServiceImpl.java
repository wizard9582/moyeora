package com.ssafy.api.service;

import com.ssafy.api.request.PostRegisterPostReq;
import com.ssafy.db.entity.Post;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.PostRepository;
import com.ssafy.db.repository.PostRepositorySupport;
import com.ssafy.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("postService")
public class PostServiceImpl implements PostService{

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostRepositorySupport postRepositorySupport;

    @Override
    public Post createPost(User user, PostRegisterPostReq postInfo) {
        Post post = new Post();

        post.setNotice(postInfo.getNotice());
        post.setTitle(postInfo.getTitle());
        post.setDescription(postInfo.getDescription());
        post.setUser(user);

       return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPost() {
        return postRepositorySupport.getAllPost();
    }

    @Override
    public List<Post> getAllNotice() {
        return postRepositorySupport.getAllNotice();
    }


    @Override
    public void updatePostByPostId(PostRegisterPostReq postInfo, Long postId) {
        postRepositorySupport.updatePostByPostId(postInfo, postId);
    }

    @Override
    public void deletePostByPostId(Long postId) {
        postRepositorySupport.deletePostByPostId(postId);
    }

    @Override
    public Post getPost(Long postId) {
        return postRepository.findById(postId.intValue()).get();
    }

    @Override
    public List<Post> getPostByUserId(long id) {
        return postRepositorySupport.getPostByUserId(id);
    }
}
