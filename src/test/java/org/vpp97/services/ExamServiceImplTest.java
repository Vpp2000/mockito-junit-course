package org.vpp97.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // same as MockitoAnnotations.openMocks(this);
class ExamServiceImplTest {
    @Mock   // same as Mockito.mock
    private ExamRepository repository;
    @Mock  // same as Mockito.mock
    private QuestionRepository questionRepository;
    @InjectMocks  // same as new ExamServiceImpl(this.repository, this.questionRepository);
    private ExamServiceImpl service;

    @BeforeEach
    void setupTestCases(){
        //MockitoAnnotations.openMocks(this);
        //this.repository = Mockito.mock(ExamRepository.class);
        //this.questionRepository = Mockito.mock(QuestionRepository.class);
        //this.service = new ExamServiceImpl(this.repository, this.questionRepository);
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

        verify(repository).findAll();
        verify(questionRepository).findQuestionsByExamId(0L);
    }

    @Test
    @DisplayName("Test find exam with questions included but course doesnt exist")
    void find_non_existent_exam_with_questions(){
        when(this.repository.findAll()).thenReturn(Data.EXAM_LIST);
        //when(this.questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTION_LIST); // ERROR, UNNECESSARY STUBBING DETECTED
        Exam exam = service.findExamByNameWithQuestions("Anatomy");
        assertNull(exam);
        // verify(questionRepository).findQuestionsByExamId(anyLong());  ERROR, WANTED BUT NOT INVOKED
    }

    @Test
    @DisplayName("Test saving exam method")
    void save_exam(){

        // GIVEN
        Exam newExam = Data.EXAM;
        newExam.setQuestions(Data.QUESTION_LIST);

        //WHEN
        when(this.repository.save(any(Exam.class))).then(new Answer<Exam>() {
            Long sequence = 8L;
            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        });

        // THEN
        Exam exam = service.save(newExam);
        assertNotNull(exam.getId());
        assertEquals(8L, exam.getId());

        verify(this.repository).save(any(Exam.class));
        verify(this.questionRepository).saveMany(anyList());
    }


}