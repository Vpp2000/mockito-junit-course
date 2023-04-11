package org.vpp97.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.vpp97.models.Exam;
import org.vpp97.repository.ExamRepository;
import org.vpp97.repository.ExamRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ExamServiceImplTest {

    @Test
    @DisplayName("Test find exam by name")
    void find_exam_by_name() {
        ExamRepository repository = Mockito.mock(ExamRepository.class);
        ExamService service = new ExamServiceImpl(repository);
        List<Exam> exams = Arrays.asList(new Exam(0L, "Programming"), new Exam(2L, "History"), new Exam(3L, "Chemistry"));

        when(repository.findAll()).thenReturn(exams);

        Optional<Exam> examOptional = service.findExamByName("Programming");

        assertTrue(examOptional.isPresent());

        Long expectedId = 0L;
        Long actualId = examOptional.orElseThrow().getId();

        assertEquals(expectedId, actualId);
        assertEquals("Programming", examOptional.orElseThrow().getName());
    }

    @Test
    @DisplayName("Test find exam by name in empty list")
    void find_by_exam_name_in_empty_list() {
        ExamRepository repository = Mockito.mock(ExamRepository.class);
        ExamService service = new ExamServiceImpl(repository);
        List<Exam> exams = Collections.emptyList();

        when(repository.findAll()).thenReturn(exams);

        Optional<Exam> examOptional = service.findExamByName("Programming");

        assertFalse(examOptional.isPresent());
    }


}