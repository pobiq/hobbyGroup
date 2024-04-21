package com.zerobase.hobbyGroup.service;

import static com.zerobase.hobbyGroup.type.ApplyStatus.ACCEPT;
import static com.zerobase.hobbyGroup.type.ApplyStatus.APPLY;
import static com.zerobase.hobbyGroup.type.ApplyStatus.REJECT;

import com.zerobase.hobbyGroup.dto.ApplyGroup.AcceptRequest;
import com.zerobase.hobbyGroup.dto.ApplyGroup.CreateRequest;
import com.zerobase.hobbyGroup.dto.ApplyGroup.GetDetailRequest;
import com.zerobase.hobbyGroup.dto.ApplyGroup.GetDetailResponse;
import com.zerobase.hobbyGroup.dto.ApplyGroup.GetListRequest;
import com.zerobase.hobbyGroup.dto.ApplyGroup.GetListResponse;
import com.zerobase.hobbyGroup.dto.ApplyGroup.GetSelfListRequest;
import com.zerobase.hobbyGroup.dto.ApplyGroup.GetSelfListResponse;
import com.zerobase.hobbyGroup.dto.ApplyGroup.RejectRequest;
import com.zerobase.hobbyGroup.dto.ApplyGroup.UpdateRequest;
import com.zerobase.hobbyGroup.dto.ApplyGroup.UpdateResponse;
import com.zerobase.hobbyGroup.entity.ApplyGroupEntity;
import com.zerobase.hobbyGroup.exception.impl.apply.AlreadyApplyGroupException;
import com.zerobase.hobbyGroup.exception.impl.apply.AlreadyRejectStatusException;
import com.zerobase.hobbyGroup.exception.impl.apply.FullGroupException;
import com.zerobase.hobbyGroup.exception.impl.apply.NoApplyGroupException;
import com.zerobase.hobbyGroup.exception.impl.apply.NoApplyStatusException;
import com.zerobase.hobbyGroup.exception.impl.apply.NoGroupLeaderException;
import com.zerobase.hobbyGroup.exception.impl.apply.NoPeriodException;
import com.zerobase.hobbyGroup.exception.impl.apply.NoRejectException;
import com.zerobase.hobbyGroup.exception.impl.group.NoGroupException;
import com.zerobase.hobbyGroup.exception.impl.other.NoYourSelfException;
import com.zerobase.hobbyGroup.exception.impl.user.NoUserException;
import com.zerobase.hobbyGroup.repository.ApplyGroupRepository;
import com.zerobase.hobbyGroup.repository.GroupBoardRepository;
import com.zerobase.hobbyGroup.repository.UserRepository;
import com.zerobase.hobbyGroup.security.TokenProvider;
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
public class ApplyGroupService {

  private final UserRepository userRepository;

  private final ApplyGroupRepository applyGroupRepository;

  private final GroupBoardRepository groupBoardRepository;

  private final TokenProvider tokenProvider;

  /**
   * 모임 신청
   */
  public void create(CreateRequest request, String token) {
    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var group = this.groupBoardRepository.findById(request.getGroupId())
        .orElseThrow(NoGroupException::new);

    if(this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatusIn(group.getGroupId(), user.getUserId(), List.of(APPLY.getValue(), ACCEPT.getValue()))) {
      throw new AlreadyApplyGroupException();
    }

    LocalDateTime now = LocalDateTime.now();

    if(group.getStartAt().isAfter(now) || group.getEndAt().isBefore(now)) {
      throw new NoPeriodException();
    }

    Long count = this.applyGroupRepository.countByGroupBoardEntityGroupIdAndStatus(group.getGroupId(), ACCEPT.getValue());

    if(count >= group.getHeadCount()) {
      throw new FullGroupException();
    }

    ApplyGroupEntity applyGroupEntity = ApplyGroupEntity.builder()
        .applyContent(request.getApplyContent())
        .createdAt(LocalDateTime.now())
        .status(APPLY.getValue())
        .groupBoardEntity(group)
        .userEntity(user)
        .build();

    this.applyGroupRepository.save(applyGroupEntity);
  }

  /**
   * 모임 신청 리스트 조회
   */
  public List<GetListResponse> getList(GetListRequest request, String token) {
    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var group = this.groupBoardRepository.findById(request.getGroupId())
        .orElseThrow(NoGroupException::new);


    // 모임장만 조회 가능
    if(!group.getUserEntity().getUserId().equals(user.getUserId())) {
      throw new NoGroupLeaderException();
    }

    PageRequest pageRequest = PageRequest.of(request.getPage(), 20, Sort.by("createdAt").descending());

    Page<ApplyGroupEntity> applyGroupEntityPage = this.applyGroupRepository.findByGroupBoardEntityGroupId(pageRequest, request.getGroupId());

    List<GetListResponse> resultList = new ArrayList<>();
    for(ApplyGroupEntity entity : applyGroupEntityPage) {
      GetListResponse getListResponse = GetListResponse.builder()
          .applyId(entity.getApplyId())
          .applyContent(entity.getApplyContent())
          .status(entity.getStatus())
          .createdAt(entity.getCreatedAt())
          .nickname(entity.getUserEntity().getNickname())
          .build();
      resultList.add(getListResponse);
    }

    return resultList;
  }

  /**
   * 자신이 한 모임 신청 목록 조회
   */
  public List<GetSelfListResponse> getSelfList(GetSelfListRequest request, String token) {
    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    PageRequest pageRequest = PageRequest.of(request.getPage(), 20, Sort.by("createdAt").descending());

    Page<ApplyGroupEntity> applyGroupEntityPage = this.applyGroupRepository.findByUserEntityUserId(pageRequest, user.getUserId());

    List<GetSelfListResponse> resultList = new ArrayList<>();
    for(ApplyGroupEntity entity : applyGroupEntityPage) {
      GetSelfListResponse getSelfListResponse = GetSelfListResponse.builder()
          .applyId(entity.getApplyId())
          .applyContent(entity.getApplyContent())
          .status(entity.getStatus())
          .createdAt(entity.getCreatedAt())
          .groupTitle(entity.getGroupBoardEntity().getGroupTitle())
          .build();
      resultList.add(getSelfListResponse);
    }

    return resultList;
  }

  /**
   * 모임 신청 개별 조회
   */
  public GetDetailResponse getDetail(GetDetailRequest request, String token) {
    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var applyGroup = this.applyGroupRepository.findById(request.getApplyId())
        .orElseThrow(NoApplyGroupException::new);

    if(!Objects.equals(user.getUserId(), applyGroup.getUserEntity().getUserId())) {
      throw new NoYourSelfException();
    }

    return GetDetailResponse.builder()
        .applyId(applyGroup.getApplyId())
        .applyContent(applyGroup.getApplyContent())
        .createdAt(applyGroup.getCreatedAt())
        .status(applyGroup.getStatus())
        .groupTitle(applyGroup.getGroupBoardEntity().getGroupTitle())
        .build();

  }

  /**
   * 모임 신청 수정
   */
  public UpdateResponse update(UpdateRequest request, String token) {

    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var applyGroup = this.applyGroupRepository.findByApplyIdAndStatus(request.getApplyId(), APPLY.getValue())
        .orElseThrow(NoApplyGroupException::new);


    if(!Objects.equals(user.getUserId(), applyGroup.getUserEntity().getUserId())) {
      throw new NoYourSelfException();
    }

    ApplyGroupEntity applyGroupEntity = ApplyGroupEntity.builder()
        .applyId(applyGroup.getApplyId())
        .applyContent(request.getApplyContent())
        .createdAt(applyGroup.getCreatedAt())
        .updatedAt(LocalDateTime.now())
        .status(applyGroup.getStatus())
        .groupBoardEntity(applyGroup.getGroupBoardEntity())
        .userEntity(applyGroup.getUserEntity())
        .build();

    this.applyGroupRepository.save(applyGroupEntity);

    return UpdateResponse.builder()
        .applyContent(request.getApplyContent())
        .updateAt(applyGroupEntity.getUpdatedAt())
        .build();
  }

  /**
   * 모임 신청 수락
   */
  public void accept(AcceptRequest request, String token) {
    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var applyGroup = this.applyGroupRepository.findById(request.getApplyId())
        .orElseThrow(NoApplyGroupException::new);

    var group = this.groupBoardRepository.findById(applyGroup.getGroupBoardEntity().getGroupId())
        .orElseThrow(NoGroupException::new);

    // 모임장만 수락 가능
    if(!group.getUserEntity().getUserId().equals(user.getUserId())) {
      throw new NoGroupLeaderException();
    }

    // 신청 상태가 아닐때 오류
    if(!applyGroup.getStatus().equals(APPLY.getValue())) {
      throw new NoApplyStatusException();
    }

    // 인원이 다 찬 상태에서 수락을 할때 오류
    Long headCount = this.applyGroupRepository.countByGroupBoardEntityGroupIdAndStatus(group.getGroupId(), ACCEPT.getValue());
    if(headCount >= group.getHeadCount()) {
      throw new FullGroupException();
    }

    ApplyGroupEntity applyGroupEntity = ApplyGroupEntity.builder()
        .applyId(applyGroup.getApplyId())
        .applyContent(applyGroup.getApplyContent())
        .createdAt(applyGroup.getCreatedAt())
        .updatedAt(applyGroup.getUpdatedAt())
        .status(ACCEPT.getValue())
        .groupBoardEntity(applyGroup.getGroupBoardEntity())
        .userEntity(applyGroup.getUserEntity())
        .build();

    this.applyGroupRepository.save(applyGroupEntity);
  }

  /**
   * 모임 신청 철회
   */
  public void reject(RejectRequest request, String token) {
    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var applyGroup = this.applyGroupRepository.findByApplyIdAndStatusIn(request.getApplyId(), List.of(APPLY.getValue(), ACCEPT.getValue()))
        .orElseThrow(NoApplyGroupException::new);

    var group = this.groupBoardRepository.findById(applyGroup.getGroupBoardEntity().getGroupId())
        .orElseThrow(NoGroupException::new);
    
    // 모임장만 회원들의 철회가 가능
    if(!Objects.equals(user.getUserId(), group.getUserEntity().getUserId())) {
      throw new NoGroupLeaderException();
    }

    // 이미 철회한 상태일 경우
    if(applyGroup.getStatus().equals(REJECT.getValue())) {
      throw new AlreadyRejectStatusException();
    }

    // 모임장은 철회가 안됨
    if(group.getUserEntity().getUserId().equals(applyGroup.getUserEntity().getUserId())) {
      throw new NoRejectException();
    }

    ApplyGroupEntity applyGroupEntity = ApplyGroupEntity.builder()
        .applyId(applyGroup.getApplyId())
        .applyContent(applyGroup.getApplyContent())
        .createdAt(applyGroup.getCreatedAt())
        .updatedAt(applyGroup.getUpdatedAt())
        .status(REJECT.getValue())
        .groupBoardEntity(applyGroup.getGroupBoardEntity())
        .userEntity(applyGroup.getUserEntity())
        .build();

    this.applyGroupRepository.save(applyGroupEntity);

  }



}
