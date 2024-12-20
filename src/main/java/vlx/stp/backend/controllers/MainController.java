package vlx.stp.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public ResponseEntity<?> homePage(){
        return new ResponseEntity<>("super home page", HttpStatus.OK);
    }
}
