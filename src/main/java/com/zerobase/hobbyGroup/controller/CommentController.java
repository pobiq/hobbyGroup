package com.zerobase.hobbyGroup.controller;

import com.zerobase.hobbyGroup.dto.Comment;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.service.CommentService;
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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  private final TokenProvider tokenProvider;

  private final Util util;

  /**
   * 댓글 추가
   */
  @PostMapping("/create")
  public ResponseEntity<?> createGeneralActivityBoard(@RequestBody @Valid Comment.CreateRequest request, @RequestHeader("Authorization") String token) throws Exception {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    Comment.CreateResponse result = this.commentService.create(request, token);
    return ResponseEntity.ok(result);
  }

  /**
   * 댓글 조회(최신 순)
   */
  @PostMapping("/latest/list")
  public ResponseEntity<?> getLatestList(@RequestBody Comment.GetListRequest request, @RequestHeader("Authorization") String token) {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    List<Comment.GetListResponse> result = this.commentService.getLatestList(request, token);

    return ResponseEntity.ok(result);
  }

  /**
   * 댓글 수정
   */
  @PutMapping("/update")
  public ResponseEntity<?> updateActivityBoard(@RequestBody @Valid Comment.UpdateRequest request, @RequestHeader("Authorization") String token) throws Exception {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    Comment.UpdateResponse result = this.commentService.update(request, token);

    return ResponseEntity.ok(result);
  }

  /**
   * 댓글 삭제
   */
  @Transactional
  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteActivityBoard(@RequestBody @Valid Comment.DeleteRequest request, @RequestHeader("Authorization") String token) throws Exception {

    // validation
    ResponseEntity<?> tokenResponse = this.tokenProvider.validToken(token);
    if (tokenResponse != null) {
      return tokenResponse; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    Comment.DeleteResponse result =  this.commentService.delete(request, token);

    return ResponseEntity.ok(result.getContent() + " 댓글이 삭제 완료 되었습니다.");
  }

}
