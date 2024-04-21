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

public class ActivityBoard {
  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateRequest {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 30, message ="제목은 30자 이하 이어야 합니다.")
    private String activityTitle;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max = 100, message ="내용은 100자 이하 이어야 합니다.")
    private String activityContent;

    @NotNull(message = "그룹 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "그룹 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "그룹 아이디는 2147483647 이하여야 합니다.")
    private Long groupId;

  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateResponse {
    private String activityTitle;

    private String activityContent;

    private String type;

    private LocalDateTime createdAt;

    private Long viewCount;

    private String filePath;

    private String fileOriginalName;

    private String nickname;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetListRequest {
    @NotNull(message = "그룹 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "그룹 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "그룹 아이디는 2147483647 이하여야 합니다.")
    private Long groupId;

    private Integer page;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetListResponse {
    private Long activityId;

    private String activityTitle;

    private String activityContent;

    private LocalDateTime createdAt;

    private String type;

    private Long viewCount;

    private String filePath;

    private String fileOriginalName;

    private String nickname;

    private Long groupId;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetDetailRequest {
    @NotNull(message = "활동 게시물 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "활동 게시물 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "활동 게시물 아이디는 2147483647 이하여야 합니다.")
    private Long activityId;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetDetailReponse {
    private Long activityId;

    private String activityTitle;

    private String activityContent;

    private LocalDateTime createdAt;

    private String type;

    private Long viewCount;

    private String filePath;

    private String fileOriginalName;

    private String nickname;

    private Long groupId;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateRequest {

    @NotNull(message = "활동 게시물 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "활동 게시물 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "활동 게시물 아이디는 2147483647 이하여야 합니다.")
    private Long activityId;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max=20, message = "제목은 20자 이하여야 합니다.")
    private String activityTitle;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max=100, message = "내용은 100자 이하여야 합니다.")
    private String activityContent;

    @NotBlank(message = "글의 타입은 필수 입력 값입니다.")
    private String type;

  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateResponse {
    private String activityTitle;

    private String activityContent;

    private String type;

    private LocalDateTime updatedAt;

    private Long viewCount;

    private String filePath;

    private String fileOriginalName;

    private Long groupId;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class DeleteRequest {

    @NotNull(message = "활동 게시물 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "활동 게시물 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "활동 게시물 아이디는 2147483647 이하여야 합니다.")
    private Long activityId;

  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class DeleteResponse {

    private String activityTitle;

  }

}
