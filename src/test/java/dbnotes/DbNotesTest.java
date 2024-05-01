package dbnotes;

import org.example.dao.NoteDaoImpl;
import org.example.dbnotes.DbNotes;
import org.example.service.NoteServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DbNotesTest {
    private final DbNotes dbNotes = NoteDaoImpl.getDbNotes();
    private final NoteServiceImpl noteService = new NoteServiceImpl();

    @AfterEach
    public void clearDb() {
        dbNotes.setNotes(new ArrayList<>());
        dbNotes.setId(0);
    }



    @Test
    @DisplayName("Метод getId генерирует уникальные id")
    public void getId() {
        String testText = "Первая заметка";
        String[] testLabels = {"заполняемежедневник", "мойежедневник", "личныедневники"};
        noteService.createNote(testText, testLabels);
        String id1 = Integer.toString(dbNotes.getNotes().get(0).getId());
        noteService.removeNoteById(id1);
        noteService.createNote(testText, testLabels);
        String id2 = Integer.toString(dbNotes.getNotes().get(0).getId());
        noteService.removeNoteById(id2);
        assertNotEquals(id1, id2);
    }
}
