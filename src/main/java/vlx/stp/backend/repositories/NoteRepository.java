package vlx.stp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vlx.stp.backend.entities.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
