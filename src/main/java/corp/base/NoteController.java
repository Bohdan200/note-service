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

//  http://localhost:8080/note/list
    @GetMapping("/list")
    public ModelAndView getAll() {
        ModelAndView result = new ModelAndView("list");
        result.addObject("notes", noteService.listAll());
        result.addObject("totalNotes", noteService.listAll().size());
        return result;
    }

//  http://localhost:8080/note/add
    @GetMapping("/add")
    public ModelAndView showAddNotePage() {
        return new ModelAndView("add-note");
    }

//  http://localhost:8080/note/add?title=someTitle&content=someContent
    @PostMapping("/add")
    public String addNote(@RequestParam("title") String title, @RequestParam("content") String content) {
        if (title == null || title.length() < 2) {
            return "redirect:/note/list?error=Title must contain at least 2 characters";
        }
        if (content == null || content.length() < 2) {
            return "redirect:/note/list?error=Content must contain at least 2 characters";
        }

        Note newNote = new Note();
        newNote.setId(noteService.listAll().size() + 1);
        newNote.setTitle(title);
        newNote.setContent(content);

        noteService.add(newNote);
        return "redirect:/note/list";
    }

    // http://localhost:8080/note/delete/3
    @PostMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") String id) {
        try {
            noteService.deleteById(Long.parseLong(id));
        } catch (IllegalArgumentException e) {
            return "redirect:/note/list?error=Note not found";
        }
        return "redirect:/note/list";
    }

//  http://localhost:8080/note/edit/5
    @GetMapping("/edit/{id}")
    public ModelAndView editPage(@PathVariable("id") String id) {
        ModelAndView result = new ModelAndView("edit-note");
        try {
            Note note = noteService.getById(Long.parseLong(id));
            result.addObject("note", note);
        } catch (IllegalArgumentException e) {
            result.addObject("error", "Note not found");
            result.setViewName("redirect:/note/list");
        }
        return result;
    }

//  http://localhost:8080/note/edit?id=3&title=newTitle&content=newContent
    @PostMapping("/edit")
    public String editNote(@RequestParam("id") String id,
                           @RequestParam(name = "title", required = false) String title,
                           @RequestParam(name = "content", required = false) String content) {
        try {
            Note note = noteService.getById(Long.parseLong(id));

            if (title != null && !title.isEmpty()) {
                if (title.length() < 2) {
                    return "redirect:/note/list?error=Title must contain at least 2 characters";
                }
                note.setTitle(title);
            }

            if (content != null && !content.isEmpty()) {
                if (content.length() < 2) {
                    return "redirect:/note/list?error=Content must contain at least 2 characters";
                }
                note.setContent(content);
            }

            noteService.update(note);
        } catch (IllegalArgumentException e) {
            return "redirect:/note/list?error=Note not found";
        }
        return "redirect:/note/list";
    }
}
