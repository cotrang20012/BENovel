package mobile.security.JWT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import mobile.security.DTO.AppUserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${apps.security.secret}")
    private String jwtSecret;

    @Value("${apps.security.header-string}")
    private String headerString;
    @Value("${apps.security.token-prefix}")
    private String tokenPrefix;
    @Value("${apps.security.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${apps.security.header-string}")
    private String authorizationHeader;
    @Value("${apps.security.refreshJwtExpirationMs}")
    private int refreshJwtExpirationMs;
    public String generateJwtToken(AppUserDetail userPrincipal) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        String access_token = JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ jwtExpirationMs))
                .withClaim("roleNames",userPrincipal.getRoles().stream().collect(Collectors.toList()))
                .withClaim("rolePermissions",userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return access_token;
    }

    public String generateRefreshJwtToken(AppUserDetail userPrincipal) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        String refresh_token = JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ refreshJwtExpirationMs))
                .sign(algorithm);
        return refresh_token;
    }

    public String getUserNameFromJwtToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return  jwt.getSubject();
    }

    public Collection<SimpleGrantedAuthority> getAuthoritiesFromJwtToken(String token) {
        String tempRolesString =Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody().get("rolePermissions").toString();
        String[] roles =tempRolesString.substring(1,tempRolesString.length()-1).replaceAll(" ","").split(",");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (String role: roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }


    public String generateEmailJwtToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        String email_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis()+ 600000))
                .sign(algorithm);
        return email_token;
    }

    public boolean validateJwtToken(String authToken) throws ExpiredJwtException {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(authToken.replace(tokenPrefix, ""))
                    .getBody()
                    .getSubject();
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public boolean validateExpiredToken(String authToken) {
        DecodedJWT jwt = JWT.decode(authToken);
        return  jwt.getExpiresAt().before(new Date());
    }
}