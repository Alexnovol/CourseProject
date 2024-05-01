package org.example.dao;
import org.example.model.Note;

import java.util.List;

public interface NoteDao {

    void createNote(String text, String[] labels);

    String getAllNotes();

    boolean removeNoteById(String id);
}
