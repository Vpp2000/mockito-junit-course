package org.vpp97.repository;

import org.vpp97.models.Exam;

import java.util.List;

public interface ExamRepository {
    List<Exam> findAll();
    Exam save(Exam exam);
}
