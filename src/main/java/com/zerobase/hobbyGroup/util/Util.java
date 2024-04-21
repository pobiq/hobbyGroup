package com.zerobase.hobbyGroup.util;


import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class Util {

  public ResponseEntity<?> createValidFile(MultipartFile file) {
    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
    }

    // 파일 크기 제한을 확인 (예: 10MB)
    if (file.getSize() > 10 * 1024 * 1024) {
      return ResponseEntity.badRequest().body("파일 크기가 너무 큽니다.");
    }

    // 허용된 파일 확장자를 확인
    String originalFilename = file.getOriginalFilename();
    String[] allowedExtensions = {"jpg", "jpeg", "png", "gif"};
    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
    if (!Arrays.asList(allowedExtensions).contains(fileExtension)) {
      return ResponseEntity.badRequest().body("허용되지 않은 파일 형식입니다.");
    }

    // vadliation 확인
    return null;
  }

}
