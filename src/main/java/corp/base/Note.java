package corp.base;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "note")
@Entity
@Data
public class Note {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;
}
