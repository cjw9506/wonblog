package com.wonblog.api.service;

import com.wonblog.api.domain.Comment;
import com.wonblog.api.domain.Post;
import com.wonblog.api.dto.request.CommentCreate;
import com.wonblog.api.dto.request.CommentEdit;
import com.wonblog.api.dto.request.CommentSearch;
import com.wonblog.api.dto.response.CommentResponse;
import com.wonblog.api.dto.response.PostResponse;
import com.wonblog.api.exception.PostNotFound;
import com.wonblog.api.exception.CommentNotFound;
import com.wonblog.api.repository.PostRepository;
import com.wonblog.api.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void saveComment(Long postId, CommentCreate request) {

        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .writer(request.getWriter())
                .post(post)
                .build();
        post.addComment(comment);

        commentRepository.save(comment);
    }

    public CommentResponse findOne(Long postId, Long commentId) {

        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);

        CommentResponse response = CommentResponse.builder()
                .id(post.getId())
                .content(comment.getContent())
                .writer(comment.getWriter())
                .post(post)
                .build();

        return response;
    }


    public List<CommentResponse> getList(Long postId, CommentSearch commentSearch) {

        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        return commentRepository.findByPost(post, commentSearch).stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .writer(comment.getWriter())
                        .post(post)
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long commentId, CommentEdit commentEdit) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);
        comment.change(commentEdit.getContent(), commentEdit.getWriter(), LocalDateTime.now());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);
        commentRepository.delete(comment);
    }
}
