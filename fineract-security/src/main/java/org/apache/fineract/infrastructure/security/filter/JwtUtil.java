package org.apache.fineract.infrastructure.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.fineract.useradministration.domain.AppUser;
import org.apache.fineract.useradministration.domain.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry}")
    private long expiry;

    public SecretKey key(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(AppUser appUser){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", appUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(appUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiry))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(final String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(final String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String extractUserName(final String token){
        return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(final String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public Set<String> extractRoles(final String token){
        return new HashSet<>(
                extractClaims(token).get("roles", List.class)
        );
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(UserDetails userDetails, String token){
        final String user = extractUserName(token);
        return user.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }



}
