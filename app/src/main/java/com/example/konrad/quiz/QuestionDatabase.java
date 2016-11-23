package com.example.konrad.quiz;

import java.util.List;

/**
 * Created by Konrad on 2016-11-23.
 */

public interface QuestionDatabase {
    List<Question> getQuestions(int difficulty);

}
