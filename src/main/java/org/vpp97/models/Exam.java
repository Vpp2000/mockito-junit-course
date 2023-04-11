package org.vpp97.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
public class Exam {
    private Long id;
    private String name;
    private List<String> questions;

    public Exam(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
