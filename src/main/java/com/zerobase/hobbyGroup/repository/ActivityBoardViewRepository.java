package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.ActivityBoardEntity;
import com.zerobase.hobbyGroup.entity.ActivityBoardViewEntity;
import com.zerobase.hobbyGroup.entity.GroupBoardViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityBoardViewRepository extends JpaRepository<ActivityBoardViewEntity, Long> {

  boolean existsByActivityBoardEntityActivityIdAndUserEntityUserId(Long activityId, Long userId);

  void deleteByActivityBoardEntityActivityId(Long activityId);
}
