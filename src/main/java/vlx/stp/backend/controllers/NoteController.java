package vlx.stp.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vlx.stp.backend.dto.NoteRequestDto;
import vlx.stp.backend.services.NoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public ResponseEntity<?> getNotes(){
        return new ResponseEntity<>(noteService.getNotes(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> newNote(@RequestBody NoteRequestDto noteRequestDto){
        noteService.createNote(noteRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
