package corp.base;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RequestMapping("/note")
@Controller
public class NoteController {
    private final NoteService noteService;

    // GET  http://localhost:8080/note/list
    @GetMapping("/list")
    public ModelAndView getAll() {
        ModelAndView result = new ModelAndView("list");
        result.addObject("notes", noteService.findAll());
        result.addObject("totalNotes", noteService.findCount());
        return result;
    }

    // GET  http://localhost:8080/note/add
    @GetMapping("/add")
    public ModelAndView showAddNotePage() {
        return new ModelAndView("add-note");
    }

    // POST http://localhost:8080/note/add?title=someTitle&content=someContent
    @PostMapping("/add")
    public String addNote(@RequestParam("title") String title, @RequestParam("content") String content) {
        if (title == null || title.length() < 2) {
            return "redirect:/note/list?error=Title must contain at least 2 characters";
        }
        if (content == null || content.isEmpty()) {
            return "redirect:/note/list?error=No content";
        }

        Note newNote = new Note();
        newNote.setTitle(title);
        newNote.setContent(content);

        noteService.save(newNote);
        return "redirect:/note/list";
    }

    // POST http://localhost:8080/note/delete/3
    @PostMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") String id) {
        try {
            noteService.deleteById(id);
        } catch (IllegalArgumentException e) {
            return "redirect:/note/list?error=Note not found";
        }
        return "redirect:/note/list";
    }

    // GET  http://localhost:8080/note/edit/5
    @GetMapping("/edit/{id}")
    public ModelAndView editPage(@PathVariable("id") String id) {
        ModelAndView result = new ModelAndView("edit-note");
        try {
            Note note = noteService.getById(id);
            result.addObject("note", note);
        } catch (IllegalArgumentException e) {
            result.addObject("error", "Note not found");
            result.setViewName("redirect:/note/list");
        }
        return result;
    }

    // POST  http://localhost:8080/note/edit?id=3&title=newTitle&content=newContent
    @PostMapping("/edit")
    public String editNote(@RequestParam("id") String id,
                           @RequestParam(name = "title", required = false) String title,
                           @RequestParam(name = "content", required = false) String content) {
        try {
            Note note = noteService.getById(id);

            if (title != null && !title.isEmpty()) {
                if (title.length() < 2) {
                    return "redirect:/note/list?error=Title must contain at least 2 characters";
                }
                note.setTitle(title);
            }

            if (content != null && !content.isEmpty()) {
                note.setContent(content);
            } else {
                return "redirect:/note/list?error=No content";
            }

            noteService.save(note);
        } catch (IllegalArgumentException e) {
            return "redirect:/note/list?error=Note not found";
        }
        return "redirect:/note/list";
    }

//  GET  http://localhost:8080/note/searchTitle?query=4
    @GetMapping("/searchTitle")
    public ModelAndView searchByTitle(@RequestParam("query") String query) {
        ModelAndView result = new ModelAndView("list");
        result.addObject("notes", noteService.searchTitle(query));
        result.addObject("totalNotes", noteService.findCount());
        return result;
    }

//  GET  http://localhost:8080/note/searchTitleAndContent?query=JDBC
    @GetMapping("/searchTitleAndContent")
    public ModelAndView searchByTitleAndContent(@RequestParam("query") String query) {
        ModelAndView result = new ModelAndView("list");
        result.addObject("notes", noteService.searchTitleAndContent(query));
        result.addObject("totalNotes", noteService.findCount());
        return result;
    }

//  POST  http://localhost:8080/note/deleteByTitle?query=Some
    @PostMapping("/deleteByTitle")
    public String deleteByTitle(@RequestParam("query") String query) {
        noteService.deleteAllByTitle(query);
        return "redirect:/note/list";
    }
}
