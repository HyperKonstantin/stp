package vlx.stp.backend.controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vlx.stp.backend.entities.AboutResponse;

import java.io.File;
import java.util.Map;

@Slf4j
@RestController
public class AboutController {

    @Autowired
    public ObjectMapper objectMapper;

    @GetMapping("/about")
    public ResponseEntity<?> about(){
        return new ResponseEntity<>(getAboutInfo(), HttpStatus.OK);
    }

    @SneakyThrows
    private AboutResponse getAboutInfo(){

        Map<String, String> map = objectMapper.readValue(new File("target/classes/git.json"),
                new TypeReference<Map<String,String>>(){});

        log.info(map.toString());

        return AboutResponse.builder()
                .version(map.get("git.build.version"))
                .branch(map.get("git.branch"))
                .commit(map.get("git.commit.id.abbrev"))
                .commitUser(map.get("git.commit.user.name"))
                .build_time(map.get("git.build.time"))
                .build();
    }
}
