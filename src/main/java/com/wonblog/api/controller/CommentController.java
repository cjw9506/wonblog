package com.wonblog.api.controller;

import com.wonblog.api.dto.request.CommentCreate;
import com.wonblog.api.dto.request.CommentEdit;
import com.wonblog.api.dto.request.CommentSearch;
import com.wonblog.api.dto.response.CommentResponse;
import com.wonblog.api.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public void makeComment(@PathVariable Long postId, @RequestBody @Valid CommentCreate request) {
        commentService.saveComment(postId, request);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public CommentResponse getOneComment(@PathVariable Long postId, @PathVariable Long commentId) {
        CommentResponse response = commentService.findOne(postId, commentId);

        return response;
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> getComments(@PathVariable Long postId, @ModelAttribute CommentSearch commentSearch) {
        return commentService.getList(postId, commentSearch);
    }

    @PatchMapping("/posts/{postId}/comments/{commentId}")
    public void editComment(@PathVariable Long commentId, @RequestBody @Valid CommentEdit commentEdit) {
        commentService.edit(commentId, commentEdit);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
