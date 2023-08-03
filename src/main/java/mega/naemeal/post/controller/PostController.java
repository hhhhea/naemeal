package mega.naemeal.post.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;;
import mega.naemeal.S3.S3Service;
import mega.naemeal.common.ApiResponse;
import mega.naemeal.post.dto.request.PostRequestDto;
import mega.naemeal.post.dto.response.AllPostResponseDto;
import mega.naemeal.post.dto.response.PostResponseDto;
import mega.naemeal.post.service.PostServiceImpl;
import mega.naemeal.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/posts", produces = "application/json;charset=UTF-8")
public class PostController {

    private static String dirName = "recipe";
    private static String imgPath = "recipe/recipe-basic.jpg";

    private final PostServiceImpl postService;
    private final S3Service s3Service;

    //레시피 등록하기
    @PostMapping
    public ResponseEntity<ApiResponse> createRecipe(
        @RequestPart("requestDto") PostRequestDto requestDto,
        @RequestPart(value = "file", required = false) MultipartFile file,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
        throws IOException {
        if (file == null) {
            imgPath = imgPath;
        } else {
            imgPath = s3Service.updateImage(file, dirName);
        }
        PostResponseDto data = postService.createPost(requestDto, imgPath, userDetails.getUserId());
        ApiResponse responseDto = new ApiResponse("레시피가 등록되었습니다.", data);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //레시피 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse> getPosts() {
        List<AllPostResponseDto> data = postService.getAllRecipePosts();
        ApiResponse responseDto = new ApiResponse("전체 레시피를 조회했습니다.", data);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //작성 레시피 선택 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable Long postId) {
        PostResponseDto data = postService.getPost(postId);
        ApiResponse responseDto = new ApiResponse("레시피 선택 조회가 완료되었습니다.", data);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    //레시피 글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println(postId);
        System.out.println( userDetails.getUserId());
        postService.deletePost(postId);
        ApiResponse responseDto = new ApiResponse("레시피 삭제가 완료되었습니다.");
        System.out.println("delete~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //레시피 글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse> updatePost(
        @PathVariable Long postId,
        @RequestPart("requestDto") PostRequestDto requestDto,
        @RequestPart(value = "file", required = false) MultipartFile file,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        if (file == null) {
            imgPath = postService.getPostImage(userDetails.getUserId(), postId);
        } else {
            imgPath = s3Service.updateImage(file, dirName);
        }
        PostResponseDto data = postService.updatePost(postId,
            requestDto, imgPath, userDetails.getUserId());
        ApiResponse responseDto = new ApiResponse("레시피 수정이 완료되었습니다.", data);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    //나의 게시물 전체조회
    @GetMapping("/my")
    public ResponseEntity<ApiResponse> getPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AllPostResponseDto> data = postService.getAllPosts(userDetails.getUserId());
        ApiResponse responseDto = new ApiResponse("나의 레시피를 전체 조회했습니다.", data);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
