package com.zerobase.hobbyGroup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "activity_board_view")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityBoardViewEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "activity_board_view_id")
  private Long activityBoardViewId;

  @ManyToOne
  @JoinColumn(name = "activity_id")
  private ActivityBoardEntity activityBoardEntity;

  @ManyToOne
  @JoinColumn(name ="user_id")
  private UserEntity userEntity;

  @Column(name = "created_at")
  private LocalDateTime createAt;

}
