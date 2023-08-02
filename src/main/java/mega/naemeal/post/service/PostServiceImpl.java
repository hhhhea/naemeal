package mega.naemeal.post.service;

import lombok.RequiredArgsConstructor;
import mega.naemeal.comment.entity.Comment;
import mega.naemeal.comment.repository.CommentRepository;
import mega.naemeal.enums.Category;
import mega.naemeal.post.dto.request.PostRequestDto;
import mega.naemeal.post.dto.response.AllPostResponseDto;
import mega.naemeal.post.dto.response.PostResponseDto;
import mega.naemeal.post.entity.Post;
import mega.naemeal.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 글 등록
    @Override
    public PostResponseDto createPost(PostRequestDto requestDto, String imgPath, String userId) {
        Post recipePost = new Post(Category.RECIPE, userId, requestDto.getTitle(),
            requestDto.getContent(), imgPath);
        Post savedPost = postRepository.save(recipePost);
        return new PostResponseDto(savedPost);
    }

    //삭제
    @Override
    public void deletePost(Long postId, String userId) {
        Post post = postRepository.findByPostIdAndUserId(
            postId, userId).orElseThrow(
            () -> new IllegalArgumentException("삭제할 글이 존재하지 않습니다."));
        postRepository.delete(post);
    }

    //전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<AllPostResponseDto> getAllRecipePosts() {
        List<Post> recipePosts = postRepository.findAll();
        List<AllPostResponseDto> responseDto = new ArrayList<>();
        for (Post post : recipePosts) {
            responseDto.add(new AllPostResponseDto(post));
        }
        return responseDto;
    }

    //선택 조회
    @Override
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(
                () -> new IllegalArgumentException("탐색한 글이 존재하지 않습니다..")
            );
        List<Comment> comments = commentRepository.findByPostId(post.getPostId());
        return new PostResponseDto(post, comments);
    }


    @Override
    public List<AllPostResponseDto> getAllPosts(String userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);
        List<AllPostResponseDto> responseDto = new ArrayList<>();
        for (Post post : posts) {
            responseDto.add(new AllPostResponseDto(post));
        }
        return responseDto;
    }

    //글 수정
    @Override
    public PostResponseDto updatePost(Long postId,
        PostRequestDto requestDto, String imgPath, String userId) {
        Post post = postRepository.findByPostIdAndUserId(
            postId, userId).orElseThrow(
            () -> new IllegalArgumentException("해당 글이 없거나, 본인의 글이 아닙니다.")
        );
        post.update(requestDto.getTitle(), requestDto.getContent(), imgPath);
        return new PostResponseDto(post);
    }

    @Override
    public String getPostImage(String userId, Long postId) {
        Post post = postRepository.findByPostIdAndUserId(
            postId, userId).orElseThrow(
            () -> new IllegalArgumentException("해당 글이 없거나, 본인의 글이 아닙니다.")
        );
        return post.getImage();
    }
}
