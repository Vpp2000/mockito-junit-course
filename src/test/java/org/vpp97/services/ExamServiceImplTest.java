package org.vpp97.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.vpp97.data.DataTest;
import org.vpp97.models.Exam;
import org.vpp97.repository.ExamRepository;
import org.vpp97.repository.ExamRepositoryImpl;
import org.vpp97.repository.QuestionRepository;
import org.vpp97.repository.QuestionRepositoryImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // same as MockitoAnnotations.openMocks(this);
class ExamServiceImplTest {
    @Mock   // same as Mockito.mock
    private ExamRepositoryImpl repository;
    @Mock  // same as Mockito.mock
    private QuestionRepositoryImpl questionRepository;
    @InjectMocks  // same as new ExamServiceImpl(this.repository, this.questionRepository);
    private ExamServiceImpl service;
    @Captor
    private ArgumentCaptor<Long> longCaptor;

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
        List<Exam> exams = DataTest.EXAM_LIST;

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
        when(this.repository.findAll()).thenReturn(DataTest.EXAM_LIST);
        when(this.questionRepository.findQuestionsByExamId(anyLong())).thenReturn(DataTest.QUESTION_LIST);
        Exam exam = service.findExamByNameWithQuestions("Programming");

        assertEquals(3, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("LIMITS"));

        verify(repository).findAll();
        verify(questionRepository).findQuestionsByExamId(0L);
    }

    @Test
    @DisplayName("Test find exam with questions included but course doesnt exist")
    void find_non_existent_exam_with_questions(){
        when(this.repository.findAll()).thenReturn(DataTest.EXAM_LIST);
        //when(this.questionRepository.findQuestionsByExamId(anyLong())).thenReturn(DataTest.QUESTION_LIST); // ERROR, UNNECESSARY STUBBING DETECTED
        Exam exam = service.findExamByNameWithQuestions("Anatomy");
        assertNull(exam);
        // verify(questionRepository).findQuestionsByExamId(anyLong());  ERROR, WANTED BUT NOT INVOKED
    }

    @Test
    @DisplayName("Test saving exam method")
    void save_exam(){

        // GIVEN
        Exam newExam = DataTest.EXAM;
        newExam.setQuestions(DataTest.QUESTION_LIST);

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

    @Test
    void test_exception_management(){
        when(this.repository.findAll()).thenReturn(DataTest.EXAM_LIST_IDS_NULL);
        when(this.questionRepository.findQuestionsByExamId(isNull())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.service.findExamByNameWithQuestions("Programming");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(this.repository).findAll();
        verify(this.questionRepository).findQuestionsByExamId(isNull());
    }


    @Test
    void test_with_arg_matchers() {
        when(this.repository.findAll()).thenReturn(DataTest.EXAM_LIST);
        when(this.questionRepository.findQuestionsByExamId(anyLong())).thenReturn(DataTest.QUESTION_LIST); // ERROR, UNNECESSARY STUBBING DETECTED
        this.service.findExamByNameWithQuestions("Programming");

        verify(this.repository).findAll();
        verify(this.questionRepository).findQuestionsByExamId(argThat(
                        arg -> arg != null && arg == 0L
                )
        );
    }

    @Nested
    class CustomArgsMatcher implements ArgumentMatcher<Long> {
        private Long argument;
        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument >= 0L;
        }

        @Override
        public String toString() {
            return String.format("Custom message from CustomArgsMatcher on error. \n Argument with value %s must be a positive number or zero, but non negative", argument);
        }


        @Test
        void test_with_arg_matchers_inner_class() {
            //when(repository.findAll()).thenReturn(DataTest.EXAM_LIST_IDS_NEGATIVE);
            when(repository.findAll()).thenReturn(DataTest.EXAM_LIST);
            when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(DataTest.QUESTION_LIST); // ERROR, UNNECESSARY STUBBING DETECTED
            service.findExamByNameWithQuestions("Programming");

            verify(repository).findAll();
            verify(questionRepository).findQuestionsByExamId(argThat(new CustomArgsMatcher())
            );
        }
    }

    @Test
    @DisplayName("Test with argument captor")
    void test_with_arg_captor() {
        when(repository.findAll()).thenReturn(DataTest.EXAM_LIST);
        // ITS NOT NECESSARY FOR THIS STUF when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(DataTest.QUESTION_LIST); // ERROR, UNNECESSARY STUBBING DETECTED
        service.findExamByNameWithQuestions("Programming");
        verify(repository).findAll();
        verify(questionRepository).findQuestionsByExamId(longCaptor.capture());

        assertEquals(0L, longCaptor.getValue());
    }

    @Nested
    class DoMethodsTesting {
        @Test
        @DisplayName("Test do throw")
        void test_do_throw() {
            Exam exam = DataTest.EXAM;
            exam.setQuestions(DataTest.QUESTION_LIST);
            doThrow(IllegalArgumentException.class).when(questionRepository).saveMany(anyList());
            assertThrows(IllegalArgumentException.class, () -> {
               service.save(exam);
            });
        }
    }
    @Nested
    class DoSomethingTesting {
        @Test
        @DisplayName("Test do answer")
        void test_do_answer() {
            when(repository.findAll()).thenReturn(DataTest.EXAM_LIST);

            //when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(DataTest.QUESTION_LIST); // ERROR, UNNECESSARY STUBBING DETECTED
            doAnswer(invocationOnMock -> {
                Long examId = invocationOnMock.getArgument(0);
                List<String> questions = examId == 0L ? DataTest.QUESTION_LIST : Collections.emptyList();
                return questions;
            }).when(questionRepository).findQuestionsByExamId(anyLong());

            Exam exam = service.findExamByNameWithQuestions("Programming");

            assertEquals(0L, exam.getId());
            assertNotNull(exam.getQuestions());

            verify(questionRepository).findQuestionsByExamId(anyLong());

        }

        @Test
        @DisplayName("Test saving exam method")
        void save_exam(){

            // GIVEN
            Exam newExam = DataTest.EXAM;
            newExam.setQuestions(DataTest.QUESTION_LIST);

            //WHEN
            doAnswer(new Answer<Exam>() {
                Long sequence = 8L;
                @Override
                public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                    Exam exam = invocationOnMock.getArgument(0);
                    exam.setId(sequence++);
                    return exam;
                }
            }).when(repository).save(any(Exam.class));

            // THEN
            Exam exam = service.save(newExam);
            assertNotNull(exam.getId());
            assertEquals(8L, exam.getId());

            verify(repository).save(any(Exam.class));
            verify(questionRepository).saveMany(anyList());
        }

        @Test
        @DisplayName("Test using real methods")
        void test_real_methods(){
            when(repository.findAll()).thenReturn(DataTest.EXAM_LIST);
            doCallRealMethod().when(questionRepository).findQuestionsByExamId(anyLong());
            Exam exam = service.findExamByNameWithQuestions("Programming");
            assertEquals(0L,exam.getId());
            assertEquals("Programming", exam.getName());
        }
    }

    @Test
    @DisplayName("Test using spies")
    void test_using_spies(){
        ExamRepository examRepository = spy(ExamRepositoryImpl.class);
        QuestionRepository questionRepo = spy(QuestionRepositoryImpl.class);
        ExamService examService = new ExamServiceImpl(examRepository, questionRepo);

        doReturn(DataTest.QUESTION_LIST).when(questionRepo).findQuestionsByExamId(anyLong());

        Exam exam = examService.findExamByNameWithQuestions("Programming");
        assertNotNull(exam);
        assertEquals("Programming", exam.getName());
        assertTrue(!exam.getQuestions().isEmpty());

        verify(examRepository).findAll();
        verify(questionRepo).findQuestionsByExamId(anyLong());
    }


}