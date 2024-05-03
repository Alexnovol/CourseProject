package org.example.dao;

import lombok.Getter;
import org.example.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDaoImpl implements NoteDao {
    @Getter
    private List<Note> notes = new ArrayList<>();
    private final Note noteGen = new Note();

    @Override
    public void createNote(String text, String[] labels) {
        Note note = new Note();
        note.setId(noteGen.genId());
        note.setText(text);
        note.setLabels(labels);
        notes.add(note);
    }

    @Override
    public String getAllNotes() {
        StringBuilder sb = new StringBuilder();
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
        List<Note> notesFiltered = new ArrayList<>(notes.stream().filter(note -> note.getId() != numId).toList());
        if (notes.size() == notesFiltered.size()) {
            return false;
        } else {
            notes = notesFiltered;
            return true;
        }
    }
}
