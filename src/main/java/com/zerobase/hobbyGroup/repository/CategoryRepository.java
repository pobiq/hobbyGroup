package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.CategoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
  List<CategoryEntity> findAllByOrderByCategoryName();

  Optional<CategoryEntity> findByCategoryName(String categoryName);

  boolean existsByCategoryName(String categoryName);
}
