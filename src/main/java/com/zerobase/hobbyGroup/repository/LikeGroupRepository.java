package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.ApplyGroupEntity;
import com.zerobase.hobbyGroup.entity.LikeGroupEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeGroupRepository extends JpaRepository<LikeGroupEntity, Long> {

  List<LikeGroupEntity> findByUserEntityUserId(Long userId);
}
