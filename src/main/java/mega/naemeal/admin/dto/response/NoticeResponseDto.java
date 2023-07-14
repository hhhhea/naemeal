package mega.naemeal.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.admin.entity.Notice;
import mega.naemeal.common.TimeStamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NoticeResponseDto extends TimeStamp {

    private String title;

    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long noticeId;

    public NoticeResponseDto(Notice notice) {
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.createdAt = notice.getCreatedAt();
        this.modifiedAt =notice.getModifiedAt();
        this.noticeId = notice.getNoticeId();
    }
}