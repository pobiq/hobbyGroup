package com.zerobase.hobbyGroup.controller;

import com.zerobase.hobbyGroup.dto.ApplyGroup;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.service.ApplyGroupService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/applyGroup")
@RequiredArgsConstructor
public class ApplyGroupController {

  private final ApplyGroupService applyGroupService;

  private final TokenProvider tokenProvider;

  /**
   * 모임 신청
   */
  @PostMapping("/create")
  public ResponseEntity<?> createApplyGroup(@RequestBody @Valid ApplyGroup.CreateRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    this.applyGroupService.create(request, token);
    return ResponseEntity.ok("모임신청을 완료했습니다.");
  }

  /**
   * 특정 모임 신청 목록 조회
   */
  @PostMapping("/list")
  public ResponseEntity<?> getListApplyGroup(@RequestBody @Valid ApplyGroup.GetListRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    List<ApplyGroup.GetListResponse> result = this.applyGroupService.getList(request, token);
    return ResponseEntity.ok(result);
  }

  /**
   * 자신이 한 모임 신청 목록 조회
   */
  @PostMapping("/self/list")
  public ResponseEntity<?> getSelfListApplyGroup(@RequestBody ApplyGroup.GetSelfListRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    List<ApplyGroup.GetSelfListResponse> result = this.applyGroupService.getSelfList(request, token);
    return ResponseEntity.ok(result);
  }

  /**
   * 모임 신청 개별 조회
   */
  @PostMapping("/detail")
  public ResponseEntity<?> getDetailApplyGroup(@RequestBody @Valid ApplyGroup.GetDetailRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    ApplyGroup.GetDetailResponse result = this.applyGroupService.getDetail(request, token);
    return ResponseEntity.ok(result);
  }

  /**
   * 모임 신청 수정
   */
  @PutMapping("/update")
  public ResponseEntity<?> updateApplyGroup(@RequestBody @Valid ApplyGroup.UpdateRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    ApplyGroup.UpdateResponse result = this.applyGroupService.update(request, token);
    return ResponseEntity.ok(result);
  }

  /**
   * 모임 신청 수락
   */
  @PostMapping("/accept")
  public ResponseEntity<?> acceptApplyGroup(@RequestBody @Valid ApplyGroup.AcceptRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    this.applyGroupService.accept(request, token);
    return ResponseEntity.ok("모임 신청 수락이 완료되었습니다.");
  }

  /**
   * 모임 신청 철회
   */
  @PostMapping("/reject")
  public ResponseEntity<?> rejectApplyGroup(@RequestBody @Valid ApplyGroup.RejectRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    this.applyGroupService.reject(request, token);
    return ResponseEntity.ok("모임 신청 철회가 완료 되었습니다.");
  }



}
