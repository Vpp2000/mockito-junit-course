package org.vpp97.data;

import org.vpp97.models.Exam;

import java.util.Arrays;
import java.util.List;

public class Data {
    public static final List<Exam> EXAM_LIST = Arrays.asList(
            new Exam(0L, "Programming"),
            new Exam(2L, "History"),
            new Exam(3L, "Chemistry"),
            new Exam(4L, "Math"));

    public static final List<Exam> EXAM_LIST_IDS_NULL= Arrays.asList(
            new Exam(null, "Programming"),
            new Exam(null, "History"),
            new Exam(null, "Chemistry"),
            new Exam(null, "Math"));

    public static final List<Exam> EXAM_LIST_IDS_NEGATIVE= Arrays.asList(
            new Exam(-1L, "Programming"),
            new Exam(-2L, "History"),
            new Exam(-3L, "Chemistry"),
            new Exam(-4L, "Math"));



    public static final List<String> QUESTION_LIST = Arrays.asList(
            "DERIVATIVES",
            "CALCULUS",
            "LIMITS"
    );

    public static final Exam EXAM = new Exam(null, "Gym");
}
