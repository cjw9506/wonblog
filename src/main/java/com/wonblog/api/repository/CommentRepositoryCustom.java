package com.wonblog.api.repository;

import com.wonblog.api.domain.Comment;
import com.wonblog.api.domain.Post;
import com.wonblog.api.dto.request.CommentSearch;
import com.wonblog.api.dto.request.PostSearch;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findByPost(Post post, CommentSearch commentSearch);
}
