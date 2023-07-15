package mega.naemeal.admin.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import mega.naemeal.admin.dto.request.NoticeRequestDto;
import mega.naemeal.admin.dto.response.NoticeResponseDto;
import mega.naemeal.admin.entity.Notice;
import mega.naemeal.admin.repository.NoticeRepository;
import mega.naemeal.comment.dto.response.CommentReportResponseDto;
import mega.naemeal.comment.entity.Comment;
import mega.naemeal.comment.entity.CommentReportManage;
import mega.naemeal.comment.repository.CommentManageRepository;
import mega.naemeal.comment.repository.CommentRepository;
import mega.naemeal.cookprogram.entity.CookProgram;
import mega.naemeal.cookprogram.repository.CookProgramRepository;
import mega.naemeal.enums.UserStatus;
import mega.naemeal.profile.entity.Profile;
import mega.naemeal.profile.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AdminServiceImplTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private CookProgramRepository cookProgramRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private CommentManageRepository commentManageRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("공지사항 작성 테스트")
    public void testCreateNotice() {
        // given
        NoticeRequestDto requestDto = NoticeRequestDto.builder()
                .title("Test Title")
                .content("Test Content")
                .build();
        Notice mockNotice = Notice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
        NoticeResponseDto expectedResponseDto = NoticeResponseDto.builder()
                .title(mockNotice.getTitle())
                .content(mockNotice.getContent())
                .build();

        when(noticeRepository.save(any(Notice.class))).thenReturn(mockNotice);

        // when
        NoticeResponseDto responseDto = adminService.createNotice(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(expectedResponseDto.getTitle(), responseDto.getTitle());
        assertEquals(expectedResponseDto.getContent(), responseDto.getContent());

        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

    @Test
    @DisplayName("공지 사항 전체 조회 테스트")
    void getNoticeList_ShouldReturnAllNotices() {
        // given
        List<Notice> mockNotices = new ArrayList<>();
        mockNotices.add(Notice.builder().noticeId(1L).title("Notice 1").content("Content 1").build());
        mockNotices.add(Notice.builder().noticeId(2L).title("Notice 2").content("Content 2").build());

        when(noticeRepository.findAllByOrderByModifiedAtDesc()).thenReturn(mockNotices);

        // when
        List<NoticeResponseDto> result = adminService.getNoticeList();

        // then
        assertEquals(mockNotices.size(), result.size());
        for (int i = 0; i < mockNotices.size(); i++) {
            NoticeResponseDto expectedDto = NoticeResponseDto.builder()
                    .noticeId(mockNotices.get(i).getNoticeId())
                    .title(mockNotices.get(i).getTitle())
                    .content(mockNotices.get(i).getContent())
                    .build();
            NoticeResponseDto actualDto = result.get(i);
            assertEquals(expectedDto.getNoticeId(), actualDto.getNoticeId());
            assertEquals(expectedDto.getTitle(), actualDto.getTitle());
            assertEquals(expectedDto.getContent(), actualDto.getContent());
        }

        verify(noticeRepository, times(1)).findAllByOrderByModifiedAtDesc();
        verifyNoMoreInteractions(noticeRepository);
    }


    @Test
    @DisplayName("공지사항 선택 조회 테스트")
    void findNoticeTest() {
        // given
        Long noticeId = 1L;
        Notice mockNotice = Notice.builder()
                .noticeId(noticeId)
                .title("Notice Title")
                .content("Notice Content")
                .build();

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(mockNotice));

        // when
        NoticeResponseDto result = adminService.findNotice(noticeId);

        // then
        assertNotNull(result);
        assertEquals(mockNotice.getNoticeId(), result.getNoticeId());
        assertEquals(mockNotice.getTitle(), result.getTitle());
        assertEquals(mockNotice.getContent(), result.getContent());

        verify(noticeRepository, times(1)).findById(noticeId);
        verifyNoMoreInteractions(noticeRepository);
    }

    @Test
    @DisplayName("공지사항 선택 조회 예외 테스트")
    void findNotice_Exception() {
        // given
        Long noticeId = 1L;

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.findNotice(noticeId);
        });

        verify(noticeRepository, times(1)).findById(noticeId);
        verifyNoMoreInteractions(noticeRepository);
    }


    @Test
    @DisplayName("공지사항 수정 테스트")
    void updateNoticeTest() {
        // given
        Long noticeId = 1L;
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";
        NoticeRequestDto requestDto = NoticeRequestDto.builder()
                .title(updatedTitle)
                .content(updatedContent)
                .build();

        Notice existingNotice = Notice.builder()
                .noticeId(noticeId)
                .title("Initial Title")
                .content("Initial Content")
                .build();

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(existingNotice));
        when(noticeRepository.save(existingNotice)).thenReturn(existingNotice);

        // when
        NoticeResponseDto result = adminService.updateNotice(noticeId, requestDto);

        // then
        assertNotNull(result);
        assertEquals(existingNotice.getNoticeId(), result.getNoticeId());
        assertEquals(updatedTitle, result.getTitle());
        assertEquals(updatedContent, result.getContent());

        verify(noticeRepository, times(1)).findById(noticeId);
        verifyNoMoreInteractions(noticeRepository);
    }

    @Test
    @DisplayName("공지사항 수정 예외 테스트")
    void updateNoticeException() {
        // given
        Long noticeId = 1L;
        NoticeRequestDto requestDto = NoticeRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());

        // when then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.updateNotice(noticeId, requestDto);
        });

        verify(noticeRepository, times(1)).findById(noticeId);
        verifyNoMoreInteractions(noticeRepository);
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    void deleteNoticeTest() {
        // given
        Long noticeId = 1L;
        Notice mockNotice = new Notice(noticeId, "Notice Title", "Notice Content");

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(mockNotice));

        // when
        adminService.deleteNotice(noticeId);

        // then
        verify(noticeRepository, times(1)).findById(noticeId);
        verify(noticeRepository, times(1)).delete(mockNotice);
        verifyNoMoreInteractions(noticeRepository);
    }

    @Test
    @DisplayName("공지사항 삭제 예외 테스트")
    void deleteNoticeException() {
        // given
        Long noticeId = 1L;

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.deleteNotice(noticeId);
        });

        verify(noticeRepository, times(1)).findById(noticeId);
        verifyNoMoreInteractions(noticeRepository);
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void adminDeletePostTest() {
        // given
        Long postId = 1L;
        CookProgram mockPost = new CookProgram(postId, "Post Title", "Post Content");
        List<Comment> mockComments = Arrays.asList(
                new Comment(1L, "Comment 1", mockPost),
                new Comment(2L, "Comment 2", mockPost)
        );

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(commentRepository.findByPostId(postId)).thenReturn(mockComments);

        // when
        adminService.adminDeletePost(postId);

        // then
        verify(cookProgramRepository, times(1)).findById(postId);
        verify(cookProgramRepository, times(1)).delete(mockPost);
        verify(commentRepository, times(1)).deleteByPostId(postId);
        verifyNoMoreInteractions(cookProgramRepository, commentRepository);
    }

    @Test
    @DisplayName("게시글 삭제 예외 테스트")
    void adminDeletePostException() {
        // given
        Long postId = 1L;

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.adminDeletePost(postId);
        });

        verify(cookProgramRepository, times(1)).findById(postId);
        verifyNoMoreInteractions(cookProgramRepository, commentRepository);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void adminDeleteCommentTest() {
        // given
        Long postId = 1L;
        Long commentId = 1L;
        CookProgram mockPost = new CookProgram(postId, "Post Title", "Post Content");
        Comment mockComment = new Comment(commentId, "Comment Content", mockPost);

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // when
        adminService.adminDeleteComment(postId, commentId);

        // then
        verify(cookProgramRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(mockComment);
        verifyNoMoreInteractions(cookProgramRepository, commentRepository);
    }

    @Test
    @DisplayName("댓글 삭제 예외 테스트 - 게시글이 존재하지 않는 경우")
    void adminDeleteComment_PostNotFound() {
        // given
        Long postId = 1L;
        Long commentId = 1L;

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.adminDeleteComment(postId, commentId);
        });

        verify(cookProgramRepository, times(1)).findById(postId);
        verifyNoMoreInteractions(cookProgramRepository, commentRepository);
    }

    @Test
    @DisplayName("댓글 삭제 예외 테스트 - 삭제할 댓글이 없는 경우")
    void adminDeleteComment_CommentNotFound() {
        // given
        Long postId = 1L;
        Long commentId = 1L;
        CookProgram mockPost = new CookProgram(postId, "Post Title", "Post Content");

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.adminDeleteComment(postId, commentId);
        });

        verify(cookProgramRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verifyNoMoreInteractions(cookProgramRepository, commentRepository);
    }

    @Test
    @DisplayName("유저 활동 정지 테스트")
    void blockUserTest() {
        // given
        String userId = "testUser";
        Profile mockProfile = new Profile(userId, UserStatus.STANDARD);

        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(mockProfile));

        // when
        adminService.blockUser(userId);

        // then
        verify(profileRepository, times(1)).findByUserId(userId);
        verifyNoMoreInteractions(profileRepository);
    }

    @Test
    @DisplayName("유저 활동 정지 예외 테스트 - 프로필이 존재하지 않는 경우")
    void blockUserException_ProfileNotFound() {
        // given
        String userId = "testUser";

        when(profileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.blockUser(userId);
        });

        verify(profileRepository, times(1)).findByUserId(userId);
        verifyNoMoreInteractions(profileRepository);
    }

    @Test
    @DisplayName("유저 활동 재개 테스트")
    void resumeUserTest() {
        // given
        String userId = "testUser";
        Profile mockProfile = new Profile(userId, UserStatus.BLOCKED);

        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(mockProfile));

        // when
        adminService.resumeUser(userId);

        // then
        verify(profileRepository, times(1)).findByUserId(userId);
        verifyNoMoreInteractions(profileRepository);
    }

    @Test
    @DisplayName("유저 활동 재개 예외 테스트 - 프로필이 존재하지 않는 경우")
    void resumeUserException_ProfileNotFound() {
        // given
        String userId = "testUser";

        when(profileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.resumeUser(userId);
        });

        verify(profileRepository, times(1)).findByUserId(userId);
        verifyNoMoreInteractions(profileRepository);
    }

    @Test
    @DisplayName("신고 유저 조회 테스트")
    void getCautionUserListTest() {
        // given
        List<CommentReportManage> mockCommentReportManages = new ArrayList<>();
        mockCommentReportManages.add(CommentReportManage.builder()
                .reportedUserId("user1")
                .reportedReason("Reason 1")
                        .commentId(1L)
                .build());
        mockCommentReportManages.add(CommentReportManage.builder()
                .reportedUserId("user2")
                .reportedReason("Reason 2")
                        .commentId(2L)
                .build());

        when(commentManageRepository.findAllByOrderByModifiedAtDesc()).thenReturn(mockCommentReportManages);

        // when
        List<CommentReportResponseDto> result = adminService.getCautionUserList();

        // then
        assertEquals(mockCommentReportManages.size(), result.size());
        for (int i = 0; i < mockCommentReportManages.size(); i++) {
            CommentReportManage expectedManage = mockCommentReportManages.get(i);
            CommentReportResponseDto actualDto = result.get(i);
            assertEquals(expectedManage.getCommentId(), actualDto.getCommentId());
            assertEquals(expectedManage.getReportedReason(), actualDto.getReportedReason());
        }

        verify(commentManageRepository, times(1)).findAllByOrderByModifiedAtDesc();
        verifyNoMoreInteractions(commentManageRepository);
    }


}
