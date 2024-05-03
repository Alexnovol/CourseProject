package org.example.service;

import org.example.dao.NoteDaoImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NoteServiceImpl implements NoteService {

    private final Logger logger = Logger.getLogger(Class.class.getName());
    private final NoteDaoImpl noteDao = new NoteDaoImpl();
    private final Scanner scanner = new Scanner(System.in);
    private String command = "";



    public void run() {
        logger.setLevel(Level.FINE);
        System.out.println("Это ваша записная книжка, вот список доступных команд: help, note-new, note-list, " +
                "note-remove, note-export, exit");
        while (!command.equals("exit")) {
            System.out.println("Введите команду");
            command = scanner.nextLine();
            if (!isCorrectCommand(command)) {
                System.err.println("Команда не найдена");
            }
            switch (command) {
                case "help" -> handlerHelp();
                case "note-new" -> handlerNoteNew();
                case "note-list" -> handlerNoteList();
                case "note-remove" -> handlerNoteRemove();
                case "note-export" -> handlerNoteExport();
            }
        }
    }

    public void handlerHelp() {
        logger.log(Level.FINE, "Вызвана команда " + command);
        printCommands();
    }

    public void handlerNoteNew() {
        logger.log(Level.FINE, "вызвана команда " + command);
        System.out.println("Введите заметку");
        String text = scanner.nextLine();
        try {
            isCorrectTextNote(text);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Добавить метки? Метки состоят из одного слова и могут содержать только буквы. Для добавления нескольких меток разделяйте слова пробелом.");
        String[] labels = Arrays.stream(scanner.nextLine().split(" "))
                .filter(label -> !label.isEmpty()).toArray(String[]::new);
        try {
            createNote(text, labels);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void handlerNoteList() {
        logger.log(Level.FINE, "Вызвана команда " + command);
        System.out.println("Введите метки, чтобы отобразить определенные заметки " +
                "или оставьте пустым для отображения всех заметок");
        String[] filterLabels = Arrays.stream(scanner.nextLine().split(" "))
                .filter(label -> !label.isEmpty()).toArray(String[]::new);
        if (filterLabels.length > 0) {
            try {
                System.out.println(getFilteredNotes(prepareTextLabels(filterLabels)));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(getAllNotes());
        }
    }

    public void handlerNoteRemove() {
        logger.log(Level.FINE, "Вызвана команда " + command);
        System.out.println("Введите id удаляемой заметки");
        String id = scanner.nextLine();
        try {
            isCorrectId(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return;
        }
        if (removeNoteById(id)) {
            System.out.println("Заметка удалена");
        } else {
            System.out.println("Заметка не найдена");
        }
    }

    public void handlerNoteExport() {
        logger.log(Level.FINE, "Вызвана команда " + command);
        saveNotesTextFile();
    }

    public boolean isCorrectCommand(String command) {
        String[] commands = {"help", "note-new", "note-list", "note-remove", "note-export", "exit"};
        if (!Arrays.asList(commands).contains(command)) {
            logger.log(Level.WARNING, "Вызвана некорректная команда");
            return false;
        }
        return true;
    }

    public void isCorrectTextNote(String text) {
        if (text.length() < 3) {
            logger.log(Level.INFO, "Текст заметки должен быть длиннее 3 символов, введено - " + text.length());
            throw new IllegalArgumentException("Текст заметки должен быть длиннее 3 символов");
        }
    }

    public void isCorrectId(String id) {
        if (!id.matches("\\d+")) {
            logger.log(Level.INFO, "Id должно быть целым числом, введено - " + id);
            throw new IllegalArgumentException("Id должно быть целым числом");
        }
    }

    public String[] prepareTextLabels(String[] labels) {
        String[] result = Arrays.stream(labels).map(String::toUpperCase).toArray(String[]::new);
        if (result.length > 0) {
            if (!Arrays.stream(result).allMatch(label -> label.matches("[A-ZА-Я]+"))) {
                logger.log(Level.INFO, "Пользователь ввел метки, состоящие не только из букв");
                throw new IllegalArgumentException("Метки должны состоять только из букв");
            }
        }
        return result;

    }


    @Override
    public void printCommands() {
        String listCommands = """
                help - выводит на экран список доступных команд с их описанием
                note-new  - создать новую заметку
                note-list - выводит все заметки на экран
                note-remove - удаляет заметку
                note-export - сохраняет все заметки в текстовый файл и выводит имя сохраненного файла
                exit - выход из приложения""";
        System.out.println(listCommands);
    }

    @Override
    public void createNote(String text, String[] labels) {
        noteDao.createNote(text, prepareTextLabels(labels));
        System.out.println("Заметка добавлена");
    }

    @Override
    public String getAllNotes() {
        return noteDao.getAllNotes();
    }

    @Override
    public String getFilteredNotes(String[] filterLabels) {
        List<String> notes = new ArrayList<>(Arrays.asList(getAllNotes().split("===================")));
        Iterator<String> iterator = notes.iterator();
        for (String label : filterLabels) {
            while (iterator.hasNext()) {
                String note = iterator.next();
                if (!note.contains(label)) {
                    iterator.remove();
                }
            }
        }
        return String.join("===================", notes);
    }

    @Override
    public boolean removeNoteById(String id) {
        return noteDao.removeNoteById(id);
    }

    @Override
    public void saveNotesTextFile() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd_HH.mm.ss"));
        String filePath = String.format("notes_%s.txt", dateTime);
        String notes = getAllNotes();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(notes.getBytes(StandardCharsets.UTF_8));
            System.out.println("Заметки успешно сохранены. Имя сохраненного файла: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Произошло исключение при записи заметок в файл");
        }
    }
}
