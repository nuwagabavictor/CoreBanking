//package org.apache.fineract.infrastructure.security.jwt;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.apache.fineract.infrastructure.security.service.TenantAwareJpaPlatformUserDetailsService;
//import org.junit.jupiter.api.Order;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final TenantAwareJpaPlatformUserDetailsService userDetailsService;
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse
//            response, FilterChain filterChain) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")){
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String token = authHeader.substring(7).trim();
//        try{
//
//            if (token !=null ){
//                String username = jwtUtil.extractUserName(token);
//
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                    Set<String> roles = jwtUtil.extractRoles(token);
//                    List<GrantedAuthority> authorities = roles.stream()
//                            .map(SimpleGrantedAuthority::new)
//                            .collect(Collectors.toList());
//
//                    if (jwtUtil.validateToken(userDetails, token)){
//                        UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(
//                                userDetails, null, authorities
//                        );
//                        token1.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContextHolder.getContext().setAuthentication(token1);
//                    }
//
//                }
//            }
//        } catch (Exception e){
//            SecurityContextHolder.clearContext();
//        }
//        filterChain.doFilter(request, response);
//    }
//}
