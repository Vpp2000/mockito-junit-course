package org.vpp97.models;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class Exam {
    private Long id;

    public Exam(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private String name;
    private List<String> questions;
}
