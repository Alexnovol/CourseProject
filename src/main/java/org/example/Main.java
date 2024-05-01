package org.example;

import org.example.service.NoteService;
import org.example.service.NoteServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        NoteServiceImpl noteService = new NoteServiceImpl();
        noteService.run();

    }
}