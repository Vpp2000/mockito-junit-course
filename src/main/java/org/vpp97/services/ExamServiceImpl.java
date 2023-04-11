package org.vpp97.services;

import org.vpp97.models.Exam;
import org.vpp97.repository.ExamRepository;

import java.util.Optional;

public class ExamServiceImpl implements ExamService{
    private ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public Optional<Exam> findExamByName(String name) {
        Optional<Exam> examOptional = examRepository.findAll()
                .stream()
                .filter(e -> e.getName().contains(name))
                .findFirst();

        return examOptional;
    }
}
