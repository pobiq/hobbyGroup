package com.zerobase.hobbyGroup.service;

import static com.zerobase.hobbyGroup.type.ApplyStatus.ACCEPT;

import com.zerobase.hobbyGroup.dto.GroupBoard.CreateRequest;
import com.zerobase.hobbyGroup.dto.GroupBoard.CreateResponse;
import com.zerobase.hobbyGroup.dto.GroupBoard.DeleteRequest;
import com.zerobase.hobbyGroup.dto.GroupBoard.DeleteResponse;
import com.zerobase.hobbyGroup.dto.GroupBoard.GetDetailReponse;
import com.zerobase.hobbyGroup.dto.GroupBoard.GetDetailRequest;
import com.zerobase.hobbyGroup.dto.GroupBoard.GetListRequest;
import com.zerobase.hobbyGroup.dto.GroupBoard.GetListResponse;
import com.zerobase.hobbyGroup.dto.GroupBoard.UpdateRequest;
import com.zerobase.hobbyGroup.dto.GroupBoard.UpdateResponse;
import com.zerobase.hobbyGroup.entity.ApplyGroupEntity;
import com.zerobase.hobbyGroup.entity.FileEntity;
import com.zerobase.hobbyGroup.entity.GroupBoardEntity;
import com.zerobase.hobbyGroup.entity.GroupBoardViewEntity;
import com.zerobase.hobbyGroup.exception.impl.auth.NoAuthException;
import com.zerobase.hobbyGroup.exception.impl.category.NoCategoryException;
import com.zerobase.hobbyGroup.exception.impl.file.NoFileException;
import com.zerobase.hobbyGroup.exception.impl.file.NotCreationFolderException;
import com.zerobase.hobbyGroup.exception.impl.group.NoGroupException;
import com.zerobase.hobbyGroup.exception.impl.other.NoYourSelfException;
import com.zerobase.hobbyGroup.exception.impl.user.NoUserException;
import com.zerobase.hobbyGroup.repository.ApplyGroupRepository;
import com.zerobase.hobbyGroup.repository.CategoryRepository;
import com.zerobase.hobbyGroup.repository.FileRepository;
import com.zerobase.hobbyGroup.repository.GroupBoardRepository;
import com.zerobase.hobbyGroup.repository.GroupBoardViewRepository;
import com.zerobase.hobbyGroup.repository.UserRepository;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.type.Roles;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@AllArgsConstructor
public class GroupBoardService {

  private final GroupBoardRepository groupBoardRepository;

  private final UserRepository userRepository;

  private final CategoryRepository categoryRepository;

  private final FileRepository fileRepository;

  private final GroupBoardViewRepository groupBoardViewRepository;

  private final ApplyGroupRepository applyGroupRepository;

  private final TokenProvider tokenProvider;

  /**
   * 그룹 모집 게시물 추가
   * @param request
   * @param token
   * @param file
   * @return
   * @throws Exception
   */
  public CreateResponse create(CreateRequest request, String token, MultipartFile file) throws Exception {
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var category = this.categoryRepository.findById(request.getCategoryId())
        .orElseThrow(NoCategoryException::new);


    // 현재 날짜 가져오기
    LocalDate currentDate = LocalDate.now();

    // 날짜를 문자열로 변환하기
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy\\MM\\dd");
    String formattedDate = currentDate.format(formatter);

    String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files\\" + formattedDate;
    UUID uuid = UUID.randomUUID();
    String fileName = uuid + "_" + file.getOriginalFilename();
    File saveFile = new File(projectPath, fileName);
    if(!saveFile.exists()) {
      boolean mkdir = saveFile.mkdirs();
      if (!mkdir) {
        throw new NotCreationFolderException();
      }
    }

    file.transferTo(saveFile);

    FileEntity fileEntity = FileEntity.builder()
        .filePath(projectPath)
        .uuid(uuid.toString())
        .fileOriginalName(file.getOriginalFilename())
        .fileSize(file.getSize())
        .build();

    this.fileRepository.save(fileEntity);

    GroupBoardEntity groupBoardEntity = GroupBoardEntity.builder()
        .groupTitle(request.getGroupTitle())
        .headCount(request.getHeadCount())
        .groupContent(request.getGroupContent())
        .startAt(request.getStartAt())
        .endAt(request.getEndAt())
        .createdAt(LocalDateTime.now())
        .viewCount(0L)
        .fileEntity(fileEntity)
        .categoryEntity(category)
        .userEntity(user)
        .build();

    this.groupBoardRepository.save(groupBoardEntity);

    ApplyGroupEntity applyGroupEntity = ApplyGroupEntity.builder()
        .applyContent("모임장")
        .createdAt(LocalDateTime.now())
        .status(ACCEPT.getValue())
        .groupBoardEntity(groupBoardEntity)
        .userEntity(user)
        .build();

    this.applyGroupRepository.save(applyGroupEntity);

    return CreateResponse.builder()
        .groupTitle(groupBoardEntity.getGroupTitle())
        .headCount(groupBoardEntity.getHeadCount())
        .groupContent(groupBoardEntity.getGroupContent())
        .startAt(groupBoardEntity.getStartAt())
        .endAt(groupBoardEntity.getEndAt())
        .createdAt(groupBoardEntity.getCreatedAt())
        .filePath(fileEntity.getFilePath())
        .fileOriginalName(fileEntity.getFileOriginalName())
        .categoryName(category.getCategoryName())
        .userName(user.getUsername())
        .build();
  }

  public List<GetListResponse> getLatestList(GetListRequest request) {

    List<Long> categoryIdList = request.getCatoryIdList();

    Page<GroupBoardEntity> groupBoardEntityList;

    PageRequest pageRequest = PageRequest.of(request.getPage(), 10, Sort.by("createdAt").descending());

    if(categoryIdList == null) {
      groupBoardEntityList = this.groupBoardRepository.findAll(pageRequest);
    } else {
      groupBoardEntityList = this.groupBoardRepository.findByCategoryEntityCategoryIdIn(categoryIdList, pageRequest);
    }

    List<GetListResponse> resultList = new ArrayList<>();
    for(GroupBoardEntity entity: groupBoardEntityList) {
      GetListResponse getLatestListResponse = GetListResponse.builder()
          .groupId(entity.getGroupId())
          .groupTitle(entity.getGroupTitle())
          .createdAt(entity.getCreatedAt())
          .filePath(entity.getFileEntity().getFilePath())
          .fileName(entity.getFileEntity().getUuid() + "_" + entity.getFileEntity().getFileOriginalName())
          .categoryName(entity.getCategoryEntity().getCategoryName())
          .nickname(entity.getUserEntity().getNickname())
          .viewCount(entity.getViewCount())
          .build();

      resultList.add(getLatestListResponse);
    }

    return resultList;
  }

  public List<GetListResponse> getTitleList(GetListRequest request) {

    List<Long> categoryIdList = request.getCatoryIdList();

    Page<GroupBoardEntity> groupBoardEntityList;

    PageRequest pageRequest = PageRequest.of(request.getPage(), 10, Sort.by("groupTitle").ascending());

    if(categoryIdList == null) {
      groupBoardEntityList = this.groupBoardRepository.findAll(pageRequest);
    } else {
      groupBoardEntityList = this.groupBoardRepository.findByCategoryEntityCategoryIdIn(categoryIdList, pageRequest);
    }

    List<GetListResponse> resultList = new ArrayList<>();
    for(GroupBoardEntity entity: groupBoardEntityList) {
      GetListResponse getLatestListResponse = GetListResponse.builder()
          .groupId(entity.getGroupId())
          .groupTitle(entity.getGroupTitle())
          .createdAt(entity.getCreatedAt())
          .filePath(entity.getFileEntity().getFilePath())
          .fileName(entity.getFileEntity().getUuid() + "_" + entity.getFileEntity().getFileOriginalName())
          .categoryName(entity.getCategoryEntity().getCategoryName())
          .nickname(entity.getUserEntity().getNickname())
          .viewCount(entity.getViewCount())
          .build();

      resultList.add(getLatestListResponse);
    }

    return resultList;
  }

  public List<GetListResponse> getViewList(GetListRequest request) {

    List<Long> categoryIdList = request.getCatoryIdList();

    Page<GroupBoardEntity> groupBoardEntityList;

    PageRequest pageRequest = PageRequest.of(request.getPage(), 10, Sort.by("viewCount").descending());

    if(categoryIdList == null) {
      groupBoardEntityList = this.groupBoardRepository.findAll(pageRequest);
    } else {
      groupBoardEntityList = this.groupBoardRepository.findByCategoryEntityCategoryIdIn(categoryIdList, pageRequest);
    }

    List<GetListResponse> resultList = new ArrayList<>();
    for(GroupBoardEntity entity: groupBoardEntityList) {
      GetListResponse getLatestListResponse = GetListResponse.builder()
          .groupId(entity.getGroupId())
          .groupTitle(entity.getGroupTitle())
          .createdAt(entity.getCreatedAt())
          .filePath(entity.getFileEntity().getFilePath())
          .fileName(entity.getFileEntity().getUuid() + "_" + entity.getFileEntity().getFileOriginalName())
          .categoryName(entity.getCategoryEntity().getCategoryName())
          .nickname(entity.getUserEntity().getNickname())
          .viewCount(entity.getViewCount())
          .build();

      resultList.add(getLatestListResponse);
    }

    return resultList;

  }

  public GetDetailReponse getDetail(GetDetailRequest request, String token) {

    // vaildation
    var group = this.groupBoardRepository.findById(request.getGroupId())
        .orElseThrow(NoGroupException::new);

    Long viewCount = group.getViewCount();

    if(!token.isEmpty()) {
      String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

      var user = this.userRepository.findByEmail(email)
          .orElseThrow(NoUserException::new);

      boolean flag = this.groupBoardViewRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserId(group.getGroupId(), user.getUserId());

      if(!flag) {
        GroupBoardViewEntity groupBoardViewEntity = GroupBoardViewEntity.builder()
            .groupBoardEntity(group)
            .userEntity(user)
            .createAt(LocalDateTime.now())
            .build();

        this.groupBoardViewRepository.save(groupBoardViewEntity);

        viewCount++;

        group.setViewCount(viewCount);

        this.groupBoardRepository.save(group);
      }
    }

    return GetDetailReponse.builder()
        .groupTitle(group.getGroupTitle())
        .headCount(group.getHeadCount())
        .groupContent(group.getGroupContent())
        .startAt(group.getStartAt())
        .endAt(group.getEndAt())
        .createdAt(group.getCreatedAt())
        .filePath(group.getFileEntity().getFilePath())
        .fileOriginalName(group.getFileEntity().getFileOriginalName())
        .categoryName(group.getCategoryEntity().getCategoryName())
        .nickname(group.getUserEntity().getNickname())
        .viewCount(viewCount)
        .build();

  }

  public UpdateResponse update(UpdateRequest request, MultipartFile file, String token) throws Exception {

    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var category = this.categoryRepository.findById(request.getCategoryId())
        .orElseThrow(NoCategoryException::new);

    var group = this.groupBoardRepository.findById(request.getGroupId())
        .orElseThrow(NoGroupException::new);

    FileEntity fileEntity = this.fileRepository.findById(group.getFileEntity().getFileId())
        .orElseThrow(NoFileException::new);

    // validation
    if(!Objects.equals(user.getUserId(), group.getUserEntity().getUserId())) {
      throw new NoYourSelfException();
    }

    // 사진을 수정할 경우
    if(!file.isEmpty()) {

      // 기존의 파일 삭제
      String filePath = group.getFileEntity().getFilePath() + "\\" + group.getFileEntity().getUuid() + "_" + group.getFileEntity().getFileOriginalName();
      File originalFile = new File(filePath);

      if (originalFile.delete()) {
        log.info("File deleted successfully");
      } else {
        log.info("Failed to delete the file");
      }

      // 현재 날짜 가져오기
      LocalDate currentDate = LocalDate.now();

      // 날짜를 문자열로 변환하기
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy\\MM\\dd");
      String formattedDate = currentDate.format(formatter);

      String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files\\" + formattedDate;
      UUID uuid = UUID.randomUUID();
      String fileName = uuid + "_" + file.getOriginalFilename();
      File saveFile = new File(projectPath, fileName);
      if(!saveFile.exists()) {
        boolean mkdir = saveFile.mkdirs();
        if (!mkdir) {
          throw new NotCreationFolderException();
        }
      }

      file.transferTo(saveFile);

      fileEntity = FileEntity.builder()
          .fileId(group.getFileEntity().getFileId())
          .filePath(projectPath)
          .uuid(uuid.toString())
          .fileOriginalName(file.getOriginalFilename())
          .fileSize(file.getSize())
          .build();

      this.fileRepository.save(fileEntity);
    }

    GroupBoardEntity groupBoardEntity = GroupBoardEntity.builder()
        .groupId(group.getGroupId())
        .groupTitle(request.getGroupTitle())
        .headCount(request.getHeadCount())
        .groupContent(request.getGroupContent())
        .startAt(request.getStartAt())
        .endAt(request.getEndAt())
        .createdAt(group.getCreatedAt())
        .updatedAt(LocalDateTime.now())
        .viewCount(group.getViewCount())
        .fileEntity(fileEntity)
        .categoryEntity(category)
        .userEntity(user)
        .build();

    this.groupBoardRepository.save(groupBoardEntity);

    return UpdateResponse.builder()
        .groupTitle(groupBoardEntity.getGroupTitle())
        .headCount(groupBoardEntity.getHeadCount())
        .groupContent(groupBoardEntity.getGroupContent())
        .startAt(groupBoardEntity.getStartAt())
        .endAt(groupBoardEntity.getEndAt())
        .updatedAt(groupBoardEntity.getUpdatedAt())
        .viewCount(groupBoardEntity.getViewCount())
        .filePath(fileEntity.getFilePath())
        .fileOriginalName(fileEntity.getFileOriginalName())
        .categoryName(category.getCategoryName())
        .build();

  }

  public DeleteResponse delete(DeleteRequest request, String token) {

    // validation
    var group = this.groupBoardRepository.findById(request.getGroupId())
        .orElseThrow(NoGroupException::new);

    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    List<String> roles = user.getRoles();

    boolean userFlag = false;

    for(String role : roles) {
      if(role.equals(Roles.USER.getValue()))
        userFlag = true;
    }

    // 일반 유저가 삭제할 때
    if(userFlag) {
      if(!Objects.equals(user.getUserId(), group.getUserEntity().getUserId())) {
        throw new NoAuthException();
      }
    }

    this.applyGroupRepository.deleteByGroupBoardEntityGroupId(group.getGroupId());

    this.groupBoardViewRepository.deleteByGroupBoardEntityGroupId(group.getGroupId());

    this.groupBoardRepository.deleteById(request.getGroupId());

    this.fileRepository.deleteById(group.getFileEntity().getFileId());

    // 기존의 파일 삭제
    String filePath = group.getFileEntity().getFilePath() + "\\" + group.getFileEntity().getUuid() + "_" + group.getFileEntity().getFileOriginalName();
    File originalFile = new File(filePath);

    if (originalFile.delete()) {
      log.info("File deleted successfully");
    } else {
      log.info("Failed to delete the file");
    }

    return DeleteResponse.builder()
        .groupTitle(group.getGroupTitle())
        .build();
  }


}
