package mega.naemeal.comment.service;

import lombok.RequiredArgsConstructor;
import mega.naemeal.comment.dto.request.CommentRequestDto;
import mega.naemeal.comment.dto.response.CommentResponseDto;
import mega.naemeal.comment.entity.Comment;
import mega.naemeal.comment.repository.CommentRepository;
import mega.naemeal.cookprogram.entity.CookProgram;
import mega.naemeal.cookprogram.repository.CookProgramRepository;
import mega.naemeal.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final CookProgramRepository cookProgramRepository;

  // 댓글 작성
  @Transactional
  public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto,
                                          UserDetailsImpl userDetails) {

    CookProgram cookProgram = cookProgramRepository.findById(postId).orElseThrow(
        () -> new IllegalArgumentException("해당 게시글이 없습니다.")
    );
    Comment comment = new Comment(requestDto.getComments(), userDetails.getUserId(),
        userDetails.getUser().getNickname(), postId);
    commentRepository.save(comment);
    return new CommentResponseDto(comment);
  }

  // 댓글 수정
  @Transactional
  public CommentResponseDto updateComment(Long postId, CommentRequestDto requestDto,
      UserDetailsImpl userDetails, Long commentId) {

    cookProgramRepository.findById(postId).orElseThrow(
        () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
    );

    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new IllegalArgumentException("수정할 댓글이 없습니다.")
    );

    if (!userDetails.getUser().isValidId(comment.getUserId())) {
      throw new IllegalArgumentException("본인의 댓글만 수정 가능합니다.");
    }

    comment.updateComment(requestDto);
    return new CommentResponseDto(comment);
  }

  // 댓글 삭제
  @Transactional
  public void deleteComment(Long postId, UserDetailsImpl userDetails, Long commentId) {

    cookProgramRepository.findById(postId).orElseThrow(
        () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
    );

    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new IllegalArgumentException("삭제할 댓글이 없습니다.")
    );
    if (!userDetails.getUser().isValidId(comment.getUserId())) {
      throw new IllegalArgumentException("본인의 댓글만 삭제 가능합니다.");
    }

    commentRepository.delete(comment);
  }

  // 게시글의 댓글 조회
  @Override
  public List<CommentResponseDto> getCommentList(Long postId) {
    List<Comment> comments = commentRepository.findByPostId(postId);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    for (Comment comment : comments) {
      commentResponseDtoList.add(new CommentResponseDto(comment));
    }

    return commentResponseDtoList;
  }


}
