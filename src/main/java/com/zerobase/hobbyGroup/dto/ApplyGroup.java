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

public class ApplyGroup {
  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateRequest {
    @NotBlank(message = "내용은 필수 입력 값 입니다.")
    @Size(max = 100, message = "내용은 최대 100자 입니다.")
    private String applyContent;

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
    private Long applyId;

    private String applyContent;

    private String status;

    private LocalDateTime createdAt;

    private String nickname;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetSelfListRequest {
    private Integer page;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetSelfListResponse {
    private Long applyId;

    private String applyContent;

    private String status;

    private LocalDateTime createdAt;

    private String groupTitle;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetDetailRequest {
    @NotNull(message = "그룹 신청 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "그룹 신청 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "그룹 신청 아이디는 2147483647 이하여야 합니다.")
    private Long applyId;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetDetailResponse {
    private Long applyId;

    private String applyContent;

    private String status;

    private LocalDateTime createdAt;

    private String groupTitle;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateRequest {
    @NotNull(message = "그룹 신청 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "그룹 신청 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "그룹 신청 아이디는 2147483647 이하여야 합니다.")
    private Long applyId;

    @NotBlank(message = "내용은 필수 입력 값 입니다.")
    @Size(max = 100, message = "내용은 최대 100자 입니다.")
    private String applyContent;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateResponse {
    private String applyContent;

    private LocalDateTime updateAt;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class RejectRequest {
    @NotNull(message = "그룹 신청 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "그룹 신청 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "그룹 신청 아이디는 2147483647 이하여야 합니다.")
    private Long applyId;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AcceptRequest {
    @NotNull(message = "그룹 신청 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "그룹 신청 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "그룹 신청 아이디는 2147483647 이하여야 합니다.")
    private Long applyId;
  }


}
