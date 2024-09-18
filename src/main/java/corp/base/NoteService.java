package corp.base;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public long findCount() {
        return noteRepository.count();
    }

    public Note getById(String id) {
        return noteRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new NoSuchElementException("Note not found with id: " + id));
    }

    public void save(Note note) {
        noteRepository.save(note);
    }

    public boolean exists(String id) {
        if (id == null) {
            return false;
        }

        return noteRepository.existsById(Long.valueOf(id));
    }

    public List<Note> searchTitle(String query) {
        return noteRepository.searchTitle("%" + query + "%");
    }

    public List<Note> searchTitleAndContent(String query) {
        return noteRepository.searchTitleAndContent("%" + query + "%");
    }

    public void deleteById(String id) {
        noteRepository.deleteById(Long.valueOf(id));
    }


    @Transactional(rollbackOn = {NullPointerException.class}, dontRollbackOn = {IOException.class})
    public void deleteAllByTitle(String title) {
        noteRepository.deleteAllByTitle("%" + title + "%");
    }
}
