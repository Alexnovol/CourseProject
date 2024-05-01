package org.example.service;

import org.example.model.Note;

import java.util.List;

public interface NoteService {

    void printCommands();

    void createNote(String text, String[] labels);

    String getAllNotes();

    String getFilteredNotes(String[] filterLabels);

    boolean removeNoteById(String id);

    void saveNotesTextFile();
}
