package com.wonblog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonblog.api.domain.Comment;
import com.wonblog.api.domain.Post;
import com.wonblog.api.dto.request.CommentCreate;
import com.wonblog.api.dto.request.CommentEdit;
import com.wonblog.api.repository.CommentRepository;
import com.wonblog.api.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.wonblog.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class CommentControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("게시글에 댓글 작성하기")
    void test1() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();
        postRepository.save(post);

        CommentCreate comment = CommentCreate.builder()
                .content("첫 댓글")
                .writer("정지원 친구")
                .build();

        String json = objectMapper.writeValueAsString(comment);

        this.mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-create",
                        pathParameters(
                        parameterWithName("postId").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("writer").description("작성자")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 단건 조회")
    void test2() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .content("첫 댓글")
                .writer("정지원 친구")
                .post(post)
                .build();

        commentRepository.save(comment);

        this.mockMvc.perform(get("/posts/{postId}/comments/{commentId}", post.getId(), comment.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-inquiry",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("댓글 ID"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("writer").description("댓글 작성자"),
                                fieldWithPath("post").description("게시글"),
                                fieldWithPath("post.id").description("게시글 ID"),
                                fieldWithPath("post.title").description("게시글 제목"),
                                fieldWithPath("post.content").description("게시글 내용"),
                                fieldWithPath("post.author").description("게시글 작성자"),
                                fieldWithPath("post.createdAt").description("게시글 생성일"),
                                fieldWithPath("post.updatedAt").description("게시글 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 댓글 전체 조회")
    void test3() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();
        postRepository.save(post);

        List<Comment> requestComments = IntStream.range(0, 20)
                .mapToObj(i -> Comment.builder()
                        .content("내용" + i)
                        .writer("고객" + i)
                        .post(post)
                        .build())
                .collect(Collectors.toList());

        commentRepository.saveAll(requestComments);

        this.mockMvc.perform(get("/posts/{postId}/comments?page=1&size=5", post.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-inquiries",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("[]").description("댓글 목록"),
                                fieldWithPath("[].id").description("댓글 ID"),
                                fieldWithPath("[].content").description("댓글 내용"),
                                fieldWithPath("[].writer").description("댓글 작성자"),
                                fieldWithPath("[].post").description("게시글"),
                                fieldWithPath("[].post.id").description("게시글 ID"),
                                fieldWithPath("[].post.title").description("게시글 제목"),
                                fieldWithPath("[].post.content").description("게시글 내용"),
                                fieldWithPath("[].post.author").description("게시글 작성자"),
                                fieldWithPath("[].post.createdAt").description("게시글 생성일"),
                                fieldWithPath("[].post.updatedAt").description("게시글 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 수정")
    void test4() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .content("첫 댓글")
                .writer("정지원 친구")
                .post(post)
                .build();

        commentRepository.save(comment);

        CommentEdit commentEdit = CommentEdit.builder()
                .content("댓글 수정이요~~")
                .writer("정지원 친구2")
                .build();

        String json = objectMapper.writeValueAsString(commentEdit);

        this.mockMvc.perform(patch("/posts/{postId}/comments/{commentId}", post.getId(), comment.getId())
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-edit",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("writer").description("게시글 작성자")
                        )

                ));
    }

    @Test
    @DisplayName("게시글 삭제")
    void test5() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .content("첫 댓글")
                .writer("정지원 친구")
                .post(post)
                .build();

        commentRepository.save(comment);

        Comment findComment = commentRepository.findAll().get(0);

        this.mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", post.getId(), findComment.getId())
                        .accept(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-delete",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        )
                ));
    }
}
