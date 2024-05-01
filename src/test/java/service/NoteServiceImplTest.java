package service;

import org.example.dao.NoteDaoImpl;
import org.example.dbnotes.DbNotes;
import org.example.service.NoteServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class NoteServiceImplTest {

    private final NoteServiceImpl noteService = new NoteServiceImpl();
    private final DbNotes dbNotes = NoteDaoImpl.getDbNotes();


    @AfterEach
    public void clearDb() {
        dbNotes.setNotes(new ArrayList<>());
        dbNotes.setId(0);
    }


    @Test
    @DisplayName("Метод isCorrectCommand пропускает корректную команду")
    public void shouldApproveCorrectCommand() {
        String[] commands = {"help", "note-new", "note-list", "note-remove", "note-export", "exit"};
        for (String command : commands) {
            assertTrue(noteService.isCorrectCommand(command));
        }
    }

    @Test
    @DisplayName("Метод isCorrectCommand не пропускает некорректную команду")
    public void shouldNotApproveIncorrectCommand() {
        assertFalse(noteService.isCorrectCommand("notenew"));
    }

    @Test
    @DisplayName("Метод isCorrectTextNote не вызывает исключений при корректном тексте")
    public void shouldNotThrowExceptionWithCorrectText() {
        assertDoesNotThrow(() -> noteService.isCorrectTextNote("текст"));
    }

    @Test
    @DisplayName("Метод isCorrectTextNote вызывает исключение при некорректном тексте")
    public void shouldThrowExceptionWithIncorrectText() {
        assertThrows(IllegalArgumentException.class, () -> noteService.isCorrectTextNote("те"));
    }

    @Test
    @DisplayName("Метод prepareTextLabels не вызывает исключений при корректных метках")
    public void shouldNotThrowExceptionWithCorrectLabels() {
        String[] labels = {"заполняемежедневник", "мойежедневник", "личныедневники"};
        assertDoesNotThrow(() -> noteService.prepareTextLabels(labels));
    }

    @Test
    @DisplayName("Метод prepareTextLabels вызывает исключение при некорректных метках")
    public void shouldThrowExceptionWithIncorrectLabels() {
        String[] incorrectLabels = {"Метк1", "12345", ","};
        assertThrows(IllegalArgumentException.class, () -> noteService.prepareTextLabels(incorrectLabels));
    }

    @Test
    @DisplayName("Метод isCorrectId не вызывает исключений при корректном id")
    public void shouldNotThrowExceptionWithCorrectId() {
        assertDoesNotThrow(() -> noteService.isCorrectId("1"));
    }

    @Test
    @DisplayName("Метод isCorrectId вызывает исключение при некорректном id")
    public void shouldThrowExceptionWithIncorrectId() {
        assertThrows(IllegalArgumentException.class, () -> noteService.isCorrectId("один"));
    }

    @Test
    @DisplayName("Метод createNote создает заметку из введенных данных")
    public void createNote() {
        String testText1 = "Первая заметка";
        String[] testLabels1 = {"заполняемежедневник", "мойежедневник", "личныедневники"};
        String testText2 = "Вторая заметка";
        String[] testLabels2 = {"Метка"};
        noteService.createNote(testText1, testLabels1);
        noteService.createNote(testText2, testLabels2);
        String expected = """
                1#Первая заметка
                ЗАПОЛНЯЕМЕЖЕДНЕВНИК;МОЙЕЖЕДНЕВНИК;ЛИЧНЫЕДНЕВНИКИ
                
                ===================
                
                2#Вторая заметка
                МЕТКА
                
                """;
        assertEquals(expected, noteService.getAllNotes());
    }

}
