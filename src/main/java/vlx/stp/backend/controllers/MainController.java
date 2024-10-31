package vlx.stp.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public ResponseEntity<?> homePage(){
        return new ResponseEntity<>("Hello, this is page!", HttpStatus.OK);
    }

}
