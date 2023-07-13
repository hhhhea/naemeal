package mega.naemeal.cookprogram.service;

import mega.naemeal.cookprogram.dto.AllCookProgramResponseDto;
import mega.naemeal.cookprogram.dto.CookProgramRequestDto;
import mega.naemeal.cookprogram.dto.CookProgramResponseDto;
import mega.naemeal.cookprogram.entity.CookProgram;
import mega.naemeal.cookprogram.repository.CookProgramRepository;
import mega.naemeal.enums.PostStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CookProgramServiceImplTest {

    @Mock
    private CookProgramRepository cookProgramRepository;

    @InjectMocks
    private CookProgramServiceImpl cookProgramService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    public void testCreatePost() {
        // given
        String userId = "testUser";
        CookProgramRequestDto requestDto = CookProgramRequestDto.builder()
                .title("Test Title")
                .content("Test Content")
                .area("Test Area")
                .locationName("Test Location")
                .deadline(LocalDateTime.now())
                .maxEnrollmentNum(10)
                .build();

        CookProgram savedCookProgram = CookProgram.builder()
                .postId(1L) // 저장된 게시글의 ID
                .userId(userId)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .area(requestDto.getArea())
                .locationName(requestDto.getLocationName())
                .deadline(requestDto.getDeadline())
                .maxEnrollmentNum(requestDto.getMaxEnrollmentNum())
                .build();

        when(cookProgramRepository.save(any(CookProgram.class))).thenReturn(savedCookProgram);

        // when
        CookProgramResponseDto responseDto = cookProgramService.createPost(userId, requestDto, "test-image.jpg");

        // then
        assertNotNull(responseDto);
        assertEquals(savedCookProgram.getTitle(), responseDto.getTitle());
        assertEquals(savedCookProgram.getContent(), responseDto.getContent());
        assertEquals(savedCookProgram.getArea(), responseDto.getArea());
        assertEquals(savedCookProgram.getLocationName(), responseDto.getLocationName());
        assertEquals(savedCookProgram.getDeadline(), responseDto.getDeadline());

        // 적절한 메서드가 호출되었는지 검증
        verify(cookProgramRepository, times(1)).save(any(CookProgram.class));
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void testUpdatePost() {
        // given
        Long postId = 1L;
        String userId = "testUser";

        CookProgramRequestDto requestDto = CookProgramRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .area("Updated Area")
                .locationName("Updated Location")
                .deadline(LocalDateTime.now())
                .maxEnrollmentNum(5)
                .build();

        String imgPath = "test-image.jpg";

        CookProgram existingPost = CookProgram.builder()
                .postId(postId)
                .userId(userId)
                .title("Original Title")
                .content("Original Content")
                .area("Original Area")
                .locationName("Original Location")
                .deadline(LocalDateTime.now().minusDays(1))
                .maxEnrollmentNum(10)
                .build();

        CookProgram updatedPost = CookProgram.builder()
                .postId(postId)
                .userId(userId)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .area(requestDto.getArea())
                .locationName(requestDto.getLocationName())
                .deadline(requestDto.getDeadline())
                .maxEnrollmentNum(requestDto.getMaxEnrollmentNum())
                .build();

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        when(cookProgramRepository.save(any(CookProgram.class))).thenReturn(updatedPost);

        // when
        CookProgramResponseDto responseDto = cookProgramService.updatePost(requestDto, postId, userId, imgPath);

        // then
        assertNotNull(responseDto);
        assertEquals(updatedPost.getTitle(), responseDto.getTitle());
        assertEquals(updatedPost.getContent(), responseDto.getContent());
        assertEquals(updatedPost.getArea(), responseDto.getArea());
        assertEquals(updatedPost.getLocationName(), responseDto.getLocationName());
        assertEquals(updatedPost.getDeadline(), responseDto.getDeadline());

        // 적절한 메서드가 호출되었는지 검증
        verify(cookProgramRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시글 이미지 가져오기 테스트")
    public void testGetPostImage() {
        // given
        Long postId = 1L;
        String userId = "testUser";
        String image = "http://example.com/images/test-image.jpg";

        CookProgram post = CookProgram.builder()
                .postId(postId)
                .userId(userId)
                .image(image)
                .build();

        when(cookProgramRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.of(post));

        // when
        String result = cookProgramService.getPostImage(userId, postId);

        // then
        assertNotNull(result);
        String filename = new File(post.getImage()).getName();
        assertEquals("test-image.jpg", filename);


        // 적절한 메서드가 호출되었는지 검증
        verify(cookProgramRepository, times(1)).findByPostIdAndUserId(postId, userId);
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void testDeletePost() {
        // given
        Long postId = 1L;
        String userId = "testUser";

        CookProgram existingPost = CookProgram.builder()
                .postId(postId)
                .userId(userId)
                .build();

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        // when
        assertDoesNotThrow(() -> cookProgramService.deletePost(postId, userId));

        // then
        verify(cookProgramRepository, times(1)).findById(postId);
        verify(cookProgramRepository, times(1)).delete(existingPost);
    }

    @Test
    @DisplayName("전체 요리프로그램글 조회 테스트")
    public void testGetAllPost() {
        // given
        List<CookProgram> mockCookPrograms = new ArrayList<>();
        mockCookPrograms.add(CookProgram.builder()
                .postId(1L)
                .title("Post 1")
                .content("Content 1")
                .build());
        mockCookPrograms.add(CookProgram.builder()
                .postId(2L)
                .title("Post 2")
                .content("Content 2")
                .build());

        List<AllCookProgramResponseDto> expectedResponseDto = new ArrayList<>();
        expectedResponseDto.add(new AllCookProgramResponseDto(mockCookPrograms.get(0)));
        expectedResponseDto.add(new AllCookProgramResponseDto(mockCookPrograms.get(1)));

        when(cookProgramRepository.findAllByOrderByCreatedAtDesc()).thenReturn(mockCookPrograms);

        // when
        List<AllCookProgramResponseDto> actualResponseDto = cookProgramService.getAllPost();

        // then
        assertEquals(expectedResponseDto.size(), actualResponseDto.size());
        for (int i = 0; i < expectedResponseDto.size(); i++) {
            AllCookProgramResponseDto expectedDto = expectedResponseDto.get(i);
            AllCookProgramResponseDto actualDto = actualResponseDto.get(i);

            assertEquals(expectedDto.getPostId(), actualDto.getPostId());
            assertEquals(expectedDto.getTitle(), actualDto.getTitle());
            assertEquals(expectedDto.getContent(), actualDto.getContent());
        }

        // 적절한 메서드가 호출되었는지 검증
        verify(cookProgramRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("선택 요리프로그램글 조회 - 존재하는 경우")
    public void testGetPost_ExistingPost() {
        // given
        Long postId = 1L;
        CookProgram mockPost = CookProgram.builder()
                .postId(postId)
                .title("Test Title")
                .content("Test Content")
                .build();

        when(cookProgramRepository.findByPostId(postId)).thenReturn(Optional.of(mockPost));

        // when
        CookProgramResponseDto responseDto = cookProgramService.getPost(postId);

        // then
        assertEquals(mockPost.getPostId(), responseDto.getPostId());
        assertEquals(mockPost.getTitle(), responseDto.getTitle());
        assertEquals(mockPost.getContent(), responseDto.getContent());

        // 적절한 메서드가 호출되었는지 검증
        verify(cookProgramRepository, times(1)).findByPostId(postId);
    }

    @Test
    @DisplayName("선택 요리프로그램글 조회 - 존재하지 않는 경우")
    public void testGetPost_NonExistingPost() {
        // given
        Long postId = 1L;

        when(cookProgramRepository.findByPostId(postId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> cookProgramService.getPost(postId));

        // 적절한 메서드가 호출되었는지 검증
        verify(cookProgramRepository, times(1)).findByPostId(postId);
    }



}
