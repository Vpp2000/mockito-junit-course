package org.vpp97.repository;

import org.vpp97.data.Data;
import org.vpp97.models.Exam;

import java.util.Arrays;
import java.util.List;

public class ExamRepositoryImpl implements ExamRepository{
    @Override
    public List<Exam> findAll() {
        System.out.println("Find all");
        return Arrays.asList(new Exam(0L, "Programming"),new Exam(1L, "Math"), new Exam(2L, "History"), new Exam(3L, "Chemistry"));
    }

    @Override
    public Exam save(Exam exam) {
        System.out.println("Save exam");
        return exam;
    }
}
