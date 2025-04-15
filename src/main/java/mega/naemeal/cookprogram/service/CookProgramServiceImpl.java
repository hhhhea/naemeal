package mega.naemeal.cookprogram.service;

import lombok.RequiredArgsConstructor;
import mega.naemeal.cookprogram.dto.AllCookProgramResponseDto;
import mega.naemeal.cookprogram.dto.CookProgramRequestDto;
import mega.naemeal.cookprogram.dto.CookProgramResponseDto;
import mega.naemeal.cookprogram.entity.CookProgram;
import mega.naemeal.cookprogram.repository.CookProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CookProgramServiceImpl implements CookProgramService{

    private final CookProgramRepository cookProgramRepository;

    //게시글 작성
    @Override
    @Transactional
    public CookProgramResponseDto createPost(String userId,
        CookProgramRequestDto requestDto, String imgPath) {

        CookProgram post = new CookProgram(userId, requestDto.getTitle(),
            requestDto.getContent(),
            requestDto.getArea(), requestDto.getLocationName(), requestDto.getDeadline(),
            requestDto.getMaxEnrollmentNum(), imgPath);//닉네임, 지역,
        cookProgramRepository.save(post);

        return new CookProgramResponseDto(post);
    }

    //게시글 수정
    @Override
    @Transactional
    public CookProgramResponseDto updatePost(CookProgramRequestDto requestDto,
        Long postId, String userId, String imgPath) {
        System.out.println("updatePost!!!!!!!!!!!!!");
        System.out.println(requestDto.getTitle());
        System.out.println(postId+"###########");
        CookProgram post = cookProgramRepository.findByPostId(postId).orElseThrow(
                () -> new IllegalArgumentException("모집글이 존재하지 않습니다.")
        );
        System.out.println(post.getPostId());
        System.out.println(post.getTitle());

        if (!post.getUserId().equals(userId)) {
            System.out.println("!post.getUserId().equals(userId)");
            throw new IllegalArgumentException("요리프로그램글의 작성자가 일치하지 않습니다.");
        } else {
            System.out.println("post.getUserId().equals(userId)");
            post.update(requestDto, imgPath);
        }
        return new CookProgramResponseDto(post);
    }

    @Override
    public String getPostImage(String userId, Long postId) {
        CookProgram post = cookProgramRepository.findByPostIdAndUserId(postId, userId)
            .orElseThrow(
                () -> new IllegalArgumentException("해당 요리프로그램글이 존재하지 않거나, 해당기업의 요리프로그램글이 아닙니다. "));
        return post.getImage().substring(38);
    }


    //게시글 삭제
    @Override
    public void deletePost(Long postId, String userId) {
        CookProgram post = cookProgramRepository.findById(postId).orElseThrow(
            () -> new IllegalArgumentException("해당 요리프로그램글이 존재하지 않습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("요리프로그램글의 작성자가 일치하지 않습니다.");
        } else {
            cookProgramRepository.delete(post);
        }
    }


    // 전체 요리프로그램글 조회
    @Override
    @Transactional(readOnly = true)
    public List<AllCookProgramResponseDto> getAllPost() {
        List<CookProgram> allCookProgram = cookProgramRepository.findAllByOrderByCreatedAtDesc();
        List<AllCookProgramResponseDto> responseDto = new ArrayList<>();
        for (CookProgram cookProgram : allCookProgram) {
            responseDto.add(new AllCookProgramResponseDto(cookProgram));
        }
        return responseDto;
    }

    // 선택 요리프로그램글 조회
    @Override
    @Transactional(readOnly = true)
    public CookProgramResponseDto getPost(Long postId) {
        CookProgram post = cookProgramRepository.findByPostId(postId).orElseThrow(
                () -> new IllegalArgumentException("모집글이 존재하지 않습니다.")
        );
        CookProgramResponseDto responseDto = new CookProgramResponseDto(post);

        return responseDto;
    }


}
