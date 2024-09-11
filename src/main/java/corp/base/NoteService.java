package corp.base;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {
    private final List<Note> notes = new ArrayList<>();

    public NoteService() {
        notes.add(new Note(1, "Note 1", "Java Core + Java Dev"));
        notes.add(new Note(2, "Note 2", "SQL + JDBC"));
        notes.add(new Note(3, "Note 3", "Servlets + Hibernate + Flyway"));
        notes.add(new Note(4, "Note 4", "Spring Boot + GraphQL"));
        notes.add(new Note(5, "Note 5", "Kotlin + Android API"));
    }

    List<Note> listAll() {
        return new ArrayList<>(notes);
    }

    Note getById(long id) {
        return notes.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Note with id " + id + " not found"));
    }

    Note add(Note note) {
        notes.add(note);
        return note;
    }

    void update(Note note) {
        notes.stream()
                .filter(n -> n.getId() == note.getId())
                .findFirst()
                .ifPresentOrElse(
                        n -> {
                            n.setId(note.getId());
                            n.setTitle(note.getTitle());
                            n.setContent(note.getContent());
                        },
                        () -> {
                            throw new IllegalArgumentException("Note with id " + note.getId() + " not found");
                        }
                );
    }

    void deleteById(long id) {
        boolean isRemoved = notes.removeIf(note -> note.getId() == id);
        if (!isRemoved) {
            throw new IllegalArgumentException("Note with id " + id + " not found");
        }
    }
}
