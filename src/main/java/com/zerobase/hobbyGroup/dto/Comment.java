package com.zerobase.hobbyGroup.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class Comment {
  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateRequest {
    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max = 100, message ="내용은 100자 이하 이어야 합니다.")
    private String content;

    @NotNull(message = "활동 게시판 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "활동 게시판 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "활동 게시판 아이디는 2147483647 이하여야 합니다.")
    private Long activityId;

  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateResponse {
    private String content;

    private LocalDateTime createdAt;

    private String activityTitle;

    private String nickname;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetListRequest {
    @NotNull(message = "활동 게시판 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "활동 게시판 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "활동 게시판 아이디는 2147483647 이하여야 합니다.")
    private Long activityId;

    private Integer page;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetListResponse {
    private Long commentId;

    private String content;

    private LocalDateTime createdAt;

    private String activityTitle;

    private String nickname;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateRequest {
    @NotNull(message = "댓글 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "댓글 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "댓글 아이디는 2147483647 이하여야 합니다.")
    private Long commentId;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max = 100, message ="내용은 100자 이하 이어야 합니다.")
    private String content;

  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateResponse {
    private String content;

    private LocalDateTime updatedAt;

    private String activityTitle;

    private String nickname;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class DeleteRequest {

    @NotNull(message = "댓글 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "댓글 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "댓글 아이디는 2147483647 이하여야 합니다.")
    private Long commentId;

  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class DeleteResponse {

    private String content;

  }

}
