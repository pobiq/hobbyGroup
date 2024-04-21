package com.zerobase.hobbyGroup.service;

import com.zerobase.hobbyGroup.dto.LikeGroup.CreateRequest;
import com.zerobase.hobbyGroup.dto.LikeGroup.DeleteRequest;
import com.zerobase.hobbyGroup.dto.LikeGroup.GetSelfListRequest;
import com.zerobase.hobbyGroup.dto.LikeGroup.GetSelfListResponse;
import com.zerobase.hobbyGroup.entity.LikeGroupEntity;
import com.zerobase.hobbyGroup.exception.impl.group.NoGroupException;
import com.zerobase.hobbyGroup.exception.impl.other.NoYourSelfException;
import com.zerobase.hobbyGroup.exception.impl.user.NoUserException;
import com.zerobase.hobbyGroup.exception.impl.likeGroup.NoLikeGroupException;
import com.zerobase.hobbyGroup.repository.ApplyGroupRepository;
import com.zerobase.hobbyGroup.repository.GroupBoardRepository;
import com.zerobase.hobbyGroup.repository.LikeGroupRepository;
import com.zerobase.hobbyGroup.repository.UserRepository;
import com.zerobase.hobbyGroup.security.TokenProvider;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LikeGroupService {

  private final UserRepository userRepository;

  private final ApplyGroupRepository applyGroupRepository;

  private final GroupBoardRepository groupBoardRepository;

  private final LikeGroupRepository likeGroupRepository;

  private final TokenProvider tokenProvider;

  /**
   * 모임 찜 추가
   */
  public void create(CreateRequest request, String token) {
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var group = this.groupBoardRepository.findById(request.getGroupId())
        .orElseThrow(NoGroupException::new);

    LikeGroupEntity likeGroupEntity = LikeGroupEntity.builder()
        .createdAt(LocalDateTime.now())
        .groupBoardEntity(group)
        .userEntity(user)
        .build();

    this.likeGroupRepository.save(likeGroupEntity);
  }

  /**
   * 모임 찜 개별 목록 조회
   */
  public List<GetSelfListResponse> getSelfList(GetSelfListRequest request, String token) {
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    PageRequest pageRequest = PageRequest.of(request.getPage(), 20, Sort.by("createdAt").descending());

    List<LikeGroupEntity> likeGroupEntityList = this.likeGroupRepository.findByUserEntityUserId(user.getUserId());

    List<GetSelfListResponse> result = new ArrayList<>();

    for(LikeGroupEntity entity : likeGroupEntityList) {
      GetSelfListResponse getSelfListResponse = GetSelfListResponse.builder()
          .likeGroupId(entity.getLikeGroupId())
          .createdAt(entity.getCreatedAt())
          .groupTitle(entity.getGroupBoardEntity().getGroupTitle())
          .build();
      result.add(getSelfListResponse);
    }

    return result;
  }

  /**
   * 모임 찜 삭제
   */
  public void delete(DeleteRequest request, String token) {
    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    LikeGroupEntity likeGroupEntity = this.likeGroupRepository.findById(request.getLikeGroupId())
        .orElseThrow(NoLikeGroupException::new);

    // 본인만 삭제 가능
    if(!Objects.equals(user.getUserId(), likeGroupEntity.getUserEntity().getUserId())) {
      throw new NoYourSelfException();
    }

    this.likeGroupRepository.deleteById(request.getLikeGroupId());
  }
}
