package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.ApplyGroupEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyGroupRepository extends JpaRepository<ApplyGroupEntity, Long> {

  boolean existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatusIn(Long groupId, Long userId, List<String> status);

  Long countByGroupBoardEntityGroupIdAndStatus(Long groupId, String status);

  Page<ApplyGroupEntity> findByGroupBoardEntityGroupId(Pageable pageRequest, Long groupId);

  Optional<ApplyGroupEntity> findByApplyIdAndStatus(Long applyId, String status);

  Page<ApplyGroupEntity> findByUserEntityUserId(PageRequest pageRequest, Long userId);

  void deleteByGroupBoardEntityGroupId(Long groupId);

  Optional<ApplyGroupEntity> findByApplyIdAndStatusIn(Long applyId, List<String> status);

  boolean existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(Long groupId, Long userId, String value);

  boolean existsByGroupBoardEntityGroupIdAndStatus(Long groupId, String value);
}
