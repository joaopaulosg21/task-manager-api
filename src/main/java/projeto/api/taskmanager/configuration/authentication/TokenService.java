package projeto.api.taskmanager.configuration.authentication;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import projeto.api.taskmanager.user.dtos.UserDTO;

@Service
public class TokenService {
    
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.exp}")
    private String exp;

    public String generate(Authentication authentication) {
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        Date expiration = new Date(Long.parseLong(exp) + new Date().getTime());

        return Jwts.builder()
                    .setIssuedAt(new Date())
                    .setSubject(Long.toString(userDTO.id()))
                    .setExpiration(expiration)
                    .signWith(Keys.hmacShaKeyFor(secret.getBytes()),SignatureAlgorithm.HS256)
                    .compact();

    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public long getIdFromToken(String token) {
        Claims body = Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token).getBody();

        return Long.parseLong(body.getSubject());
    }
}
