package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.CategoryEntity;
import com.zerobase.hobbyGroup.entity.GroupBoardEntity;
import com.zerobase.hobbyGroup.entity.UserEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupBoardRepository extends JpaRepository<GroupBoardEntity, Long> {

  Page<GroupBoardEntity> findByCategoryEntityCategoryIdIn(List<Long> categoryIdList, Pageable pageRequest);

}
