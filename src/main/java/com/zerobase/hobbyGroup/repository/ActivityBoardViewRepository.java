package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.ActivityBoardViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityBoardViewRepository extends JpaRepository<ActivityBoardViewEntity, Long> {

  boolean existsByActivityBoardEntityActivityIdAndUserEntityUserId(Long activityId, Long userId);

  void deleteByActivityBoardEntityActivityId(Long activityId);
}
