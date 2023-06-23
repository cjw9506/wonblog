package com.wonblog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonblog.api.domain.Post;
import com.wonblog.api.dto.request.PostCreate;
import com.wonblog.api.dto.request.PostEdit;
import com.wonblog.api.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.wonblog.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("글 등록")
    @Transactional
    void test1() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();

        String json = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("author").description("작성자")
                        )
                        ));
    }

    @Test
    @DisplayName("글 단건 조회")
    void test2() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();
        postRepository.save(post);

        Post findPost = postRepository.findAll().get(0);

        this.mockMvc.perform(get("/posts/{postId}", findPost.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("author").description("작성자"),
                                fieldWithPath("createdAt").description("생성일")
                        )
                ));
    }


    @Test
    @DisplayName("글 전체 조회")
    void test3() throws Exception {

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("title" + i)
                        .content("content" + i)
                        .author("test")
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        this.mockMvc.perform(get("/posts?page=1&size=5")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiries",
                        responseFields(
                                fieldWithPath("[]").description("게시글 목록"),
                                fieldWithPath("[].id").description("게시글 ID"),
                                fieldWithPath("[].title").description("제목"),
                                fieldWithPath("[].content").description("내용"),
                                fieldWithPath("[].author").description("작성자"),
                                fieldWithPath("[].createdAt").description("생성일")
                        )
                ));
    }

    @Test
    @DisplayName("글 수정")
    @Transactional
    void test4() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목")
                .content("내용")
                .author("지원")
                .build();

        Post findPost = postRepository.findAll().get(0);

        String json = objectMapper.writeValueAsString(postEdit);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/posts/{postId}", findPost.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-edit",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("author").description("작성자")
                        )
                ));
    }

    @Test
    @DisplayName("글 삭제")
    @Transactional
    void test5() throws Exception {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();
        postRepository.save(post);

        Post findPost = postRepository.findAll().get(0);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/posts/{postId}", findPost.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-delete",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        )
                ));
    }


}
