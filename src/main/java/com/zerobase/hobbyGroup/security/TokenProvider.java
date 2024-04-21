package com.zerobase.hobbyGroup.security;

import com.zerobase.hobbyGroup.service.RedisService;
import com.zerobase.hobbyGroup.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {

  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1 hour
  private static final String KEY_ROLES = "roles";

  private final UserService userService;

  private final RedisService redisService;

  @Value("{spring.jwt.secretKey}")
  private String secretKey;

  /**
   * 토큰 생성(발급)
   *
   * @param email
   * @param roles
   * @return
   */
  public String generateToken(String email, List<String> roles) {
    // 다음 정보들을 포함한 claims 생성
    Claims claims = Jwts.claims().setSubject(email);
    //      - email
    //      - roles
    claims.put(KEY_ROLES, roles);
    //      - 생성 시간
    var now = new Date();
    //      - 만료 시간
    var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);
    //      - signature
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS512, this.secretKey)
        .compact();

    // jwt 발급
    return token;
  }

  // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
  public Authentication getAuthentication(String jwt) {
    UserDetails userDetails = this.userService.loadUserByUsername(this.getUsername(jwt));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return this.parseClaims(token).getSubject();
  }

  // 토큰의 유효성 검증을 수행
  public boolean validateToken(String token) {
      if (!StringUtils.hasText(token)) {
          return false;
      }

    var claims = this.parseClaims(token);
    return !claims.getExpiration().before(new Date());
  }

  public ResponseEntity<?> validToken(String token) {
    if (token == null || !token.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 토큰입니다.");
    }

    String jwtToken = token.substring(7); // "접두어 제거"

    if (this.isTokenInvalid(jwtToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 로그아웃된 토큰입니다.");
    }

    // 토큰이 유효하면 null을 반환하여 성공을 나타냄
    return null;
  }

  /**
   * 토큰으로 유저 정보 가져오기
   *
   * @param token 토큰
   * @return Claims 객체
   */
  public Claims parseClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

    /**
     * 토큰 블랙리스트 추가
     * @param token 기존 발행한 토큰
     */
  public void invalidateToken(String token) {
      Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
      String key = claims.getSubject() + "Token";

      // redis에 토큰 추가 (블랙리스트)
      this.redisService.setValues(key, token);
  }

  public boolean isTokenInvalid(String token) {
    try {
      Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
      String key = claims.getSubject() + "Token";

      String tokenValue = this.redisService.getValues(key);

      return token.equals(tokenValue);
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

}
