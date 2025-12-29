package de.nikolasklein.backend.controller; // Passe den Paketnamen an

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/public") // Wir nutzen "public", damit wir noch keinen Login brauchen
public class TestController {

    @GetMapping("/hello")
    public Map<String, String> sayHello() {
        return Map.of("message", "Hallo vom Spring Boot Backend (Java 25)! 👋");
    }
}