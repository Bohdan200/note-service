package corp.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
@Rollback
class NoteServiceJUnitTest {
    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteRepository noteRepository;

    private Note note1;
    private Note note2;

    @BeforeEach
    void setUp() {
        noteRepository.deleteAll();

        note1 = new Note();
        note1.setTitle("Test Title 1");
        note1.setContent("Test Content 1");

        note2 = new Note();
        note2.setTitle("Test Title 2");
        note2.setContent("Test Content 2");

        noteService.save(note1);
        noteService.save(note2);
    }

    @Test
    void findAll() {
        List<Note> notes = noteService.findAll();

        Assertions.assertEquals(2, notes.size());
        Assertions.assertEquals("Test Title 1", notes.getFirst().getTitle());
    }

    @Test
    void findCount() {
        long count = noteService.findCount();
        Assertions.assertEquals(2, count);
    }

    @Test
    void getByIdSuccess() {
        Note foundNote = noteService.getById(String.valueOf(note1.getId()));

        Assertions.assertEquals("Test Title 1", foundNote.getTitle());
    }

    @Test
    void getByIdFailed() {
        Assertions.assertThrows(NoSuchElementException.class, () -> noteService.getById("9"));
    }

    @Test
    void save() {
        Note newNote = new Note();
        newNote.setTitle("New Test Title");
        newNote.setContent("New Test Content");

        noteService.save(newNote);

        Assertions.assertNotNull(noteService.getById(String.valueOf(newNote.getId())));
    }

    @Test
    void exists() {
        boolean exists = noteService.exists(String.valueOf(note1.getId()));
        Assertions.assertTrue(exists);

        boolean notExists = noteService.exists("9");
        Assertions.assertFalse(notExists);
    }

    @Test
    void searchTitle() {
        List<Note> result = noteService.searchTitle("Test Title 1");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Test Title 1", result.getFirst().getTitle());
    }

    @Test
    void searchTitleAndContent() {
        List<Note> result = noteService.searchTitleAndContent("Content 1");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Test Title 1", result.getFirst().getTitle());
    }

    @Test
    void deleteById() {
        noteService.deleteById(String.valueOf(note1.getId()));

        Assertions.assertThrows(NoSuchElementException.class, () -> noteService.getById(String.valueOf(note1.getId())));
    }

    @Test
    void deleteAllByTitle() {
        noteService.deleteAllByTitle("Test Title 1");

        List<Note> notes = noteService.findAll();
        Assertions.assertEquals(1, notes.size());
    }
}
