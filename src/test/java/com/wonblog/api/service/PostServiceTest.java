package com.wonblog.api.service;

import com.wonblog.api.domain.Post;
import com.wonblog.api.dto.request.PostCreate;
import com.wonblog.api.dto.request.PostEdit;
import com.wonblog.api.dto.request.PostSearch;
import com.wonblog.api.dto.response.PostResponse;
import com.wonblog.api.exception.PostNotFound;
import com.wonblog.api.repository.PostRepository;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;
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
    @DisplayName("글 작성")
    @Transactional
    void test1() {
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();

        postService.save(postCreate);

        assertThat(1L).isEqualTo(postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertThat("제목입니다.").isEqualTo(post.getTitle());
        assertThat("내용입니다.").isEqualTo(post.getContent());
        assertThat("정지원").isEqualTo(post.getAuthor());
    }



    @Test
    @DisplayName("글 1개 조회")
    void test2() {

        Post request = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postRepository.save(request);

        assertThat(postRepository.count()).isEqualTo(1L);
        PostResponse response = postService.findOne(request.getId());
        assertThat(response.getTitle()).isEqualTo("제목입니다.");
        assertThat(response.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3() {

        List<Post> requestPosts = IntStream.range(0, 10)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        List<PostResponse> posts = postService.getList(postSearch);

        assertThat(10L).isEqualTo(posts.size());
        assertThat("foo9").isEqualTo(posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 수정")
    @Transactional
    void test4() {

        Post request = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .author("정지원")
                .build();

        postRepository.save(request);

        PostEdit postEdit = PostEdit.builder()
                .title("변경된 제목입니다.")
                .content("변경된 내용입니다.")
                .author("정지원")
                .build();

        postService.edit(request.getId(), postEdit);

        Post changedPost = postRepository.findById(request.getId())
                        .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다. id = " + request.getId()));

        Assertions.assertThat(changedPost.getTitle()).isEqualTo("변경된 제목입니다.");
        Assertions.assertThat(changedPost.getContent()).isEqualTo("변경된 내용입니다.");
    }

    @Test
    @DisplayName("글 삭제")
    @Transactional
    void test5() {
        Post post = Post.builder()
                .title("안녕")
                .content("안녕")
                .author("지원")
                .build();

        postRepository.save(post);

        Post find = postRepository.findAll().get(0);

        postService.delete(find.getId());

        Assertions.assertThat(postRepository.count()).isEqualTo(0L);
    }

    @Test
    @DisplayName("글 조회 - 없는 ID(예외)")
    void test6() {
        Post post = Post.builder()
                .title("안녕")
                .content("안녕")
                .author("지원")
                .build();

        postRepository.save(post);


        org.junit.jupiter.api.Assertions.assertThrows(PostNotFound.class, () ->
                postService.findOne(post.getId() + 1L));
    }
}