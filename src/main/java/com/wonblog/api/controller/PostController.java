package com.wonblog.api.controller;

import com.wonblog.api.dto.request.PostCreate;
import com.wonblog.api.dto.request.PostEdit;
import com.wonblog.api.dto.request.PostSearch;
import com.wonblog.api.dto.response.PostResponse;
import com.wonblog.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void savePost(@RequestBody @Valid PostCreate request) {
        postService.save(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable Long postId) {
        PostResponse response = postService.findOne(postId);
        return response;
    }

    @GetMapping("/posts")
    public List<PostResponse> getPosts(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void editPost(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(postId, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
