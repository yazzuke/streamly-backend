package co.streamly.streamly_backend.config;

import co.streamly.streamly_backend.domain.User.User;
import co.streamly.streamly_backend.service.FirebaseUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.google.firebase.auth.FirebaseToken;

import java.io.IOException;
import java.util.List;

public class FirebaseTokenFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDED_PATHS = List.of("/streamly/combos", "/streamly/accounts", "/streamly/allproducts", "/streamly/users/register");
    private final FirebaseUserService firebaseUserService;

    public FirebaseTokenFilter(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    
        String authorizationHeader = request.getHeader("Authorization");
    
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
    
                // Sincroniza el usuario con la base de datos y obtén el rol
                User user = firebaseUserService.synchronizeUser(token);
                String role = user.getRole().name(); 
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        decodedToken.getUid(), null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
    
                // Agrega este log para verificar que el token y el rol se han configurado
                System.out.println("Autenticado usuario con UID: " + decodedToken.getUid() + " y rol: " + role);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                System.out.println("Error autenticando el token de Firebase: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró el token de autenticación en la solicitud.");
        }
    
        chain.doFilter(request, response);
    }

    }