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

@Entity(name = "activity_board")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityBoardEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "activity_id")
  private Long activityId;

  @Column(name = "activity_title")
  private String activityTitle;

  @Column(name = "activity_content")
  private String activityContent;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  private String type;

  @Column(name = "view_count")
  private Long viewCount;

  @OneToOne
  @JoinColumn(name="file_id")
  private FileEntity fileEntity;

  @ManyToOne
  @JoinColumn(name ="group_id")
  private GroupBoardEntity groupBoardEntity;

  @ManyToOne
  @JoinColumn(name ="user_id")
  private UserEntity userEntity;

}
