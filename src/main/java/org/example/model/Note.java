package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Note {
    private int id;
    private String text;
    private String[] labels;
}
