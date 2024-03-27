# 취미 소모임 사이트

취미를 만들고 가입할수 있는 사이트를 만들고자 합니다.

## Setting
- Java 17
- Project : Gradle - Groovy
- Spring Boot 3.2.4

## 기술 Stack
- Spring Security
- Spring Data Jpa
- MariaDB
- Elastic Search
- swagger
- SSE (Server-Sent Event)
- Redis

## 프로젝트 기능 및 설계
- [회원 가입]
    - 항목 : 이메일(필수), 패스워드(필수), 이름(필수), 닉네임, 핸드폰 번호, 도로명 주소, 지번 주소
    - 아이디는 고유해야하고, 패스워드는 암호화 한 후 DB에 저장
    - 이메일은(네이버, 카카오, 구글) -> 인증시 무작위 일회성 번호(6자)를 Redis에 저장후 인증 실시

- [회원 수정]
    - 항목 : 패스워드(필수), 이름(필수), 닉네임, 핸드폰 번호, 도로명 주소, 지번 주소
    - 로그인 한 자신만이 수정할수 있음
 
- [회원 탈퇴]
    - 로그인 한 자신과 관리자가 탈퇴를 할수 있음

- [로그인]
    - 회원 가입시 사용한 이메일과 패스워드가 일치하면 성공

- [회원 모집 게시물 작성]
    - 항목 : 제목(필수), 인원수(필수), 내용(필수), 모집 시작 시간(필수), 모집 끝 시간(필수), 사진, 카테고리(필수)
    - 모집 기간이 끝났으면 회원 모집 불가능
    - 인원이 다 찼으면 회원 모집 불가능
    - 로그인한 회원은 회원 모집 게시물 작성 가능

- [회원 모집 목록 조회]
    - 로그인 하지 않아도 해당 기능 사용 가능
    - 카테고리 별로 필터링 후 최신순, 가나다순, 조회수 순으로 검색
    - 카테고리를 선택 안할시 모든 카테고리를 조회
    - Paging 처리 -> 1Page 당 10개 block, 1block 당 게시물 10개
    - elasticsearch 사용
 
- [회원 모집 게시물 수정]
    - 항목 : 제목(필수), 인원수(필수), 내용(필수), 모집 시작 시간(필수), 모집 끝 시간(필수), 사진, 카테고리(필수)
    - 글 작성자만 수정 가능

- [회원 모집 게시물 삭제]
    - 글 작성자 및 관리자만 삭제 가능
    - 삭제되면 모임에 가입한 회원 자동 탈퇴
 
 - [모임 신청]
     - 항목 : 내용(필수)
     - 모임장이 아닌 다른 사용자 들은 취미 모임에 신청할수 있음
     - 모집(시작~끝)기간이 맞을때만 모임 신청 가능
     - 인원이 다 차지 않은 모임만 신청 가능
     - 한 모임엔 한 번의 신청만 가능 -> 모임장이 거절시 다시 신청 가능
 
 - [모임 신청 조회]
     - 모임장은 받은 모임 신청을 최신순으로 조회 가능
 
 - [모임 신청 내용 수정]
     - 항목 : 내용(필수)
     - 모임장이 아직 수락하지 않은 경우 수정 가능
     - 자신이 한 모임 신청만 수정 가능

 - [모임 신청 철회]
     - 모임장이 아직 수락하지 않은 경우 철회 가능
     - 자신이 한 모임 신청만 철회 가능
  
 - [모임 신청 수락]
     - 모임장은 받은 모임 신청 내용을 보고 신청 수락
     - 인원이 다 차 있지 않을시에만 수락 가능

 - [취미 모임 찜 기능]
    - 사용자는 관심이 있는 취미 모임을 여러개 가입 가능
    - 편리성을 위해 관심이 있는 모임을 찜 할수 있음
  
 - [취미 모임 찜 철회]
    - 자신이 찜 한 모임을 철회 할수 있는 기능

 - [취미 모임 찜 목록 조회]
    - 카테고리 필터링 후 최신순으로 조회 가능
    - 카테고리 선택 안할시 모든 카테고리 조회
  
 - [모임 활동 게시물 작성]
    - 항목 : 제목(필수), 내용(필수), 사진, 공지 글 유무(필수)
    - 모임장은 공지게시물, 일반게시물 작성 가능 -> 공지글은 최대 2개 작성 가능
    - 일반회원은 일반게시물만 작성 가능

 - [모임 활동 게시물 조회]
    - 조회를 할시 공지글은 어떤 경우에도 보여야 함
    - 최신순, 조회수순으로 조회 가능
  
 - [모임 활동 게시물 수정]
    - 항목 : 제목(필수), 내용(필수), 사진, 공지 글 유무(필수)
    - 자신이 쓴 모임 게시물 만 수정 가능
  
 - [모임 활동 게시물 삭제]
    - 자신 및 관리자만이 게시물 삭제 가능
  
 - [모임 활동 게시물 댓글 및 대댓글 작성]
    - 항목 : 댓글 내용(필수)
    - 모임 활동 게시물에 회원들은 댓글 및 대댓글을 작성 가능
  
 - [모임 활동 게시물 댓글 및 대댓글 조회]
    - 부모1
      - 자식1
      - 자식2
          - 손자1
          - 손자2
      - 자식3
    - 부모2
      - 자식1
      - 자식2
    - 부모3
   
    - 위의 예시처럼 조회
  
 - [모임 활동 게시물 댓글 및 대댓글 수정]
    - 항목 : 댓글 내용(필수)
    - 모임 활동 게시물에 회원들은 댓글 및 대댓글을 수정 가능

 - [모임 활동 게시물 댓글 및 대댓글 삭제]
    - 자신 및 관리자만이 댓글 및 대댓글 삭제 가능

 

## ERD
![hobby](https://github.com/pobiq/hobbyGroup/assets/111417407/e94d8bd7-0cab-4bce-b94d-1725e7516a5b)


## 추가 기능 구현 예정 (우선순위가 낮은 것)
- [회원 모집 게시물 삭제]
    - 모임장 또는 관리자가 삭제할시 가입한 모든 회원에게 알림 발송

- [모임 신청]
    - 모임에 신청하면 모임장에게 알림이 가게 함

- [받은 알람 내역 조회]
    - 사용자는 받은 알람 내역을 조회할수 있음(SSE 사용)

- [모임장 위임 기능]
    - 모임장은 필요에 따라 장을 위임할수 있음
    - 위임할수 있는 대상은 같은 모임의 모임장이 아닌 회원
    - 위임할시 가입한 모든 회원에게 알림 발송

## Trouble Shooting
