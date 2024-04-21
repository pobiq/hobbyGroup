package com.zerobase.hobbyGroup.controller;

import com.zerobase.hobbyGroup.dto.ApplyGroup;
import com.zerobase.hobbyGroup.dto.LikeGroup;
import com.zerobase.hobbyGroup.dto.LikeGroup.GetSelfListResponse;
import com.zerobase.hobbyGroup.entity.LikeGroupEntity;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.service.LikeGroupService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/likeGroup")
@RequiredArgsConstructor
public class LikeGroupController {

  private final LikeGroupService likeGroupService;

  private final TokenProvider tokenProvider;

  /**
   * 모임 찜 추가
   */
  @PostMapping("/create")
  public ResponseEntity<?> createLikeGroup(@RequestBody @Valid LikeGroup.CreateRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    this.likeGroupService.create(request, token);
    return ResponseEntity.ok("모임 찜 추가 완료했습니다.");
  }

  /**
   * 모임 찜 개별 목록 조회
   */
  @PostMapping("/self/list")
  public ResponseEntity<?> getSelfListLikeGroup(@RequestBody @Valid LikeGroup.GetSelfListRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    List<LikeGroup.GetSelfListResponse> result = this.likeGroupService.getSelfList(request, token);
    return ResponseEntity.ok(result);
  }

  /**
   * 모임 찜 삭제
   */
  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteLikeGroup(@RequestBody @Valid LikeGroup.DeleteRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    this.likeGroupService.delete(request, token);
    return ResponseEntity.ok("모임 찜 삭제 완료되었습니다.");
  }

}
