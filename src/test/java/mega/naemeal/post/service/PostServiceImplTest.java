package mega.naemeal.post.service;

import mega.naemeal.comment.entity.Comment;
import mega.naemeal.comment.repository.CommentRepository;
import mega.naemeal.enums.Category;
import mega.naemeal.post.dto.request.PostRequestDto;
import mega.naemeal.post.dto.response.AllPostResponseDto;
import mega.naemeal.post.dto.response.PostResponseDto;
import mega.naemeal.post.entity.Post;
import mega.naemeal.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("글 등록 테스트")
    void createPostTest() {
        // given
        String userId = "user123";
        String title = "Test Post";
        String content = "Lorem ipsum dolor sit amet";
        String imgPath = "/path/to/image.jpg";

        PostRequestDto requestDto = PostRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        Post mockPost = Post.builder()
                .category(Category.RECIPE)
                .userId(userId)
                .title(title)
                .content(content)
                .image(imgPath)
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(mockPost);

        // when
        PostResponseDto result = postService.createPost(requestDto, imgPath, userId);

        // then
        assertNotNull(result);
        assertEquals(mockPost.getPostId(), result.getPostId());
        assertEquals(mockPost.getTitle(), result.getTitle());
        assertEquals(mockPost.getContent(), result.getContent());
        assertEquals(mockPost.getImage(), result.getImage());

        verify(postRepository, times(1)).save(any(Post.class));
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("글 삭제 테스트")
    void deletePostTest() {
        // given
        Long postId = 1L;
        String userId = "user123";

        Post mockPost = Post.builder()
                .postId(postId)
                .userId(userId)
                .build();

        when(postRepository.findByPostId(postId)).thenReturn(Optional.of(mockPost));

        // when
        assertDoesNotThrow(() -> postService.deletePost(postId));

        // then
        verify(postRepository, times(1)).findByPostId(postId);
        verify(postRepository, times(1)).delete(mockPost);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("전체 글 조회 테스트")
    void getAllRecipePostsTest() {
        // given
        List<Post> mockPosts = new ArrayList<>();
        mockPosts.add(Post.builder()
                .postId(1L)
                .userId("user123")
                .title("Post 1")
                .content("Content 1")
                .build());
        mockPosts.add(Post.builder()
                .postId(2L)
                .userId("user456")
                .title("Post 2")
                .content("Content 2")
                .build());

        when(postRepository.findAll()).thenReturn(mockPosts);

        // when
        List<AllPostResponseDto> result = postService.getAllRecipePosts();

        // then
        assertEquals(mockPosts.size(), result.size());
        for (int i = 0; i < mockPosts.size(); i++) {
            AllPostResponseDto expectedDto = AllPostResponseDto.builder()
                    .postId(mockPosts.get(i).getPostId())
                    .title(mockPosts.get(i).getTitle())
                    .content(mockPosts.get(i).getContent())
                    .build();
            AllPostResponseDto actualDto = result.get(i);
            assertEquals(expectedDto.getPostId(), actualDto.getPostId());
            assertEquals(expectedDto.getTitle(), actualDto.getTitle());
            assertEquals(expectedDto.getContent(), actualDto.getContent());
        }

        verify(postRepository, times(1)).findAll();
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("선택 조회 테스트")
    void getPostTest() {
        // given
        Long postId = 1L;
        Post mockPost = Post.builder()
                .postId(postId)
                .userId("user123")
                .title("Test Post")
                .content("Test Content")
                .build();

        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(Comment.builder()
                .commentId(1L)
                .postId(postId)
                .userId("user456")
                .comments("Comment 1")
                .build());
        mockComments.add(Comment.builder()
                .commentId(2L)
                .postId(postId)
                .userId("user789")
                .comments("Comment 2")
                .build());

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(commentRepository.findByPostId(postId)).thenReturn(mockComments);

        // when
        PostResponseDto result = postService.getPost(postId);

        // then
        assertNotNull(result);
        assertEquals(mockPost.getPostId(), result.getPostId());
        assertEquals(mockPost.getTitle(), result.getTitle());
        assertEquals(mockPost.getContent(), result.getContent());
        assertEquals(mockComments.size(), result.getCommentList().size());

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findByPostId(postId);
        verifyNoMoreInteractions(postRepository, commentRepository);
    }

    @Test
    @DisplayName("전체 글 조회 테스트")
    void getAllPostsTest() {
        // given
        String userId = "user123";
        List<Post> mockPosts = new ArrayList<>();
        mockPosts.add(Post.builder()
                .postId(1L)
                .userId(userId)
                .title("Post 1")
                .content("Content 1")
                .build());
        mockPosts.add(Post.builder()
                .postId(2L)
                .userId(userId)
                .title("Post 2")
                .content("Content 2")
                .build());

        when(postRepository.findAllByUserId(userId)).thenReturn(mockPosts);

        // when
        List<AllPostResponseDto> result = postService.getAllPosts(userId);

        // then
        assertNotNull(result);
        assertEquals(mockPosts.size(), result.size());
        for (int i = 0; i < mockPosts.size(); i++) {
            Post mockPost = mockPosts.get(i);
            AllPostResponseDto responseDto = result.get(i);
            assertEquals(mockPost.getPostId(), responseDto.getPostId());
            assertEquals(mockPost.getTitle(), responseDto.getTitle());
            assertEquals(mockPost.getContent(), responseDto.getContent());
        }

        verify(postRepository, times(1)).findAllByUserId(userId);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("글 수정 테스트")
    void updatePostTest() {
        // given
        Long postId = 1L;
        String userId = "user123";
        PostRequestDto requestDto = PostRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();
        String imgPath = "path/to/image.jpg";
        Post mockPost = Post.builder()
                .postId(postId)
                .userId(userId)
                .title("Original Title")
                .content("Original Content")
                .build();

        when(postRepository.findByPostId(postId)).thenReturn(Optional.of(mockPost));

        // when
        PostResponseDto result = postService.updatePost(postId, requestDto, imgPath, userId);

        // then
        assertNotNull(result);
        assertEquals(mockPost.getPostId(), result.getPostId());
        assertEquals(requestDto.getTitle(), result.getTitle());
        assertEquals(requestDto.getContent(), result.getContent());

        verify(postRepository, times(1)).findByPostId(postId);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("글 이미지 가져오기 테스트")
    void getPostImageTest() {
        // given
        Long postId = 1L;
        String userId = "user123";
        String image = "path/to/image.jpg";
        Post mockPost = Post.builder()
                .postId(postId)
                .userId(userId)
                .image(image)
                .build();

        when(postRepository.findByPostId(postId)).thenReturn(Optional.of(mockPost));

        // when
        String result = postService.getPostImage(userId, postId);

        // then
        assertNotNull(result);
        assertEquals(image, result);

        verify(postRepository, times(1)).findByPostId(postId);
        verifyNoMoreInteractions(postRepository);
    }



}