package org.vpp97.services;

import org.vpp97.models.Exam;

public interface ExamService {
    Exam findExamByName(String name);
}
