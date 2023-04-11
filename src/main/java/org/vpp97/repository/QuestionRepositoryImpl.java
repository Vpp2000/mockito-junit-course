package org.vpp97.repository;

import org.vpp97.data.Data;

import java.util.List;

public class QuestionRepositoryImpl implements QuestionRepository{
    @Override
    public List<String> findQuestionsByExamId(Long examId) {
        System.out.println("findQuestionsByExamId");
        return Data.QUESTION_LIST;
    }

    @Override
    public void saveMany(List<String> questions) {
        System.out.println("saveMany");
    }
}
