package com.example.topquiz.Model;

import java.util.List;

public class QuestionBank {
    private List<Question> mQuestionList;
    private int mNextQuestionIndex;


    public QuestionBank(List<Question> questionList) {
        // Shuffle the question list before storing it
        mQuestionList = questionList;
        mNextQuestionIndex = 0;
    }

    public List<Question> getQuestionList() {
        return mQuestionList;
    }

    public Question getNextQuestion() {
        mNextQuestionIndex++;
        return this.getCurrentQuestion();
    }

    public Question getCurrentQuestion(){
        return this.getQuestionList().get(mNextQuestionIndex);
    }
}
