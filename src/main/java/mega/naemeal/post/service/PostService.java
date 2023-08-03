package mega.naemeal.post.service;

import mega.naemeal.post.dto.request.PostRequestDto;
import mega.naemeal.post.dto.response.AllPostResponseDto;
import mega.naemeal.post.dto.response.PostResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface PostService {

    PostResponseDto createPost(PostRequestDto requestDto, String imgPath, String userId);
    void deletePost(@PathVariable Long postId);
    List<AllPostResponseDto> getAllRecipePosts();
    PostResponseDto getPost(Long postId);
    List<AllPostResponseDto> getAllPosts(String userId);
    PostResponseDto updatePost(@PathVariable Long postId, PostRequestDto requestDto, String imgPath, String userId);
    String getPostImage(String userId, Long postId);


}
