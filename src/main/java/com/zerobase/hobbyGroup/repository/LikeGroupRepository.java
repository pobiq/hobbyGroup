package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.LikeGroupEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeGroupRepository extends JpaRepository<LikeGroupEntity, Long> {

  List<LikeGroupEntity> findByUserEntityUserId(Long userId);
}
