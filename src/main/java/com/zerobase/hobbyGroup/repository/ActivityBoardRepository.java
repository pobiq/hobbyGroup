package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.ActivityBoardEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityBoardRepository extends JpaRepository<ActivityBoardEntity, Long> {

  Long countByGroupBoardEntityGroupIdAndType(Long groupId, String value);

  Page<ActivityBoardEntity> findByType(PageRequest pageRequest, String value);
  List<ActivityBoardEntity> findByType(String value);
}
