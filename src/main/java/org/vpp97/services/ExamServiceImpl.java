package org.vpp97.services;

import org.vpp97.models.Exam;
import org.vpp97.repository.ExamRepository;
import org.vpp97.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

public class ExamServiceImpl implements ExamService{
    private ExamRepository examRepository;
    private QuestionRepository questionRepository;

    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<Exam> findExamByName(String name) {
        Optional<Exam> examOptional = examRepository.findAll()
                .stream()
                .filter(e -> e.getName().contains(name))
                .findFirst();

        return examOptional;
    }

    @Override
    public Exam findExamByNameWithQuestions(String name) {
        Optional<Exam> examOptional = this.findExamByName(name);
        Exam exam = null;

        if(examOptional.isPresent()) {
            exam = examOptional.get();
            List<String> questions = questionRepository.findQuestionsByExamId(exam.getId());
            System.out.println("id: " + exam.getId() + " -- " + " questions:" + questions);
            exam.setQuestions(questions);
        }

        return exam;
    }

    @Override
    public Exam save(Exam exam) {
        if(!exam.getQuestions().isEmpty()) {
            questionRepository.saveMany(exam.getQuestions());
        }

        return examRepository.save(exam);
    }
}
