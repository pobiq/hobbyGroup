package com.zerobase.hobbyGroup.service;

import static com.zerobase.hobbyGroup.type.ActivityStatus.GENERAL;
import static com.zerobase.hobbyGroup.type.ActivityStatus.NOTICE;
import static com.zerobase.hobbyGroup.type.ApplyStatus.ACCEPT;

import com.zerobase.hobbyGroup.dto.ActivityBoard.CreateRequest;
import com.zerobase.hobbyGroup.dto.ActivityBoard.CreateResponse;
import com.zerobase.hobbyGroup.dto.ActivityBoard.DeleteRequest;
import com.zerobase.hobbyGroup.dto.ActivityBoard.DeleteResponse;
import com.zerobase.hobbyGroup.dto.ActivityBoard.GetDetailReponse;
import com.zerobase.hobbyGroup.dto.ActivityBoard.GetDetailRequest;
import com.zerobase.hobbyGroup.dto.ActivityBoard.GetListRequest;
import com.zerobase.hobbyGroup.dto.ActivityBoard.GetListResponse;
import com.zerobase.hobbyGroup.dto.ActivityBoard.UpdateRequest;
import com.zerobase.hobbyGroup.dto.ActivityBoard.UpdateResponse;
import com.zerobase.hobbyGroup.entity.ActivityBoardEntity;
import com.zerobase.hobbyGroup.entity.ActivityBoardViewEntity;
import com.zerobase.hobbyGroup.entity.FileEntity;
import com.zerobase.hobbyGroup.exception.impl.activity.NoActivityBoardException;
import com.zerobase.hobbyGroup.exception.impl.activity.NoCreateNoticeBoardException;
import com.zerobase.hobbyGroup.exception.impl.apply.NoAcceptStatusException;
import com.zerobase.hobbyGroup.exception.impl.auth.NoAuthException;
import com.zerobase.hobbyGroup.exception.impl.auth.NoLeaderException;
import com.zerobase.hobbyGroup.exception.impl.file.NoFileException;
import com.zerobase.hobbyGroup.exception.impl.file.NotCreationFolderException;
import com.zerobase.hobbyGroup.exception.impl.group.NoGroupException;
import com.zerobase.hobbyGroup.exception.impl.other.NoYourSelfException;
import com.zerobase.hobbyGroup.exception.impl.user.NoUserException;
import com.zerobase.hobbyGroup.repository.ActivityBoardRepository;
import com.zerobase.hobbyGroup.repository.ActivityBoardViewRepository;
import com.zerobase.hobbyGroup.repository.ApplyGroupRepository;
import com.zerobase.hobbyGroup.repository.CommentRepository;
import com.zerobase.hobbyGroup.repository.FileRepository;
import com.zerobase.hobbyGroup.repository.GroupBoardRepository;
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
public class ActivityBoardService {

  private final GroupBoardRepository groupBoardRepository;

  private final UserRepository userRepository;

  private final FileRepository fileRepository;

  private final ApplyGroupRepository applyGroupRepository;

  private final ActivityBoardRepository activityBoardRepository;

  private final ActivityBoardViewRepository activityBoardViewRepository;

  private final CommentRepository commentRepository;

  private final TokenProvider tokenProvider;

  /**
   * 활동 게시물 추가(일반글)
   */
  public CreateResponse createGeneral(CreateRequest request, String token, MultipartFile file) throws Exception {

    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var group = this.groupBoardRepository.findById(request.getGroupId())
        .orElseThrow(NoGroupException::new);

    boolean flag = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(request.getGroupId(), user.getUserId(), ACCEPT.getValue());

    // 모임에 가입된 상태가 아닐시
    if(!flag) {
      throw new NoAcceptStatusException();
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

    FileEntity fileEntity = FileEntity.builder()
        .filePath(projectPath)
        .uuid(uuid.toString())
        .fileOriginalName(file.getOriginalFilename())
        .fileSize(file.getSize())
        .build();

    this.fileRepository.save(fileEntity);

    ActivityBoardEntity activityBoardEntity = ActivityBoardEntity.builder()
        .activityTitle(request.getActivityTitle())
        .activityContent(request.getActivityContent())
        .createdAt(LocalDateTime.now())
        .viewCount(0L)
        .fileEntity(fileEntity)
        .groupBoardEntity(group)
        .type(GENERAL.getValue())
        .userEntity(user)
        .build();


    this.activityBoardRepository.save(activityBoardEntity);

    return CreateResponse.builder()
        .activityTitle(activityBoardEntity.getActivityTitle())
        .activityContent(activityBoardEntity.getActivityContent())
        .type(activityBoardEntity.getType())
        .createdAt(activityBoardEntity.getCreatedAt())
        .viewCount(activityBoardEntity.getViewCount())
        .filePath(fileEntity.getFilePath())
        .fileOriginalName(fileEntity.getFileOriginalName())
        .nickname(user.getNickname())
        .build();
  }

  /**
   * 활동 게시물 추가(공지글)
   */
  public CreateResponse createNotice(CreateRequest request, String token, MultipartFile file) throws Exception {

    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var group = this.groupBoardRepository.findById(request.getGroupId())
        .orElseThrow(NoGroupException::new);

    boolean flag = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(request.getGroupId(), user.getUserId(), ACCEPT.getValue());

    // 모임에 가입된 상태가 아닐시
    if(!flag) {
      throw new NoAcceptStatusException();
    }

    // 공지글은 모임장만 생성 가능
    if(!group.getUserEntity().getUserId().equals(user.getUserId())) {
      throw new NoLeaderException();
    }

    // 공지글 2개 이상 일시
    Long noticeCount = this.activityBoardRepository.countByGroupBoardEntityGroupIdAndType(request.getGroupId(), NOTICE.getValue());

    if(noticeCount >= 2) {
      throw new NoCreateNoticeBoardException();
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

    FileEntity fileEntity = FileEntity.builder()
        .filePath(projectPath)
        .uuid(uuid.toString())
        .fileOriginalName(file.getOriginalFilename())
        .fileSize(file.getSize())
        .build();

    this.fileRepository.save(fileEntity);

    ActivityBoardEntity activityBoardEntity = ActivityBoardEntity.builder()
        .activityTitle(request.getActivityTitle())
        .activityContent(request.getActivityContent())
        .createdAt(LocalDateTime.now())
        .viewCount(0L)
        .fileEntity(fileEntity)
        .groupBoardEntity(group)
        .type(NOTICE.getValue())
        .userEntity(user)
        .build();


    this.activityBoardRepository.save(activityBoardEntity);

    return CreateResponse.builder()
        .activityTitle(activityBoardEntity.getActivityTitle())
        .activityContent(activityBoardEntity.getActivityContent())
        .type(activityBoardEntity.getType())
        .createdAt(activityBoardEntity.getCreatedAt())
        .viewCount(activityBoardEntity.getViewCount())
        .filePath(fileEntity.getFilePath())
        .fileOriginalName(fileEntity.getFileOriginalName())
        .nickname(user.getNickname())
        .build();
  }

  /**
   * 활동 게시물 조회(최신 순)
   */
  public List<GetListResponse> getLatestList(GetListRequest request, String token) {

    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    boolean flag = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(request.getGroupId(), user.getUserId(), ACCEPT.getValue());

    // 모임에 가입된 상태가 아닐시
    if(!flag) {
      throw new NoAcceptStatusException();
    }

    List<ActivityBoardEntity> noticeList = this.activityBoardRepository.findByType(NOTICE.getValue());

    PageRequest pageRequest = PageRequest.of(request.getPage(), 10, Sort.by("createdAt").descending());

    Page<ActivityBoardEntity> activityBoardList = this.activityBoardRepository.findByType(pageRequest, GENERAL.getValue());

    List<GetListResponse> resultList = new ArrayList<>();

    for(ActivityBoardEntity entity : noticeList) {
      GetListResponse getListResponse = GetListResponse.builder()
          .activityId(entity.getActivityId())
          .activityTitle(entity.getActivityTitle())
          .activityContent(entity.getActivityContent())
          .createdAt(entity.getCreatedAt())
          .type(entity.getType())
          .viewCount(entity.getViewCount())
          .filePath(entity.getFileEntity().getFilePath())
          .fileOriginalName(entity.getFileEntity().getFileOriginalName())
          .nickname(entity.getUserEntity().getNickname())
          .groupId(request.getGroupId())
          .build();

      resultList.add(getListResponse);
    }

    for(ActivityBoardEntity entity: activityBoardList) {
      GetListResponse getListResponse = GetListResponse.builder()
          .activityId(entity.getActivityId())
          .activityTitle(entity.getActivityTitle())
          .activityContent(entity.getActivityContent())
          .createdAt(entity.getCreatedAt())
          .type(entity.getType())
          .viewCount(entity.getViewCount())
          .filePath(entity.getFileEntity().getFilePath())
          .fileOriginalName(entity.getFileEntity().getFileOriginalName())
          .nickname(entity.getUserEntity().getNickname())
          .groupId(request.getGroupId())
          .build();

      resultList.add(getListResponse);
    }

    return resultList;
  }

  /**
   * 활동 게시물 조회(가나다 순)
   */
  public List<GetListResponse> getTitleList(GetListRequest request, String token) {

    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    boolean flag = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(request.getGroupId(),
        user.getUserId(), ACCEPT.getValue());

    // 모임에 가입된 상태가 아닐시
    if(!flag) {
      throw new NoAcceptStatusException();
    }

    List<ActivityBoardEntity> noticeList = this.activityBoardRepository.findByType(NOTICE.getValue());

    PageRequest pageRequest = PageRequest.of(request.getPage(), 10, Sort.by("activityTitle").ascending());

    Page<ActivityBoardEntity> activityBoardList = this.activityBoardRepository.findAll(pageRequest);

    List<GetListResponse> resultList = new ArrayList<>();

    for(ActivityBoardEntity entity : noticeList) {
      GetListResponse getListResponse = GetListResponse.builder()
          .activityId(entity.getActivityId())
          .activityTitle(entity.getActivityTitle())
          .activityContent(entity.getActivityContent())
          .createdAt(entity.getCreatedAt())
          .type(entity.getType())
          .viewCount(entity.getViewCount())
          .filePath(entity.getFileEntity().getFilePath())
          .fileOriginalName(entity.getFileEntity().getFileOriginalName())
          .nickname(entity.getUserEntity().getNickname())
          .groupId(request.getGroupId())
          .build();

      resultList.add(getListResponse);
    }

    for(ActivityBoardEntity entity: activityBoardList) {
      GetListResponse getListResponse = GetListResponse.builder()
          .activityId(entity.getActivityId())
          .activityTitle(entity.getActivityTitle())
          .activityContent(entity.getActivityContent())
          .createdAt(entity.getCreatedAt())
          .type(entity.getType())
          .viewCount(entity.getViewCount())
          .filePath(entity.getFileEntity().getFilePath())
          .fileOriginalName(entity.getFileEntity().getFileOriginalName())
          .nickname(entity.getUserEntity().getNickname())
          .groupId(request.getGroupId())
          .build();

      resultList.add(getListResponse);
    }

    return resultList;
  }

  /**
   * 활동 게시물 조회(조회수 순)
   */
  public List<GetListResponse> getViewList(GetListRequest request, String token) {

    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    boolean flag = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(request.getGroupId(),
        user.getUserId(), ACCEPT.getValue());

    // 모임에 가입된 상태가 아닐시
    if(!flag) {
      throw new NoAcceptStatusException();
    }

    List<ActivityBoardEntity> noticeList = this.activityBoardRepository.findByType(NOTICE.getValue());

    PageRequest pageRequest = PageRequest.of(request.getPage(), 10, Sort.by("viewCount").descending());

    Page<ActivityBoardEntity> activityBoardList = this.activityBoardRepository.findAll(pageRequest);

    List<GetListResponse> resultList = new ArrayList<>();
    for(ActivityBoardEntity entity : noticeList) {
      GetListResponse getListResponse = GetListResponse.builder()
          .activityId(entity.getActivityId())
          .activityTitle(entity.getActivityTitle())
          .activityContent(entity.getActivityContent())
          .createdAt(entity.getCreatedAt())
          .type(entity.getType())
          .viewCount(entity.getViewCount())
          .filePath(entity.getFileEntity().getFilePath())
          .fileOriginalName(entity.getFileEntity().getFileOriginalName())
          .nickname(entity.getUserEntity().getNickname())
          .groupId(request.getGroupId())
          .build();

      resultList.add(getListResponse);
    }

    for(ActivityBoardEntity entity: activityBoardList) {
      GetListResponse getListResponse = GetListResponse.builder()
          .activityId(entity.getActivityId())
          .activityTitle(entity.getActivityTitle())
          .activityContent(entity.getActivityContent())
          .createdAt(entity.getCreatedAt())
          .type(entity.getType())
          .viewCount(entity.getViewCount())
          .filePath(entity.getFileEntity().getFilePath())
          .fileOriginalName(entity.getFileEntity().getFileOriginalName())
          .nickname(entity.getUserEntity().getNickname())
          .groupId(request.getGroupId())
          .build();

      resultList.add(getListResponse);
    }

    return resultList;
  }

  /**
   * 활동 게시물 개별 조회
   */
  public GetDetailReponse getDetail(GetDetailRequest request, String token) {

    // vaildation
    var activity = this.activityBoardRepository.findById(request.getActivityId())
        .orElseThrow(NoActivityBoardException::new);

    var group = this.groupBoardRepository.findById(activity.getGroupBoardEntity().getGroupId())
        .orElseThrow(NoGroupException::new);

    Long viewCount = activity.getViewCount();

    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    boolean exists = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(group.getGroupId(),
        user.getUserId(), ACCEPT.getValue());

    // 모임에 가입된 상태가 아닐시
    if(!exists) {
      throw new NoAcceptStatusException();
    }

    boolean flag = this.activityBoardViewRepository.existsByActivityBoardEntityActivityIdAndUserEntityUserId(activity.getActivityId(), user.getUserId());

    // 기존에 조회 테이블에 없으면 증가
    if(!flag) {
      ActivityBoardViewEntity entity = ActivityBoardViewEntity.builder()
          .activityBoardEntity(activity)
          .userEntity(user)
          .createAt(LocalDateTime.now())
          .build();

      this.activityBoardViewRepository.save(entity);

      viewCount++;

      activity.setViewCount(viewCount);

      this.groupBoardRepository.save(group);
    }

    return GetDetailReponse.builder()
        .activityId(activity.getActivityId())
        .activityTitle(activity.getActivityTitle())
        .activityContent(activity.getActivityContent())
        .createdAt(activity.getCreatedAt())
        .type(activity.getType())
        .viewCount(viewCount)
        .filePath(activity.getFileEntity().getFilePath())
        .fileOriginalName(activity.getFileEntity().getFileOriginalName())
        .nickname(activity.getUserEntity().getNickname())
        .groupId(group.getGroupId())
        .build();

  }

  /**
   * 활동 게시물 수정
   */
  public UpdateResponse update(UpdateRequest request, MultipartFile file, String token) throws Exception {

    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var activity = this.activityBoardRepository.findById(request.getActivityId())
        .orElseThrow(NoActivityBoardException::new);

    var group = this.groupBoardRepository.findById(activity.getGroupBoardEntity().getGroupId())
        .orElseThrow(NoGroupException::new);

    FileEntity fileEntity = this.fileRepository.findById(activity.getFileEntity().getFileId())
        .orElseThrow(NoFileException::new);

    // 자기 자신이 쓴 글이 아닐때
    if(!Objects.equals(user.getUserId(), activity.getUserEntity().getUserId())) {
      throw new NoYourSelfException();
    }

    boolean exists = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(group.getGroupId(),
        user.getUserId(), ACCEPT.getValue());

    // 모임에 가입된 상태가 아닐시
    if(!exists) {
      throw new NoAcceptStatusException();
    }

    // 사진을 수정할 경우
    if(!file.isEmpty()) {

      // 기존의 파일 삭제
      String filePath = activity.getFileEntity().getFilePath() + "\\" + activity.getFileEntity().getUuid() + "_" + activity.getFileEntity().getFileOriginalName();
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
          .fileId(activity.getFileEntity().getFileId())
          .filePath(projectPath)
          .uuid(uuid.toString())
          .fileOriginalName(file.getOriginalFilename())
          .fileSize(file.getSize())
          .build();

      this.fileRepository.save(fileEntity);
    }

    ActivityBoardEntity activityBoardEntity = ActivityBoardEntity.builder()
        .activityId(activity.getActivityId())
        .activityTitle(request.getActivityTitle())
        .activityContent(request.getActivityContent())
        .type(request.getType())
        .createdAt(activity.getCreatedAt())
        .updatedAt(LocalDateTime.now())
        .viewCount(activity.getViewCount())
        .fileEntity(fileEntity)
        .groupBoardEntity(group)
        .userEntity(user)
        .build();

    this.activityBoardRepository.save(activityBoardEntity);

    return UpdateResponse.builder()
        .activityTitle(activityBoardEntity.getActivityTitle())
        .activityContent(activityBoardEntity.getActivityContent())
        .type(activityBoardEntity.getType())
        .updatedAt(activityBoardEntity.getUpdatedAt())
        .viewCount(activityBoardEntity.getViewCount())
        .filePath(activityBoardEntity.getFileEntity().getFilePath())
        .fileOriginalName(activityBoardEntity.getFileEntity().getFileOriginalName())
        .groupId(activityBoardEntity.getGroupBoardEntity().getGroupId())
        .build();
  }

  /**
   * 활동 게시물 삭제
   */
  public DeleteResponse delete(DeleteRequest request, String token) {

    // validation
    String email = this.tokenProvider.parseClaims(token.substring(7)).getSubject();

    var user = this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);

    var activity = this.activityBoardRepository.findById(request.getActivityId())
        .orElseThrow(NoActivityBoardException::new);

    var group = this.groupBoardRepository.findById(activity.getGroupBoardEntity().getGroupId())
        .orElseThrow(NoGroupException::new);

    List<String> roles = user.getRoles();

    boolean userFlag = false;

    for(String role : roles) {
      if(role.equals(Roles.USER.getValue())) {
        userFlag = true;
      }
    }

    // 일반 유저가 삭제할 때
    if(userFlag) {

      // 자기 자신이 쓴 글만 삭제 가능
      if(!Objects.equals(user.getUserId(), activity.getUserEntity().getUserId())) {
        throw new NoAuthException();
      }

      boolean exists = this.applyGroupRepository.existsByGroupBoardEntityGroupIdAndUserEntityUserIdAndStatus(group.getGroupId(),
          user.getUserId(), ACCEPT.getValue());

      // 모임에 가입된 상태가 아닐시
      if(!exists) {
        throw new NoAcceptStatusException();
      }

    }

    this.commentRepository.deleteByActivityBoardEntityActivityId(activity.getActivityId());

    this.activityBoardViewRepository.deleteByActivityBoardEntityActivityId(activity.getActivityId());

    this.activityBoardRepository.deleteById(activity.getActivityId());

    this.fileRepository.deleteById(activity.getFileEntity().getFileId());

    // 기존의 파일 삭제
    String filePath = activity.getFileEntity().getFilePath() + "\\" + activity.getFileEntity().getUuid() + "_" + activity.getFileEntity().getFileOriginalName();
    File originalFile = new File(filePath);

    if (originalFile.delete()) {
      log.info("File deleted successfully");
    } else {
      log.info("Failed to delete the file");
    }

    return DeleteResponse.builder()
        .activityTitle(activity.getActivityTitle())
        .build();
  }


}
