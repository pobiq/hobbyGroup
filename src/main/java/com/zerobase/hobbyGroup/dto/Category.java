package com.zerobase.hobbyGroup.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class Category {
  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateRequest {
    @NotBlank(message = "카테고리 이름은 필수 입력 값 입니다.")
    @Size(max = 100, message = "카테고리 이름은 최대 100자 입니다.")
    private String categoryName;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateReponse {
    private String categoryName;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateRequest {

    @NotNull(message = "카테고리 아이디는 필수 입력 값 입니다.")
    @Min(value = 1, message = "카테고리 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "카테고리 아이디는 2147483647 이하여야 합니다.")
    private Long categoryId;

    @NotBlank(message = "카테고리 이름은 필수 입력 값 입니다.")
    @Size(max = 100, message = "카테고리 이름은 최대 100자 입니다.")
    private String categoryName;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateReponse {

    private String categoryName;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class DeleteRequest {

    @NotNull(message = "카테고리 아이디는 필수 입력 값 입니다.")
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
  public static class DeleteReponse {

    private String categoryName;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GetListReponse {
    private String categoryName;
  }

}
