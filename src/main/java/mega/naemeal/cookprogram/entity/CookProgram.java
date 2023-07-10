package mega.naemeal.cookprogram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.common.TimeStamp;
import mega.naemeal.cookprogram.dto.CookProgramRequestDto;
import mega.naemeal.enums.PostStatus;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class CookProgram extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PostStatus postStatus = PostStatus.TRUE;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String area;

    @Column
    private String nickname;

    @Column
    private String locationName;

    @Column
    private LocalDateTime deadline;

    @Column(nullable = false)
    private String image;

    //모집 날짜가 지나면 모집완료로 변경
    @PreUpdate
    public void beforeUpdate() {
        if (LocalDateTime.now().isAfter(getDeadline())) {
            postStatus = PostStatus.FALSE;
        }
        else {
            postStatus = PostStatus.TRUE;
        }
    }

    @Column
    private int maxEnrollmentNum;

    //모집 날짜 지난 경우 선택 불가
    @PrePersist
    public void checkEndTime() {
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("모집기간이 이미 지났습니다.");
        }
    }


    public CookProgram(String userId, String title, String content,
                       String area, String locationName, LocalDateTime deadline, int maxEnrollmentNum, String image) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.area = area;

        this.locationName = locationName;
        this.deadline = deadline;
        this.maxEnrollmentNum = maxEnrollmentNum;
        this.image = image;
    }

    public void update(CookProgramRequestDto requestDto, String imgPath) {//지역, 상태,
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.area = requestDto.getArea();
        this.deadline = requestDto.getDeadline();
        this.image = imgPath;
    }

}
