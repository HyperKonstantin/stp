package vlx.stp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vlx.stp.backend.dto.NoteRequestDto;
import vlx.stp.backend.entities.Note;
import vlx.stp.backend.repositories.NoteRepository;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getNotes(){
        return noteRepository.findAll();
    }

    public void createNote(NoteRequestDto noteRequestDto) {
        Note note = new Note(noteRequestDto.content());
        noteRepository.save(note);
    }
}
