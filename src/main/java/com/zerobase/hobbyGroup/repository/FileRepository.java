package com.zerobase.hobbyGroup.repository;

import com.zerobase.hobbyGroup.entity.CategoryEntity;
import com.zerobase.hobbyGroup.entity.FileEntity;
import java.io.File;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

}
