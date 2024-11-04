package co.streamly.streamly_backend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import co.streamly.streamly_backend.domain.User.Role;
import co.streamly.streamly_backend.domain.User.User;
import co.streamly.streamly_backend.domain.User.UserRepository;
import co.streamly.streamly_backend.dto.ManualUserDTO;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FirebaseUserService {

    private final UserRepository userRepository;

    public FirebaseUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Método para sincronizar el usuario de Firebase creado por google con MySQL
    public User synchronizeUser(String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken.substring(7)); // Elimina
                                                                                                         // "Bearer "
                                                                                                         // del token
            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String displayName = decodedToken.getName();
            String photoUrl = decodedToken.getPicture();

            return userRepository.findByFirebaseUid(firebaseUid).orElseGet(() -> {
                User newUser = new User();
                newUser.setFirebaseUid(firebaseUid);
                newUser.setEmail(email);
                newUser.setDisplayName(displayName);
                newUser.setPhotoUrl(photoUrl);
                newUser.setRole(Role.USER); // Por defecto, asigna el rol USER
                return userRepository.save(newUser);
            });

        } catch (Exception e) {
            throw new RuntimeException("Error en la autenticación con Firebase", e);
        }
    }

    // Método para registrar un usuario en Firebase MANUALEMENTE y sincronizarlo en
    // MySQL
    public User registerUserInFirebase(ManualUserDTO manualUserDTO) {
        try {
            // Crear usuario en Firebase usando el email y la contraseña
            CreateRequest request = new CreateRequest()
                    .setEmail(manualUserDTO.getEmail())
                    .setPassword(manualUserDTO.getPassword())
                    .setDisplayName(manualUserDTO.getDisplayName());

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

            // Guardar solo la información necesaria en MySQL (sin la contraseña)
            User user = new User();
            user.setFirebaseUid(userRecord.getUid());
            user.setEmail(manualUserDTO.getEmail());
            user.setDisplayName(manualUserDTO.getDisplayName());
            user.setRole(Role.USER); // Rol predeterminado

            return userRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear usuario en Firebase y sincronizar en la base de datos", e);
        }
    }

    // Método para buscar un usuario por su ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Método para buscar un usuario en MySQL por su Firebase UID
    public User findByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid).orElse(null);
    }

    // Método para guardar el usuario en MySQL
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public String testValidateToken(String idToken) {
        try {
            // Verificar el token sin ningún otro procesamiento
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            System.out.println("Token válido. UID del usuario: " + uid);
            return "Token válido. UID del usuario: " + uid;
        } catch (Exception e) {
            System.out.println("Error al validar el token: " + e.getMessage());
            return "Error al validar el token: " + e.getMessage();
        }
    }

}