package model;


import org.example.dao.NoteDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NoteTest {
    @Spy
    private NoteDaoImpl noteDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Метод genId генерирует уникальные id")
    public void genId() {
        String testText = "Первая заметка";
        String[] testLabels = {"заполняемежедневник", "мойежедневник", "личныедневники"};
        noteDao.createNote(testText, testLabels);
        String id1 = Integer.toString(noteDao.getNotes().get(0).getId());
        noteDao.removeNoteById(id1);
        noteDao.createNote(testText, testLabels);
        String id2 = Integer.toString(noteDao.getNotes().get(0).getId());
        noteDao.removeNoteById(id2);
        assertNotEquals(id1, id2);
    }
}
