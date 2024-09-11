package corp.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import java.util.List;

@SpringBootTest
public class NoteServiceTests {
    private NoteService noteService;

    @BeforeEach
    void start() {
        noteService = new NoteService();
    }

    @Test
    void testAddNote() {
        Note note = new Note();
        note.setId(1);
        note.setTitle("Test Title");
        note.setContent("Test Content");

        Note newNote = noteService.add(note);

        Assertions.assertNotNull(newNote);
        Assertions.assertEquals(1, newNote.getId());
        Assertions.assertEquals("Test Title", newNote.getTitle());
        Assertions.assertEquals("Test Content", newNote.getContent());
    }

    @Test
    void testListAllNotes() {
        Note note1 = new Note();
        note1.setId(1);
        note1.setTitle("First Note");
        note1.setContent("Content of the first note");

        Note note2 = new Note();
        note2.setId(2);
        note2.setTitle("Second Note");
        note2.setContent("Content of the second note");

        noteService.add(note1);
        noteService.add(note2);

        List<Note> notes = noteService.listAll();
        Assertions.assertEquals(2, notes.size());
    }

    @Test
    void testGetById() {
        Note note = new Note();
        note.setId(1);
        note.setTitle("Test Note");
        note.setContent("Test Content");

        noteService.add(note);

        Note foundNote = noteService.getById(1);
        Assertions.assertNotNull(foundNote);
        Assertions.assertEquals("Test Note", foundNote.getTitle());
    }

    @Test
    void testUpdateNote() {
        Note note = new Note();
        note.setId(1);
        note.setTitle("Initial Title");
        note.setContent("Initial Content");

        noteService.add(note);

        Note updatedNote = new Note();
        updatedNote.setId(1);
        updatedNote.setTitle("Updated Title");
        updatedNote.setContent("Updated Content");

        noteService.update(updatedNote);

        Note foundNote = noteService.getById(1);
        Assertions.assertEquals("Updated Title", foundNote.getTitle());
        Assertions.assertEquals("Updated Content", foundNote.getContent());
    }

    @Test
    void testDeleteNoteById() {
        Note note = new Note();
        note.setId(1);
        note.setTitle("Test Note");
        note.setContent("Test Content");

        noteService.add(note);

        noteService.deleteById(1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> noteService.getById(1));
    }
}

