package mega.naemeal.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mega.naemeal.comment.dto.request.CommentRequestDto;
import mega.naemeal.comment.dto.response.CommentReportResponseDto;
import mega.naemeal.comment.dto.response.CommentResponseDto;
import mega.naemeal.comment.entity.Comment;
import mega.naemeal.comment.entity.CommentReportManage;
import mega.naemeal.comment.repository.CommentManageRepository;
import mega.naemeal.comment.repository.CommentRepository;
import mega.naemeal.cookprogram.entity.CookProgram;
import mega.naemeal.cookprogram.repository.CookProgramRepository;
import mega.naemeal.security.UserDetailsImpl;
import mega.naemeal.user.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CommentServiceImplTest {

    @Mock
    private CookProgramRepository cookProgramRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentManageRepository commentManageRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void createCommentTest() {
        // given
        Long postId = 1L;
        String comments = "Test Comment";
        String userId = "testUser";
        String nickname = "Test User";

        CommentRequestDto requestDto = CommentRequestDto.builder()
                .comments(comments)
                .build();
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .member(Member.builder().nickname(nickname).build())
                .build();
        CookProgram mockCookProgram = new CookProgram(postId, "Post Title", "Post Content");
        Comment mockComment = Comment.builder()
                .comments(comments)
                .userId(userId)
                .nickname(nickname)
                .postId(postId)
                .build();

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(mockCookProgram));
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

        // when
        CommentResponseDto result = commentService.createComment(postId, requestDto, userDetails);

        // then
        assertNotNull(result);
        assertEquals(mockComment.getComments(), result.getComments());
        assertEquals(mockComment.getCommentId(), result.getCommentId());
        assertEquals(mockComment.getNickname(), result.getNickname());
        assertEquals(mockComment.getPostId(), result.getPostId());

        verify(cookProgramRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verifyNoMoreInteractions(cookProgramRepository, commentRepository);
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void updateCommentTest() {
        // given
        Long postId = 1L;
        Long commentId = 1L;
        String updatedComment = "Updated Comment";
        String userId = "testUser";
        String nickname = "Test User";

        CommentRequestDto requestDto = CommentRequestDto.builder()
                .comments(updatedComment)
                .build();
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .member(Member.builder().nickname(nickname).userId(userId).build())
                .build();
        CookProgram mockCookProgram = new CookProgram(postId, "Post Title", "Post Content");
        Comment mockComment = Comment.builder()
                .comments("Initial Comment")
                .userId(userId)
                .nickname(nickname)
                .postId(postId)
                .build();

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(mockCookProgram));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // when
        CommentResponseDto result = commentService.updateComment(postId, requestDto, userDetails, commentId);

        // then
        assertNotNull(result);
        assertEquals(updatedComment, result.getComments());

        verify(cookProgramRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verifyNoMoreInteractions(cookProgramRepository, commentRepository);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteCommentTest() {
        // given
        Long postId = 1L;
        Long commentId = 1L;
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .member(Member.builder().nickname("John").userId("user123").build())
                .build();

        CookProgram mockCookProgram = CookProgram.builder()
                .postId(postId)
                .title("Test Post")
                .content("Test Content")
                .build();
        Comment mockComment = Comment.builder()
                .commentId(commentId)
                .comments("Test Comment")
                .userId("user123") // 본인의 댓글로 설정
                .nickname("John Doe")
                .postId(postId)
                .build();

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(mockCookProgram));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // when
        assertDoesNotThrow(() -> commentService.deleteComment(postId, userDetails, commentId));

        // then
        verify(cookProgramRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(mockComment);
        verifyNoMoreInteractions(cookProgramRepository, commentRepository);
    }




    @Test
    @DisplayName("댓글 삭제 - 삭제할 댓글이 없는 경우 예외 발생")
    void deleteComment_WithNonExistingComment_ShouldThrowException() {
        // given
        Long postId = 1L;
        Long commentId = 1L;
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .member(Member.builder().nickname("John").userId("user123").build())
                .build();

        CookProgram mockCookProgram = CookProgram.builder()
                .postId(postId)
                .title("Test Post")
                .content("Test Content")
                .build();

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(mockCookProgram));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when, then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.deleteComment(postId, userDetails, commentId);
        });

        assertEquals("삭제할 댓글이 없습니다.", exception.getMessage());

        verify(cookProgramRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verifyNoMoreInteractions(cookProgramRepository, commentRepository);
    }

    @Test
    @DisplayName("댓글 삭제 - 본인의 댓글이 아닌 경우 예외 발생")
    void deleteComment_WithInvalidUser_ShouldThrowException() {
        // given
        Long postId = 1L;
        Long commentId = 1L;
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .member(Member.builder().nickname("John").userId("user123").build())
                .build();

        CookProgram mockCookProgram = CookProgram.builder()
                .postId(postId)
                .title("Test Post")
                .content("Test Content")
                .build();
        Comment mockComment = Comment.builder()
                .commentId(commentId)
                .comments("Test Comment")
                .userId("user789") // 본인의 댓글이 아닌 경우로 설정
                .nickname("Jane Doe")
                .postId(postId)
                .build();

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(mockCookProgram));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // when, then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.deleteComment(postId, userDetails, commentId);
        });

        assertEquals("본인의 댓글만 삭제 가능합니다.", exception.getMessage());

        verify(cookProgramRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verifyNoMoreInteractions(cookProgramRepository, commentRepository);
    }

    @Test
    @DisplayName("게시글의 댓글 조회 테스트")
    void getCommentListTest() {
        // given
        Long postId = 1L;
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(Comment.builder()
                .commentId(1L)
                .comments("Comment 1")
                .userId("user123")
                .nickname("John")
                .postId(postId)
                .build());
        mockComments.add(Comment.builder()
                .commentId(2L)
                .comments("Comment 2")
                .userId("user456")
                .nickname("Jane")
                .postId(postId)
                .build());

        when(commentRepository.findByPostId(postId)).thenReturn(mockComments);

        // when
        List<CommentResponseDto> result = commentService.getCommentList(postId);

        // then
        assertEquals(mockComments.size(), result.size());
        for (int i = 0; i < mockComments.size(); i++) {
            CommentResponseDto expectedDto = new CommentResponseDto(mockComments.get(i));
            CommentResponseDto actualDto = result.get(i);
            assertEquals(expectedDto.getCommentId(), actualDto.getCommentId());
            assertEquals(expectedDto.getComments(), actualDto.getComments());
            assertEquals(expectedDto.getNickname(), actualDto.getNickname());
            assertEquals(expectedDto.getPostId(), actualDto.getPostId());
        }

        verify(commentRepository, times(1)).findByPostId(postId);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    @DisplayName("댓글 신고 테스트")
    void reportCommentTest() {
        // given
        Long postId = 1L;
        Long commentId = 1L;
        String reportReason = "Offensive language";

        CookProgram mockPost = CookProgram.builder()
                .postId(postId)
                .userId("user123")
                .build();
        Comment mockComment = Comment.builder()
                .commentId(commentId)
                .comments("Comment 1")
                .userId("user456")
                .postId(postId)
                .build();
        CommentReportManage mockReport = CommentReportManage.builder()
                .reportedUserId(mockPost.getUserId())
                .commentId(commentId)
                .reportedReason(reportReason)
                .build();

        when(cookProgramRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
        when(commentManageRepository.save(any(CommentReportManage.class))).thenReturn(mockReport);

        // when
        CommentReportResponseDto result = commentService.reportComment(postId, commentId, reportReason);

        // then
        assertNotNull(result);
        assertEquals(mockReport.getCommentId(), result.getCommentId());
        assertEquals(mockReport.getReportedReason(), result.getReportedReason());

        verify(cookProgramRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentManageRepository, times(1)).save(any(CommentReportManage.class));
        verifyNoMoreInteractions(cookProgramRepository, commentRepository, commentManageRepository);
    }







}