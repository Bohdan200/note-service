package corp.base;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.NoSuchElementException;


import org.junit.jupiter.api.Assertions;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NoteServiceMockitoTest {
    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    private Note note1;
    private Note note2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        note1 = new Note();
        note1.setId(1);
        note1.setTitle("Test Title 1");
        note1.setContent("Test Content 1");

        note2 = new Note();
        note2.setId(2);
        note2.setTitle("Test Title 2");
        note2.setContent("Test Content 2");
    }

    @Test
    void findAll() {
        List<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);

        when(noteRepository.findAll()).thenReturn(notes);

        List<Note> result = noteService.findAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Test Title 1", result.getFirst().getTitle());
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void findCount() {
        when(noteRepository.count()).thenReturn(2L);

        long count = noteService.findCount();
        Assertions.assertEquals(2, count);

        verify(noteRepository, times(1)).count();
    }

    @Test
    void getByIdSuccess() {
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note1));

        Note result = noteService.getById("1");

        Assertions.assertEquals("Test Title 1", result.getTitle());
        verify(noteRepository, times(1)).findById(1L);
    }

    @Test
    void getByIdFailed() {
        when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> noteService.getById("9"));
        verify(noteRepository, times(1)).findById(9L);
    }

    @Test
    void save() {
        noteService.save(note1);
        verify(noteRepository, times(1)).save(note1);
    }

    @Test
    void existsNotWhenIdIsNull() {
        boolean result = noteService.exists(null);
        Assertions.assertFalse(result);
    }

    @Test
    void existsWhenFindNote() {
        when(noteRepository.existsById(anyLong())).thenReturn(true);

        boolean result = noteService.exists("1");

        Assertions.assertTrue(result);
        verify(noteRepository, times(1)).existsById(1L);
    }

    @Test
    void searchTitle() {
        List<Note> notes = new ArrayList<>();
        notes.add(note1);

        when(noteRepository.searchTitle(anyString())).thenReturn(notes);

        List<Note> result = noteService.searchTitle("Test");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Test Title 1", result.getFirst().getTitle());
        verify(noteRepository, times(1)).searchTitle("%Test%");
    }

    @Test
    void searchTitleAndContent() {
        List<Note> notes = new ArrayList<>();
        notes.add(note1);

        when(noteRepository.searchTitleAndContent(anyString())).thenReturn(notes);

        List<Note> result = noteService.searchTitleAndContent("Test");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Test Content 1", result.getFirst().getContent());
        verify(noteRepository, times(1)).searchTitleAndContent("%Test%");
    }

    @Test
    void deleteById() {
        noteService.deleteById("1");

        verify(noteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAllByTitle() {
        noteService.deleteAllByTitle("Test");

        verify(noteRepository, times(1)).deleteAllByTitle("%Test%");
    }
}
