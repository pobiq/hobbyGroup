package com.zerobase.hobbyGroup.dto;

import com.zerobase.hobbyGroup.dto.User.UpdateFormResponse;
import com.zerobase.hobbyGroup.entity.CategoryEntity;
import com.zerobase.hobbyGroup.entity.GroupBoardEntity;
import com.zerobase.hobbyGroup.entity.UserEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

public class GroupBoard {
  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateRequest {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String groupTitle;

    @NotNull(message = "인원수는 필수 입력 값입니다.")
    @Min(value = 2, message = "인원수는 2이상 이어야 합니다.")
    @Max(value = 200, message = "인원수는 200 이하여야 합니다.")
    private Long headCount;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String groupContent;

    @NotNull(message = "모집 시작 기간은 필수 입력 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startAt;

    @NotNull(message = "모집 종료 기간은 필수 입력 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endAt;

    @NotNull(message = "카테고리 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "카테고리 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "카테고리 아이디는 2147483647 이하여야 합니다.")
    private Long categoryId;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateResponse {
    private String groupTitle;

    private Long headCount;

    private String groupContent;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime createdAt;

    private String filePath;

    private String fileOriginalName;

    private String categoryName;

    private String userName;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetListRequest {
    private List<Long> catoryIdList;

    private Integer page;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetListResponse {
    private Long groupId;

    private String groupTitle;

    private LocalDateTime createdAt;

    private String filePath;

    private String fileName;

    private String categoryName;

    private String nickname;

    private Long viewCount;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetDetailRequest {
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
  public static class GetDetailReponse {
    private String groupTitle;

    private Long headCount;

    private String groupContent;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime createdAt;

    private String filePath;

    private String fileOriginalName;

    private String categoryName;

    private String nickname;

    private Long viewCount;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateRequest {

    @NotNull(message = "그룹 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "그룹 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "그룹 아이디는 2147483647 이하여야 합니다.")
    private Long groupId;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String groupTitle;

    @NotNull(message = "인원수는 필수 입력 값입니다.")
    @Min(value = 2, message = "인원수는 2이상 이어야 합니다.")
    @Max(value = 200, message = "인원수는 200 이하여야 합니다.")
    private Long headCount;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String groupContent;

    @NotNull(message = "모집 시작 기간은 필수 입력 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startAt;

    @NotNull(message = "모집 종료 기간은 필수 입력 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endAt;

    @NotNull(message = "카테고리 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "카테고리 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "카테고리 아이디는 2147483647 이하여야 합니다.")
    private Long categoryId;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateResponse {
    private String groupTitle;

    private Long headCount;

    private String groupContent;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime updatedAt;

    private Long viewCount;

    private String filePath;

    private String fileOriginalName;

    private String categoryName;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class DeleteRequest {

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
  public static class DeleteResponse {

    private String groupTitle;

  }

}
