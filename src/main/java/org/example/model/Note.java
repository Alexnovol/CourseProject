package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@EqualsAndHashCode
public class Note {
    private int id = 0;
    private String text;
    private String[] labels;

    public int genId() {
        id++;
        return id;
    }
}
