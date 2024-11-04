package co.streamly.streamly_backend.config;

import co.streamly.streamly_backend.domain.User.User;
import co.streamly.streamly_backend.service.FirebaseUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.checkerframework.checker.units.qual.s;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.google.firebase.auth.FirebaseToken;

import java.io.IOException;
import java.util.List;

public class FirebaseTokenFilter extends OncePerRequestFilter {
    
    private final FirebaseUserService firebaseUserService;

    public FirebaseTokenFilter(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }
    
        String authorizationHeader = request.getHeader("Authorization");
    
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            System.out.println("Token recibido en el filtro: " + token);
    
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                System.out.println("Token decodificado exitosamente: UID=" + decodedToken.getUid());
    
                User user = firebaseUserService.synchronizeUser(token);
                System.out.println("Usuario sincronizado: " + user);
                String role = "ROLE_" + user.getRole();
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
    
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        decodedToken.getUid(), null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    
                // Establece la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
    
                System.out.println("Usuario autenticado exitosamente: UID=" + decodedToken.getUid());
                System.out.println("Rol del usuario: " + role);
                System.out.println("Autenticación actual en contexto: " + SecurityContextHolder.getContext().getAuthentication());
    
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        } else {
            System.out.println("No se encontró el token de autenticación en la solicitud.");
        }
    
        chain.doFilter(request, response);
    }

}