package com.wonblog.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wonblog.api.domain.Comment;
import com.wonblog.api.domain.Post;
import com.wonblog.api.domain.QComment;
import com.wonblog.api.dto.request.CommentSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.wonblog.api.domain.QComment.*;


@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findByPost(Post post, CommentSearch commentSearch) {
        return jpaQueryFactory.selectFrom(comment)
                .where(comment.post.eq(post))
                .limit(commentSearch.getSize())
                .offset(commentSearch.getOffset())
                .orderBy(comment.id.desc())
                .fetch();
    }
}
