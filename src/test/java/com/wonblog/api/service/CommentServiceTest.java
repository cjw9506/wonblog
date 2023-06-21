package com.wonblog.api.service;

import com.wonblog.api.domain.Comment;
import com.wonblog.api.domain.Post;
import com.wonblog.api.dto.request.CommentEdit;
import com.wonblog.api.dto.request.CommentSearch;
import com.wonblog.api.dto.response.CommentResponse;
import com.wonblog.api.repository.PostRepository;
import com.wonblog.api.repository.CommentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentService commentService;

    @BeforeEach
    void beforeClean() {
        postRepository.deleteAll();
    }
    @AfterEach
    void afterClean() {
        postRepository.deleteAll();
    }
    @Test
    @DisplayName("해당하는 게시글에 댓글 저장하기")
    @Transactional
    void test1() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .author("정지원")
                .build();

        postRepository.save(post);

        Post findPost = postRepository.findAll().get(0);

        Comment comment = Comment.builder()
                .content("재밌네요.")
                .writer("지원정")
                .post(findPost)
                .build();

        commentRepository.save(comment);

        Comment findComment = commentRepository.findAll().get(0);

        Assertions.assertEquals("재밌네요.", findComment.getContent());
        //Assertions.assertEquals("정지원", findReply.getPost().getAuthor());
    }

    @Test
    @DisplayName("댓글 하나 조회하기")
    void test2() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .author("정지원")
                .build();

        postRepository.save(post);

        Post findPost = postRepository.findAll().get(0);

        Comment comment = Comment.builder()
                .content("재밌네요.")
                .writer("지원정")
                .post(findPost)
                .build();

        commentRepository.save(comment);

        Comment findComment = commentRepository.findAll().get(0);

        assertEquals(commentRepository.count(), 1L);
    }

    @Test
    @DisplayName("댓글 여러개 조회하기")
    void test3() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .author("정지원")
                .build();

        postRepository.save(post);

        Post findPost = postRepository.findAll().get(0);

        List<Comment> requestComments = IntStream.range(0, 10)
                .mapToObj(i -> Comment.builder()
                        .content("foo" + i)
                        .writer("jiwon" + i)
                        .post(post)
                        .build())
                .collect(Collectors.toList());

        commentRepository.saveAll(requestComments);

        CommentSearch commentSearch = CommentSearch.builder()
                .page(1)
                .size(10)
                .build();

        List<CommentResponse> comments = commentService.getList(findPost.getId(), commentSearch);

        assertThat(10L).isEqualTo(comments.size());
        assertThat("foo9").isEqualTo(comments.get(0).getContent());
    }

    @Test
    @DisplayName("댓글 하나 수정하기")
    @Transactional
    void test4() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .author("정지원")
                .build();

        postRepository.save(post);

        Post findPost = postRepository.findAll().get(0);

        Comment comment = Comment.builder()
                .content("재밌네요.")
                .writer("지원정")
                .post(findPost)
                .build();

        commentRepository.save(comment);

        Comment findComment = commentRepository.findAll().get(0);

        CommentEdit editComment = CommentEdit.builder()
                .content("재미없어요..")
                .writer("지언")
                .build();

        commentService.edit(findComment.getId(), editComment);

        Assertions.assertEquals(findComment.getContent(), "재미없어요..");
    }

    @Test
    @DisplayName("댓글 하나 삭제하기")
    @Transactional
    void test5() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .author("정지원")
                .build();

        postRepository.save(post);

        Post findPost = postRepository.findAll().get(0);

        Comment comment = Comment.builder()
                .content("재밌네요.")
                .writer("지원정")
                .post(findPost)
                .build();

        commentRepository.save(comment);

        Comment findComment = commentRepository.findAll().get(0);

        commentService.deleteComment(findComment.getId());

        Assertions.assertEquals(commentRepository.count(), 0L);
    }

}