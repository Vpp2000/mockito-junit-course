package org.vpp97.repository;

import java.util.List;

public interface QuestionRepository {
    List<String> findQuestionsByExamId(Long examId);
    void saveMany(List<String> questions);
}
