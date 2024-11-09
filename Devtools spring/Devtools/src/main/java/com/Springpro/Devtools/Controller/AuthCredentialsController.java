package com.Springpro.Devtools.Controller;
import com.Springpro.Devtools.Entity.AuthCredentials;
import com.Springpro.Devtools.Service.AuthCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthCredentialsController {
    @Autowired
    private AuthCredentialsService authCredentialsService;

    @PostMapping("/login")
    public String login(@RequestBody AuthCredentials authCredentials) {
        System.out.println(authCredentials);
        return authCredentialsService.validateLogin(authCredentials.getUsername(), authCredentials.getPassword(), authCredentials.getRole());
    }

    @GetMapping("/getLoginId/{username}")
    public int getLoginId(@PathVariable String username) {
        int loginId = authCredentialsService.getLoginId(username);
        System.out.println(loginId);
        return loginId;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AuthCredentials credentials) {
        try {
            if (authCredentialsService.existsByUsername(credentials.getUsername())) {
                return ResponseEntity.badRequest().body("Username already exists");
            }
            authCredentialsService.saveUserCredentials(credentials);
            return ResponseEntity.ok("Signup successful!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Signup failed");
        }
    }
}
