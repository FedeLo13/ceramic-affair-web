package es.uca.tfg.ceramic_affair_web.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DummyController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello from public!";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "Admin dashboard";
    }

    @GetMapping("/protected/resource")
    public String protectedResource() {
        return "Protected content";
    }
}
