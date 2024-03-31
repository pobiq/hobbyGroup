package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByEmail(String email);

  boolean existsByEmail(String email);
}
