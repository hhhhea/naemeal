package mega.naemeal.post.service;

import mega.naemeal.post.dto.request.PostRequestDto;
import mega.naemeal.post.dto.response.AllPostResponseDto;
import mega.naemeal.post.dto.response.PostResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface PostService {

    PostResponseDto createPost(PostRequestDto requestDto, String imgPath, String userId);
    void deletePost(@PathVariable Long challengeAuthId, String userId);
    List<AllPostResponseDto> getAllRecipePosts();
    PostResponseDto getPost(Long challengeAuthId);
    List<AllPostResponseDto> getAllPosts(String userId);
    PostResponseDto updatePost(@PathVariable Long challengeAuthId, PostRequestDto requestDto, String imgPath, String userId);
    String getPostImage(String userId, Long challengeAuthId);


}
