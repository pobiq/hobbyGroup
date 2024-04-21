package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.GroupBoardViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupBoardViewRepository extends JpaRepository<GroupBoardViewEntity, Long> {

  boolean existsByGroupBoardEntityGroupIdAndUserEntityUserId(Long groupId, Long userId);

  void deleteByGroupBoardEntityGroupId(Long groupId);
}
