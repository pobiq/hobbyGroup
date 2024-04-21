package com.zerobase.hobbyGroup.controller;

import com.zerobase.hobbyGroup.dto.Category;
import com.zerobase.hobbyGroup.dto.Category.GetListReponse;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  private final TokenProvider tokenProvider;

  /**
   * 카테고리 추가
   * @param request 카테고리 이름
   * @return 카테고리 이름
   */
  @PostMapping("/create")
  public ResponseEntity<?> createCategory(@RequestBody @Valid Category.CreateRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    Category.CreateReponse result = this.categoryService.create(request);
    return ResponseEntity.ok("카테고리 : " + result.getCategoryName() + " 추가 완료했습니다.");
  }

  /**
   * 이름순 카테고리 목록 조회
   * @return 이름순 카테고리 목록
   */
  @GetMapping("/list")
  public ResponseEntity<?> getCategoryListByCategoryName() {
    List<GetListReponse> list = this.categoryService.getCategoryListByCategoryName();
    return ResponseEntity.ok(list);
  }

  /**
   * 카테고리 수정
   * @param request 카테고리 아이디, 이름
   * @return 출력문
   */
  @PutMapping("/update")
  public ResponseEntity<?> updateCategory(@RequestBody @Valid Category.UpdateRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    Category.UpdateReponse result = this.categoryService.update(request);
    return ResponseEntity.ok("카테고리 " + request.getCategoryName() + "가 " + result.getCategoryName() +"로 바뀌었습니다.");
  }

  /**
   * 카테고리 삭제
   * @param request 카테고리 이름
   * @return 출력문
   */
  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteCategory(@RequestBody @Valid Category.DeleteRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    Category.DeleteReponse result = this.categoryService.delete(request);
    return ResponseEntity.ok("카테고리 " + result.getCategoryName() + "가 삭제되었습니다.");
  }




}
