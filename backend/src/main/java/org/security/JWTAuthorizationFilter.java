package org.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    public static final byte[] JWT_SECRET = "FaC2PTazv3whh$JmxwNrNnXly75jEMwRX1vLg^EDWC8&c1E7pl*FOs#Ljil&QU$Fafvb#sS!o0CF1V*y@enQhb$n!$7%jKYapnVg".getBytes(StandardCharsets.UTF_8);
    public static final String JWT_ROLES = "authorities";

    private static final String HEADER_PREFIX = "Bearer ";

    public static SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            Optional<Authentication> authentication = Optional.ofNullable(request.getHeader("Authorization"))
                    .filter(header -> header.startsWith(HEADER_PREFIX))
                    .map(header -> header.replace(HEADER_PREFIX, ""))
                    .map(this::extractClaims)
                    .filter(claims -> claims.get(JWT_ROLES) != null)
                    .map(this::createAuthentication);

            if (authentication.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(authentication.get());
            } else {
                SecurityContextHolder.clearContext();
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT token");
        }
    }

    private Claims extractClaims(String jwtToken) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(jwtToken).getPayload();
    }

    private Authentication createAuthentication(Claims claims) {
        Collection<String> roles = (Collection<String>) claims.get(JWT_ROLES, Collection.class);
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

    }

}