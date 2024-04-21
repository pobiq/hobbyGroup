package com.zerobase.hobbyGroup.service;

import static com.zerobase.hobbyGroup.type.ApplyStatus.ACCEPT;

import com.zerobase.hobbyGroup.dto.Comment.CreateRequest;
import com.zerobase.hobbyGroup.dto.Comment.CreateResponse;
import com.zerobase.hobbyGroup.dto.Comment.DeleteRequest;
import com.zerobase.hobbyGroup.dto.Comment.DeleteResponse;
import com.zerobase.hobbyGroup.dto.Comment.GetListRequest;
import com.zerobase.hobbyGroup.dto.Comment.GetListResponse;
import com.zerobase.hobbyGroup.dto.Comment.UpdateRequest;
import com.zerobase.hobbyGroup.dto.Comment.UpdateResponse;
import com.zerobase.hobbyGroup.entity.CommentEntity;
import com.zerobase.hobbyGroup.exception.impl.activity.NoActivityBoardException;
import com.zerobase.hobbyGroup.exception.impl.apply.NoAcceptStatusException;
import com.zerobase.hobbyGroup.exception.impl.auth.NoAuthException;
import com.zerobase.hobbyGroup.exception.impl.comment.NoCommentException;
import com.zerobase.hobbyGroup.exception.impl.group.NoGroupException;
import com.zerobase.hobbyGroup.exception.impl.other.NoYourSelfException;
import com.zerobase.hobbyGroup.exception.impl.user.NoUserException;
import com.zerobase.hobbyGroup.repository.ActivityBoardRepository;
import com.zerobase.hobbyGroup.repository.ActivityBoardViewRepository;
import com.zerobase.hobbyGroup.repository.ApplyGroupRepository;
import com.zerobase.hobbyGroup.repository.CommentRepository;
import com.zerobase.hobbyGroup.repository.FileRepository;
import com.zerobase.hobbyGroup.repository.GroupBoardRepository;
import com.zerobase.hobbyGroup.repository.UserRepository;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.type.Roles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {

  private final GroupBoardRepository groupBoardRepository;

  private final UserRepository userRepository;

  private final ApplyGroupRepository applyGroupRepository;

  private final ActivityBoardRepository activityBoardRepository;

  private final CommentRepository commentRepository;

  private final TokenProvider tokenProvider;


  public CreateResponse create(CreateRequest request, String token) throws Exception {

    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var activity = this.activityBoardRepository.findById(request.getActivityId())
        .orElseThrow(NoActivityBoardException::new);

    var group = this.groupBoardRepository.findById(activity.getGroupBoardEntity().getGroupId())
        .orElseThrow(NoGroupException::new);

    boolean flag = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(group.getGroupId(), user.getUserId(), ACCEPT.getValue());

    // 모임에 가입된 상태가 아닐시
    if(!flag) {
      throw new NoAcceptStatusException();
    }

    CommentEntity commentEntity = CommentEntity.builder()
        .content(request.getContent())
        .createdAt(LocalDateTime.now())
        .activityBoardEntity(activity)
        .userEntity(user)
        .build();

    this.commentRepository.save(commentEntity);

    return CreateResponse.builder()
        .content(commentEntity.getContent())
        .activityTitle(activity.getActivityTitle())
        .createdAt(commentEntity.getCreatedAt())
        .nickname(user.getNickname())
        .build();
  }


  public List<GetListResponse> getLatestList(GetListRequest request, String token) {

    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var activity = this.activityBoardRepository.findById(request.getActivityId())
        .orElseThrow(NoActivityBoardException::new);

    var group = this.groupBoardRepository.findById(activity.getGroupBoardEntity().getGroupId())
        .orElseThrow(NoGroupException::new);

    boolean flag = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(group.getGroupId(), user.getUserId(), ACCEPT.getValue());

    // 모임에 가입된 상태가 아닐시
    if(!flag) {
      throw new NoAcceptStatusException();
    }

    PageRequest pageRequest = PageRequest.of(request.getPage(), 10, Sort.by("createdAt").descending());

    Page<CommentEntity> commentEntities = this.commentRepository.findByActivityBoardEntityActivityId(request.getActivityId(), pageRequest);

    List<GetListResponse> resultList = new ArrayList<>();

    for(CommentEntity entity: commentEntities) {
      GetListResponse getListResponse = GetListResponse.builder()
          .commentId(entity.getCommentId())
          .content(entity.getContent())
          .createdAt(entity.getCreatedAt())
          .activityTitle(entity.getActivityBoardEntity().getActivityTitle())
          .nickname(entity.getUserEntity().getNickname())
          .build();

      resultList.add(getListResponse);
    }

    return resultList;
  }

  public UpdateResponse update(UpdateRequest request, String token) throws Exception {

    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var comment = this.commentRepository.findById(request.getCommentId())
        .orElseThrow();

    var activity = this.activityBoardRepository.findById(comment.getActivityBoardEntity().getActivityId())
        .orElseThrow(NoActivityBoardException::new);

    var group = this.groupBoardRepository.findById(activity.getGroupBoardEntity().getGroupId())
        .orElseThrow(NoGroupException::new);

    // 자기 자신이 쓴 글이 아닐때
    if(!Objects.equals(user.getUserId(), comment.getUserEntity().getUserId())) {
      throw new NoYourSelfException();
    }

    boolean exists = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(group.getGroupId(),
        user.getUserId(), ACCEPT.getValue());

    // 모임에 가입된 상태가 아닐시
    if(!exists) {
      throw new NoAcceptStatusException();
    }


    CommentEntity commentEntity = CommentEntity.builder()
        .commentId(request.getCommentId())
        .content(request.getContent())
        .updatedAt(LocalDateTime.now())
        .activityBoardEntity(activity)
        .userEntity(user)
        .build();

    this.commentRepository.save(commentEntity);

    return UpdateResponse.builder()
        .content(commentEntity.getContent())
        .activityTitle(activity.getActivityTitle())
        .updatedAt(commentEntity.getUpdatedAt())
        .nickname(user.getNickname())
        .build();

  }

  public DeleteResponse delete(DeleteRequest request, String token) {

    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var comment = this.commentRepository.findById(request.getCommentId())
        .orElseThrow(NoCommentException::new);

    var activity = this.activityBoardRepository.findById(comment.getActivityBoardEntity().getActivityId())
        .orElseThrow(NoActivityBoardException::new);

    var group = this.groupBoardRepository.findById(activity.getGroupBoardEntity().getGroupId())
        .orElseThrow(NoGroupException::new);

    List<String> roles = user.getRoles();

    boolean userFlag = false;

    for(String role : roles) {
      if(role.equals(Roles.USER.getValue())) {
        userFlag = true;
      }
    }

    // 일반 유저가 삭제할 때
    if(userFlag) {

      // 자기 자신이 쓴 글만 삭제 가능
      if(!Objects.equals(user.getUserId(), comment.getUserEntity().getUserId())) {
        throw new NoAuthException();
      }

      boolean exists = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(group.getGroupId(),
          user.getUserId(), ACCEPT.getValue());

      // 모임에 가입된 상태가 아닐시
      if(!exists) {
        throw new NoAcceptStatusException();
      }

    }

    this.commentRepository.deleteById(request.getCommentId());

    return DeleteResponse.builder()
        .content(comment.getContent())
        .build();
  }


}
