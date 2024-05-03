package org.example;

import org.example.service.NoteServiceImpl;

public class Main {
    public static void main(String[] args) {
        NoteServiceImpl noteService = new NoteServiceImpl();
        noteService.run();

    }
}