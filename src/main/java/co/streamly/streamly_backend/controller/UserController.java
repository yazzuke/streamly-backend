package co.streamly.streamly_backend.controller;

import co.streamly.streamly_backend.domain.User.User;
import co.streamly.streamly_backend.dto.ManualUserDTO;
import co.streamly.streamly_backend.service.FirebaseUserService;

import java.util.Map;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para registrar o sincronizar un usuario usando el token de Firebase
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestHeader("Authorization") String token) {
        User user = firebaseUserService.synchronizeUser(token);
        return ResponseEntity.ok(user);
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

}