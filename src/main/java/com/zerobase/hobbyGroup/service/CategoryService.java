package com.zerobase.hobbyGroup.service;

import com.zerobase.hobbyGroup.dto.Category;
import com.zerobase.hobbyGroup.dto.Category.DeleteReponse;
import com.zerobase.hobbyGroup.dto.Category.DeleteRequest;
import com.zerobase.hobbyGroup.dto.Category.GetListReponse;
import com.zerobase.hobbyGroup.dto.Category.UpdateReponse;
import com.zerobase.hobbyGroup.dto.Category.UpdateRequest;
import com.zerobase.hobbyGroup.dto.User.SignUpRequest;
import com.zerobase.hobbyGroup.entity.CategoryEntity;
import com.zerobase.hobbyGroup.exception.impl.category.NoCategoryException;
import com.zerobase.hobbyGroup.exception.impl.user.NoUserException;
import com.zerobase.hobbyGroup.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {

  private CategoryRepository categoryRepository;

  public Category.CreateReponse create(Category.CreateRequest request) {
    CategoryEntity categoryEntity = CategoryEntity.builder()
        .categoryName(request.getCategoryName())
        .build();

    CategoryEntity category = this.categoryRepository.save(categoryEntity);

    return Category.CreateReponse.builder()
        .categoryName(category.getCategoryName())
        .build();
  }

  public List<GetListReponse> getCategoryListByCategoryName() {

    List<CategoryEntity> list = this.categoryRepository.findAllByOrderByCategoryName();

    List<GetListReponse> resultList = list.stream().map(
        categoryEntity -> {
          return GetListReponse.builder()
              .categoryName(categoryEntity.getCategoryName())
              .build();
        }
    ).collect(Collectors.toList());

    return resultList;
  }

  public Category.UpdateReponse update(UpdateRequest request) {

    var category = this.categoryRepository.findById(request.getCategoryId())
        .orElseThrow(NoCategoryException::new);

    CategoryEntity categoryEntity = CategoryEntity.builder()
        .categoryId(request.getCategoryId())
        .categoryName(request.getCategoryName())
        .build();

    CategoryEntity result = this.categoryRepository.save(categoryEntity);

    return Category.UpdateReponse.builder()
        .categoryName(result.getCategoryName())
        .build();
  }

  public DeleteReponse delete(DeleteRequest request) {

    var category = this.categoryRepository.findById(request.getCategoryId())
        .orElseThrow(NoCategoryException::new);

    this.categoryRepository.deleteById(request.getCategoryId());

    return Category.DeleteReponse.builder()
        .categoryName(category.getCategoryName())
        .build();
  }
}
