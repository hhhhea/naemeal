package mega.naemeal.cookprogram.controller;


import lombok.RequiredArgsConstructor;
import mega.naemeal.S3.S3Service;
import mega.naemeal.common.ApiResponse;
import mega.naemeal.cookprogram.dto.AllCookProgramResponseDto;
import mega.naemeal.cookprogram.dto.CookProgramRequestDto;
import mega.naemeal.cookprogram.dto.CookProgramResponseDto;
import mega.naemeal.cookprogram.service.CookProgramServiceImpl;
import mega.naemeal.enums.UserRoleEnum;
import mega.naemeal.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cookProgram")
public class CookProgramController {

    private final CookProgramServiceImpl cookProgramService;
    private final S3Service s3Service;
    private static String dirName = "recipe";
    private static String imgPath = "recipe/recipe-basic.jpg";

    //게시글 작성
    @Secured(UserRoleEnum.Authority.USER)
    @PostMapping
    public ResponseEntity<ApiResponse> createPost(
            @RequestPart("requestDto") CookProgramRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        if(file == null){
            imgPath = imgPath;
        }else {
            imgPath = s3Service.updateImage(file, dirName);
        }
        ApiResponse responseDto = new ApiResponse("요리프로그램글이 작성되었습니다.");
        cookProgramService.createPost(userDetails.getUserId(), requestDto, imgPath);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //게시글 수정
    @Secured(UserRoleEnum.Authority.USER)
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse> updatePost(
            @RequestPart("requestDto") CookProgramRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        if(file == null){
            imgPath = cookProgramService.getPostImage(userDetails.getUserId(), postId);
        }else {
            imgPath = s3Service.updateImage(file, dirName);
        }
        CookProgramResponseDto data = cookProgramService.updatePost(requestDto, postId,
                userDetails.getUserId(), imgPath);
        ApiResponse responseDto = new ApiResponse("요리프로그램글이 수정되었습니다.", data);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    //게시글 삭제
    @Secured({UserRoleEnum.Authority.USER, UserRoleEnum.Authority.ADMIN})
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponse responseDto = new ApiResponse("해당 요리프로그램글이 삭제되었습니다.");
        cookProgramService.deletePost(postId, userDetails.getUserId());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);//responseDto
    }
//-----------------------------------------------------------------------------------------------------------------------

    // 전체 모집글 조회
    @GetMapping
    public ResponseEntity<ApiResponse> getAllPost() {
        List<AllCookProgramResponseDto> data = cookProgramService.getAllPost();

        ApiResponse responseDto = new ApiResponse("전체 요리프로그램 조회가 완료되었습니다.", data);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 선택 모집글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable Long postId) {
        CookProgramResponseDto data = cookProgramService.getPost(postId);
        ApiResponse responseDto = new ApiResponse("선택한 요리프로그램 조회가 완료되었습니다.", data);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
