package com.zerobase.hobbyGroup.controller;

import com.zerobase.hobbyGroup.dto.ActivityBoard;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.service.ActivityBoardService;
import com.zerobase.hobbyGroup.util.Util;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/activityBoard")
@RequiredArgsConstructor
public class ActivityBoardController {

  private final ActivityBoardService activityBoardService;

  private final TokenProvider tokenProvider;

  private final Util util;

  /**
   * 활동 게시물 추가(일반글)
   */
  @PostMapping("/create/general")
  public ResponseEntity<?> createGeneralActivityBoard(@Valid ActivityBoard.CreateRequest request, @RequestParam(name = "file") MultipartFile file, @RequestHeader("Authorization") String token) throws Exception {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    ResponseEntity<?> fileResponse = this.util.createValidFile(file);
    if (fileResponse != null) {
      return fileResponse;
    }

    ActivityBoard.CreateResponse result = this.activityBoardService.createGeneral(request, token, file);
    return ResponseEntity.ok(result);
  }

  /**
   * 활동 게시물 추가(공지글)
   */
  @PostMapping("/create/notice")
  public ResponseEntity<?> createNoticeActivityBoard(@Valid ActivityBoard.CreateRequest request, @RequestParam(name = "file") MultipartFile file, @RequestHeader("Authorization") String token) throws Exception {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    ResponseEntity<?> fileResponse = this.util.createValidFile(file);
    if (fileResponse != null) {
      return fileResponse;
    }

    ActivityBoard.CreateResponse result = this.activityBoardService.createNotice(request, token, file);
    return ResponseEntity.ok(result);
  }

  /**
   * 활동 게시물 조회(최신 순)
   */
  @PostMapping("/latest/list")
  public ResponseEntity<?> getLatestList(@RequestBody ActivityBoard.GetListRequest request, @RequestHeader("Authorization") String token) {

    List<ActivityBoard.GetListResponse> result = this.activityBoardService.getLatestList(request, token);

    return ResponseEntity.ok(result);
  }

  /**
   * 활동 게시물 조회(가나다 순)
   */
  @PostMapping("/title/list")
  public ResponseEntity<?> getTitleList(@RequestBody ActivityBoard.GetListRequest request, @RequestHeader("Authorization") String token) {

    List<ActivityBoard.GetListResponse> result = this.activityBoardService.getTitleList(request, token);

    return ResponseEntity.ok(result);
  }

  /**
   * 활동 게시물 조회(조회수 순)
   */
  @PostMapping("/view/list")
  public ResponseEntity<?> getViewList(@RequestBody ActivityBoard.GetListRequest request, @RequestHeader("Authorization") String token) {

    List<ActivityBoard.GetListResponse> result = this.activityBoardService.getViewList(request, token);

    return ResponseEntity.ok(result);
  }

  /**
   * 활동 게시물 개별 조회
   */
  @PostMapping("/detail")
  public ResponseEntity<?> getdetailActivityBoard(@RequestBody @Valid ActivityBoard.GetDetailRequest request, @RequestHeader("Authorization") String token) {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    ActivityBoard.GetDetailReponse result = this.activityBoardService.getDetail(request, token);

    return ResponseEntity.ok(result);
  }

  /**
   * 활동 게시물 수정
   */
  @PutMapping("/update")
  public ResponseEntity<?> updateActivityBoard(@Valid ActivityBoard.UpdateRequest request, @RequestParam(name = "file") MultipartFile file, @RequestHeader("Authorization") String token) throws Exception {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    if(!file.isEmpty()) {
      ResponseEntity<?> fileResponse = this.util.createValidFile(file);
      if (fileResponse != null) {
        return fileResponse;
      }
    }

    ActivityBoard.UpdateResponse result = this.activityBoardService.update(request, file, token);

    return ResponseEntity.ok(result);
  }

  /**
   * 활동 게시물 삭제
   */
  @Transactional
  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteActivityBoard(@RequestBody @Valid ActivityBoard.DeleteRequest request, @RequestHeader("Authorization") String token) throws Exception {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    ActivityBoard.DeleteResponse result =  this.activityBoardService.delete(request, token);

    return ResponseEntity.ok(result.getActivityTitle() + " 게시물이 삭제 완료 되었습니다.");
  }

}
