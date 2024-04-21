package com.zerobase.hobbyGroup.service;

import com.zerobase.hobbyGroup.dto.Category;
import com.zerobase.hobbyGroup.dto.Category.DeleteReponse;
import com.zerobase.hobbyGroup.dto.Category.DeleteRequest;
import com.zerobase.hobbyGroup.dto.Category.GetListReponse;
import com.zerobase.hobbyGroup.dto.Category.UpdateRequest;
import com.zerobase.hobbyGroup.entity.CategoryEntity;
import com.zerobase.hobbyGroup.exception.impl.category.AlreadyCategoryException;
import com.zerobase.hobbyGroup.exception.impl.category.NoCategoryException;
import com.zerobase.hobbyGroup.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public Category.CreateReponse create(Category.CreateRequest request) {

    // validation
    if(this.categoryRepository.existsByCategoryName(request.getCategoryName())) {
      throw new AlreadyCategoryException();
    }

    CategoryEntity categoryEntity = CategoryEntity.builder()
        .categoryName(request.getCategoryName())
        .build();

    CategoryEntity result = this.categoryRepository.save(categoryEntity);

    return Category.CreateReponse.builder()
        .categoryName(result.getCategoryName())
        .build();
  }

  public List<GetListReponse> getCategoryListByCategoryName() {

    List<CategoryEntity> categoryList = this.categoryRepository.findAllByOrderByCategoryName();

    List<GetListReponse> resultList = categoryList.stream().map(
        CategoryEntity -> {
          return GetListReponse.builder()
              .categoryId(CategoryEntity.getCategoryId())
              .categoryName(CategoryEntity.getCategoryName())
              .build();
        }
    ).collect(Collectors.toList());

    return resultList;
  }

  public Category.UpdateReponse update(UpdateRequest request) {

    // validation
    var category = this.categoryRepository.findById(request.getCategoryId())
        .orElseThrow(NoCategoryException::new);

    CategoryEntity categoryEntity = CategoryEntity.builder()
        .categoryId(category.getCategoryId())
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

    this.categoryRepository.deleteById(category.getCategoryId());

    return DeleteReponse.builder()
        .categoryName(category.getCategoryName())
        .build();
  }

}
