package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.CategoryEntity;
import com.zerobase.hobbyGroup.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
  List<CategoryEntity> findAllByOrderByCategoryName();
}
