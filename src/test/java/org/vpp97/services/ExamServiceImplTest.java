package org.vpp97.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.vpp97.data.Data;
import org.vpp97.models.Exam;
import org.vpp97.repository.ExamRepository;
import org.vpp97.repository.ExamRepositoryImpl;
import org.vpp97.repository.QuestionRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ExamServiceImplTest {
    private ExamRepository repository;
    private ExamService service;
    private QuestionRepository questionRepository;
    @BeforeEach
    void setupTestCases(){
        this.repository = Mockito.mock(ExamRepository.class);
        this.questionRepository = Mockito.mock(QuestionRepository.class);
        this.service = new ExamServiceImpl(this.repository, this.questionRepository);
    }

    @Test
    @DisplayName("Test find exam by name")
    void find_exam_by_name() {
        List<Exam> exams = Data.EXAM_LIST;

        when(this.repository.findAll()).thenReturn(exams);

        Optional<Exam> examOptional = this.service.findExamByName("Programming");

        assertTrue(examOptional.isPresent());

        Long expectedId = 0L;
        Long actualId = examOptional.orElseThrow().getId();

        assertEquals(expectedId, actualId);
        assertEquals("Programming", examOptional.orElseThrow().getName());
    }

    @Test
    @DisplayName("Test find exam by name in empty list")
    void find_by_exam_name_in_empty_list() {
        List<Exam> exams = Collections.emptyList();

        when(this.repository.findAll()).thenReturn(exams);

        Optional<Exam> examOptional = this.service.findExamByName("Programming");

        assertFalse(examOptional.isPresent());
    }

    @Test
    @DisplayName("Test find exam with questions included")
    void find_exam_with_questions(){
        when(this.repository.findAll()).thenReturn(Data.EXAM_LIST);
        when(this.questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTION_LIST);
        Exam exam = service.findExamByNameWithQuestions("Programming");

        assertEquals(3, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("LIMITS"));
    }


}