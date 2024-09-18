package corp.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("from Note n where lower(n.title) like lower(:query)")
    List<Note> searchTitle(@Param("query") String query);

    @Query(nativeQuery = true, value =
            "SELECT id, title, content\n" +
                    "FROM note\n" +
                    "WHERE lower(title) LIKE lower(:query) OR lower(content) LIKE lower(:query)")
    List<Note> searchTitleAndContent(@Param("query") String query);

    @Modifying
    @Query("DELETE FROM Note n WHERE lower(n.title) LIKE lower(:title)")
    void deleteAllByTitle(@Param("title") String title);
}
