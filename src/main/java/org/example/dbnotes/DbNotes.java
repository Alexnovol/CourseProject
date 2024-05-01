package org.example.dbnotes;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Note;

import java.util.ArrayList;
import java.util.List;

public class DbNotes {
    @Setter
    @Getter
    private List<Note> notes = new ArrayList<>();
    @Setter
    private int id = 0;

    public int getId() {
        id++;
        return id;
    }
}
