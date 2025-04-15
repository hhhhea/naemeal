package mega.naemeal.comment.service;

import java.util.List;

import mega.naemeal.comment.dto.request.CommentRequestDto;
import mega.naemeal.comment.dto.response.CommentReportResponseDto;
import mega.naemeal.comment.dto.response.CommentResponseDto;
import mega.naemeal.security.UserDetailsImpl;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {

    // 댓글 생성
    CommentResponseDto createComment(Long postId, CommentRequestDto requestDto,
                                     UserDetailsImpl userDetails);

    // 댓글 수정
    CommentResponseDto updateComment(Long postId, CommentRequestDto requestDto,
                                     UserDetailsImpl userDetails, Long commentId);

    // 댓글 삭제
    void deleteComment(Long postId, UserDetailsImpl userDetails, Long commentId);

    //해당 게시글 댓글 조회
    @Transactional
    List<CommentResponseDto> getCommentList(Long postId);

    // 댓글 신고
    CommentReportResponseDto reportComment(Long postId, Long commentId,
                                            String cautionReason);

}

