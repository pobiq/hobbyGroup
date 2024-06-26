package com.zerobase.hobbyGroup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "group_board")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupBoardEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_id")
  private Long groupId;

  @Column(name = "group_title")
  private String groupTitle;

  @Column(name = "head_count")
  private Long headCount;

  @Column(name = "group_content")
  private String groupContent;

  @Column(name = "start_at")
  private LocalDateTime startAt;

  @Column(name = "end_at")
  private LocalDateTime endAt;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name ="view_count")
  private Long viewCount;

  @OneToOne
  @JoinColumn(name="file_id")
  private FileEntity fileEntity;

  @OneToOne
  @JoinColumn(name ="category_id")
  private CategoryEntity categoryEntity;

  @ManyToOne
  @JoinColumn(name ="user_id")
  private UserEntity userEntity;

}
