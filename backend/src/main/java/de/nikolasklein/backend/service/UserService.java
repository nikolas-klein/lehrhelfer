package de.nikolasklein.backend.service;

import de.nikolasklein.backend.model.User;
import de.nikolasklein.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // WICHTIG: Verschlüsseln!
        user.setEmail(email);
        user.setRoles(Set.of("ROLE_ADMIN"));
        user.setRoles(java.util.Set.of("ROLE_ADMIN"));
        userRepository.save(user);
    }
}