package re.edu.security.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import re.edu.exception.JwtExceptionCustom;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String jwtSecret;
    @Value("${jwt.expired}")
    private long expiredTime;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    //    sinh jwt
    public String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
//                .setPayload(userDetails)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiredTime))
                .signWith(getKey())
                .compact();
    }
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
//                .setPayload(userDetails)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiredTime*96*7))
                .signWith(getKey())
                .compact();
    }

    //    xác minh token hợp lệ
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);

        } catch (ExpiredJwtException e) {
            throw new JwtExceptionCustom("Token hết hạn");
        } catch (UnsupportedJwtException e) {
            throw new JwtExceptionCustom("Token không được hỗ trợ");
        } catch (MalformedJwtException e) {
            throw new JwtExceptionCustom("Token không đúng định dạng");
        } catch (SignatureException e) {
            throw new JwtExceptionCustom("Token không hợp lệ");
        } catch (IllegalArgumentException e) {
            throw new JwtExceptionCustom("Token rỗng hoặc null");
        }
    }

    //    giải mã token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
