package org.interkambio.SistemaInventarioBackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.security.CustomUserPrincipal;
import org.interkambio.SistemaInventarioBackend.security.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token != null && jwtProvider.validateToken(token)) {
            // Extraemos información del token
            Long userId = jwtProvider.getUserIdFromToken(token);
            String username = jwtProvider.getUsernameFromToken(token);
            String role = jwtProvider.getRoleFromToken(token); // solo un rol por ahora

            // Convertimos el rol a GrantedAuthority
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

            // Creamos el principal con userId, username y roles
            CustomUserPrincipal principal = new CustomUserPrincipal(userId, username, authorities);

            // Creamos Authentication y lo ponemos en el contexto de Spring Security
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
