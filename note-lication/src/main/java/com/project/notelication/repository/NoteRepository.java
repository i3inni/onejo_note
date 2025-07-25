package com.project.notelication.repository;

import com.project.notelication.model.Note;
import com.project.notelication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
}
