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

public class LikeGroup {
  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateRequest {
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
    private Long likeGroupId;

    private LocalDateTime createdAt;

    private String groupTitle;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class DeleteRequest {
    @NotNull(message = "취미 모임 찜 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "취미 모임 찜 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "취미 모임 찜 아이디는 2147483647 이하여야 합니다.")
    private Long likeGroupId;
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
