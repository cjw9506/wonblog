package com.wonblog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonblog.api.domain.Comment;
import com.wonblog.api.domain.Post;
import com.wonblog.api.dto.request.CommentCreate;
import com.wonblog.api.dto.request.CommentEdit;
import com.wonblog.api.dto.request.PostCreate;
import com.wonblog.api.repository.PostRepository;
import com.wonblog.api.repository.CommentRepository;
import com.wonblog.api.service.PostService;
import com.wonblog.api.service.CommentService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void beforeClean() {
        postRepository.deleteAll();
    }
    @AfterEach
    void afterClean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 한개 작성하기")
    void test1() throws Exception {
        PostCreate post = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();

        postService.save(post);

        Post findPost = postRepository.findAll().get(0);

        CommentCreate request = CommentCreate.builder()
                .content("하하하")
                .writer("지원정")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts/{postId}/comments", findPost.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/posts/{postId}/comments 요청시 comment 값은 필수다.")
    void test2() throws Exception {

        PostCreate post = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();

        postService.save(post);

        Post findPost = postRepository.findAll().get(0);

        CommentCreate request = CommentCreate.builder()
                .content("")
                .writer("지원정")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts/{postId}/comments", findPost.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.content").value("내용을 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 1개 조회")
    void test3() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();

        postRepository.save(post);

        Comment comment = Comment.builder()
                .content("하하하")
                .writer("지원정")
                .post(post)
                .build();

        commentRepository.save(comment);

        mockMvc.perform(get("/posts/{postId}/comments/{commentId}", post.getId(), comment.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("하하하"))
                .andExpect(jsonPath("$.writer").value("지원정"))
                .andExpect(jsonPath("$.post.title").value("제목입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 여러개 조회")
    void test4() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();

        postRepository.save(post);

        List<Comment> requestComments = IntStream.range(0, 20)
                .mapToObj(i -> Comment.builder()
                        .content("foo" + i)
                        .writer("bar" + i)
                        .post(post)
                        .build())
                .collect(Collectors.toList());


        commentRepository.saveAll(requestComments);

        mockMvc.perform(get("/posts/{postId}/comments?page=1&size=10", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].content").value("foo19"))
                .andExpect(jsonPath("$[0].writer").value("bar19"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 1개 수정")
    @Transactional
    void test5() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();

        postRepository.save(post);

        Comment comment = Comment.builder()
                .content("하하하")
                .writer("지원정")
                .post(post)
                .build();

        commentRepository.save(comment);

        CommentEdit editComment = CommentEdit.builder()
                .content("웃지마")
                .writer("Dr.XX")
                .build();



        mockMvc.perform(patch("/posts/{postId}/comments/{commentId}", post.getId(), comment.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editComment)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 1개 삭제")
    @Transactional
    void test6() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();

        postRepository.save(post);

        Comment comment = Comment.builder()
                .content("하하하")
                .writer("지원정")
                .post(post)
                .build();

        commentRepository.save(comment);

        mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", post.getId(), comment.getId())
                        .contentType(APPLICATION_JSON)
                        )
                .andExpect(status().isOk())
                .andDo(print());
    }




}