package com.zerobase.hobbyGroup.controller;

import com.zerobase.hobbyGroup.dto.GroupBoard;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.service.GroupBoardService;
import com.zerobase.hobbyGroup.util.Util;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
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
@RequestMapping("/groupBoard")
@RequiredArgsConstructor
public class GroupBoardController {

  private final GroupBoardService groupBoardService;

  private final TokenProvider tokenProvider;

  private final Util util;

  /**
   * 그룹 모집 게시물 추가
   * @param request
   * @return
   */
  @PostMapping("/create")
  public ResponseEntity<?> createGroupBoard(@Valid GroupBoard.CreateRequest request, @RequestParam(name = "file") MultipartFile file, @RequestHeader("Authorization") String token) throws Exception {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    ResponseEntity<?> fileResponse = this.util.createValidFile(file);
    if (fileResponse != null) {
      return fileResponse;
    }

    LocalDateTime startAt = request.getStartAt();
    LocalDateTime endAt = request.getEndAt();

    if(startAt.isAfter(endAt)) {
      return ResponseEntity.ok("기간 시작 일자가 끝 일자 보다 이전 시간 이어야 합니다.");
    }

    GroupBoard.CreateResponse result = this.groupBoardService.create(request, token, file);
    return ResponseEntity.ok(result);
  }

  /**
   * 그룹 모집 게시물 조회(최신 순)
   */
  @PostMapping("/latest/list")
  public ResponseEntity<?> getLatestList(@RequestBody GroupBoard.GetListRequest request) {

    List<GroupBoard.GetListResponse> result = this.groupBoardService.getLatestList(request);

    return ResponseEntity.ok(result);
  }

  /**
   * 그룹 모집 게시물 조회(가나다 순)
   */
  @PostMapping("/title/list")
  public ResponseEntity<?> getTitleList(@RequestBody GroupBoard.GetListRequest request) {

    List<GroupBoard.GetListResponse> result = this.groupBoardService.getTitleList(request);

    return ResponseEntity.ok(result);
  }

  /**
   * 그룹 모집 게시물 조회(조회수 순)
   */
  @PostMapping("/view/list")
  public ResponseEntity<?> getViewList(@RequestBody GroupBoard.GetListRequest request) {

    List<GroupBoard.GetListResponse> result = this.groupBoardService.getViewList(request);

    return ResponseEntity.ok(result);
  }

  /**
   * 그룹 모집 게시물 개별 조회
   */
  @PostMapping("/detail")
  public ResponseEntity<?> getdetailGroupBoard(@RequestBody @Valid GroupBoard.GetDetailRequest request, @RequestHeader("Authorization") String token) {


    if(!token.isEmpty()) {
      // validation
      ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
      if (tokenResponse != null) {
        return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
      }
    }

    GroupBoard.GetDetailReponse result = this.groupBoardService.getDetail(request, token);

    return ResponseEntity.ok(result);
  }

  /**
   * 그룹 모집 게시물 수정
   */
  @PutMapping("/update")
  public ResponseEntity<?> updateGroupBoard(@Valid GroupBoard.UpdateRequest request, @RequestParam(name = "file") MultipartFile file, @RequestHeader("Authorization") String token) throws Exception {

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

    GroupBoard.UpdateResponse result = this.groupBoardService.update(request, file, token);

    return ResponseEntity.ok(result);
  }

  /**
   * 그룹 모집 게시물 삭제
   */
  @Transactional
  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteGroupBoard(@RequestBody @Valid GroupBoard.DeleteRequest request, @RequestHeader("Authorization") String token) throws Exception {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    GroupBoard.DeleteResponse result =  this.groupBoardService.delete(request, token);

    return ResponseEntity.ok(result.getGroupTitle() + " 게시물이 삭제 완료 되었습니다.");
  }

}
