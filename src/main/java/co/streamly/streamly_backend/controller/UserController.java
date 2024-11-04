package co.streamly.streamly_backend.controller;

import co.streamly.streamly_backend.domain.User.User;
import co.streamly.streamly_backend.dto.ManualUserDTO;
import co.streamly.streamly_backend.service.FirebaseUserService;

import java.util.Map;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import java.util.Optional;

@RestController
@RequestMapping("/streamly/users")
public class UserController {

    private final FirebaseUserService firebaseUserService;

    public UserController(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;
    }

    // Endpoint para obtener el usuario por ID en la base de datos MySQL
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = firebaseUserService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build()); // Cambia la respuesta según el estado
    }
    

    // Endpoint para registrar o sincronizar un usuario usando el token de Firebase
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestHeader("Authorization") String token) {
        String firebaseUid;
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token.substring(7));
            firebaseUid = decodedToken.getUid();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    
        User existingUser = firebaseUserService.findByFirebaseUid(firebaseUid);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya está registrado. Use /login.");
        }
    
        User newUser = firebaseUserService.synchronizeUser(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // Endpoint para registro manual usando Firebase Authentication
    @PostMapping("/registermanual")
    public ResponseEntity<User> registerUserManually(@RequestBody ManualUserDTO manualUserDTO) {
        User user = firebaseUserService.registerUserInFirebase(manualUserDTO);
        return ResponseEntity.ok(user);

    }

    // Endpoint para obtener el rol del usuario
    @GetMapping("/role")
    public ResponseEntity<Map<String, String>> getUserRole(@RequestHeader("Authorization") String token) {
        User user = firebaseUserService.synchronizeUser(token); // Ya existente
        Map<String, String> response = new HashMap<>();
        response.put("role", user.getRole().toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Encabezado de autorización inválido o ausente.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token de autorización inválido o no presente");
        }

        String token = authorizationHeader.substring(7); // Elimina "Bearer "
        System.out.println("Encabezado de autorización recibido: " + authorizationHeader);
        System.out.println("Token extraído: " + token);

        // Llamada de prueba al servicio Firebase para validar el token
        String result = firebaseUserService.testValidateToken(token);
        return ResponseEntity.ok(result);
    }

    // Endpoint para solo autenticar al usuario y devolver su rol sin sincronizar en
    // la base de datos
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestHeader("Authorization") String token) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token.substring(7));
            String firebaseUid = decodedToken.getUid();
    
            User existingUser = firebaseUserService.findByFirebaseUid(firebaseUid);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no registrado");
            }
    
            Map<String, String> response = new HashMap<>();
            response.put("role", existingUser.getRole().toString());
            response.put("uid", firebaseUid);
            return ResponseEntity.ok(response);
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

}