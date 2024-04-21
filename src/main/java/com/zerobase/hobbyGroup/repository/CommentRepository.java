package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

  Page<CommentEntity> findByActivityBoardEntityActivityId(Long activityId, PageRequest pageRequest);
}
