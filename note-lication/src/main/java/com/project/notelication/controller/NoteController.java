package com.project.notelication.controller;

import com.project.notelication.dto.NoteDto;
import com.project.notelication.model.Note;
import com.project.notelication.model.User;
import com.project.notelication.service.NoteService;
import com.project.notelication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    private NoteDto convertToDto(Note note) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(note.getId());
        noteDto.setTitle(note.getTitle());
        noteDto.setContent(note.getContent());
        return noteDto;
    }

    private Note convertToEntity(NoteDto noteDto) {
        Note note = new Note();
        note.setId(noteDto.getId());
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        // User will be set by the service layer or fetched from DB for update
        return note;
    }

    @PostMapping
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto noteDto) {
        User currentUser = getCurrentUser();
        Note note = convertToEntity(noteDto);
        Note createdNote = noteService.createNote(note, currentUser);
        return ResponseEntity.ok(convertToDto(createdNote));
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getNotes() {
        User currentUser = getCurrentUser();
        List<Note> notes = noteService.getNotesByUser(currentUser);
        List<NoteDto> noteDtos = notes.stream()
                                      .map(this::convertToDto)
                                      .collect(Collectors.toList());
        return ResponseEntity.ok(noteDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id) {
        Optional<Note> note = noteService.getNoteById(id);
        if (note.isPresent() && note.get().getUser().equals(getCurrentUser())) {
            return ResponseEntity.ok(convertToDto(note.get()));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Note not found"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @RequestBody NoteDto noteDto) {
        Optional<Note> note = noteService.getNoteById(id);
        if (note.isPresent() && note.get().getUser().equals(getCurrentUser())) {
            Note existingNote = note.get();
            existingNote.setTitle(noteDto.getTitle());
            existingNote.setContent(noteDto.getContent());
            Note updatedNote = noteService.updateNote(existingNote);
            return ResponseEntity.ok(convertToDto(updatedNote));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Note not found"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        Optional<Note> note = noteService.getNoteById(id);
        if (note.isPresent() && note.get().getUser().equals(getCurrentUser())) {
            noteService.deleteNote(id);
            return ResponseEntity.ok(Map.of("message", "Note deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Note not found"));
        }
    }
}