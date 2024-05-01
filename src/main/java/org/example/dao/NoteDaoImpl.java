package org.example.dao;

import lombok.Getter;
import org.example.dbnotes.DbNotes;
import org.example.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDaoImpl implements NoteDao {
    @Getter
    private static final DbNotes dbNotes = new DbNotes();

    @Override
    public void createNote(String text, String[] labels) {
        Note note = new Note();
        note.setId(dbNotes.getId());
        note.setText(text);
        note.setLabels(labels);
        dbNotes.getNotes().add(note);
    }

    @Override
    public String getAllNotes() {
        StringBuilder sb = new StringBuilder();
        List<Note> notes = dbNotes.getNotes();
        for (Note note : notes) {
            sb.append(note.getId()).append("#").append(note.getText()).append("\n");
            String[] labels = note.getLabels();
            for (int i = 0; i < labels.length; i++) {
                if (i != labels.length - 1) {
                    sb.append(labels[i]).append(";");
                } else {
                    sb.append(labels[i]).append("\n");
                }
            }
            sb.append("\n");
            if (notes.indexOf(note) != notes.size() - 1) {
                sb.append("===================").append("\n").append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean removeNoteById(String id) {
        int numId = Integer.parseInt(id);
        List<Note> notes = dbNotes.getNotes();
        List<Note> notesFiltered = new ArrayList<>(notes.stream().filter(note -> note.getId() != numId).toList());
        if (notes.size() == notesFiltered.size()) {
            return false;
        } else {
            dbNotes.setNotes(notesFiltered);
            return true;
        }
    }
}
