package com.ssafy.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.request.PostRegisterPostReq;
import com.ssafy.db.entity.Post;
import com.ssafy.db.entity.QPost;
import com.ssafy.db.entity.QUser;
import com.ssafy.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 유저 모델 관련 디비 쿼리 생성을 위한 구현 정의.
 */
@Repository
public class PostRepositorySupport {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QPost qPost = QPost.post;

    public List<PostRegisterPostReq> getAllPost() {
        List<Post> posts = jpaQueryFactory.select(qPost).from(qPost).
                where(qPost.notice.eq(0)).fetch();
        List<PostRegisterPostReq> res =new ArrayList<>();

        for(Post p : posts){
            PostRegisterPostReq pr= new PostRegisterPostReq();
            pr.setTitle(p.getTitle());
            pr.setNotice(p.getNotice());
            pr.setDescription(p.getDescription());
            pr.setDate(p.getDate());
            pr.setUserId(p.getUser().getUserId());
            res.add(pr);
        }

        return  res;
    }

    public List<PostRegisterPostReq> getAllNotice() {
        List<Post> Notices = jpaQueryFactory.select(qPost).from(qPost).
                where(qPost.notice.eq(1)).fetch();

        List<PostRegisterPostReq> res =new ArrayList<>();

        for(Post p : Notices){
            PostRegisterPostReq pr= new PostRegisterPostReq();
            pr.setTitle(p.getTitle());
            pr.setNotice(p.getNotice());
            pr.setDescription(p.getDescription());
            pr.setDate(p.getDate());
            pr.setUserId(p.getUser().getUserId());
            res.add(pr);
        }
        return res;
    }

    public void deletePostByPostId(Long postId) {
        long result = jpaQueryFactory.delete(qPost).where(qPost.id.eq(postId)).execute();
    }

    public void updatePostByPostId(PostRegisterPostReq postInfo, Long postId) {
        jpaQueryFactory.update(qPost)
                .where(qPost.id.eq(postId))
                .set(qPost.title, postInfo.getTitle())
                .set(qPost.description, postInfo.getDescription())
                .set(qPost.date,postInfo.getDate())
                .execute();
    }

    public List<Post> getPostByUserId(long id){
        List<Post> result = jpaQueryFactory.select(qPost).from(qPost)
                                            .where(qPost.user.id.eq(id)).fetch();
        return result;
    }
}
