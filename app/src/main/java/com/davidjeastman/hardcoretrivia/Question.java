package com.davidjeastman.hardcoretrivia;

import java.util.UUID;

/**
 * Created by David Eastman on 6/24/2017.
 */

public class Question {
    public static final String TAG = "Question";

    private UUID mId;
    private String mTriple;
    private String mOrder;
    private String mCorrectAnswer;
    private String mAnswer2;
    private String mAnswer3;
    private String mAnswer4;
    private String mQuestion;

    public Question() {
        this(UUID.randomUUID());
    }

    public Question(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTriple() {
        return mTriple;
    }

    public void setTriple(String triple) {
        mTriple = triple;
    }

    public String getOrder() {
        return mOrder;
    }

    public void setOrder(String order) {
        mOrder = order;
    }

    public String getCorrectAnswer() {
        return mCorrectAnswer;
    }

    public void setCorrectAnswer(String correct_answer) {
        mCorrectAnswer = correct_answer;
    }

    public String getAnswer2() {
        return mAnswer2;
    }

    public void setAnswer2(String answer2) {
        mAnswer2 = answer2;
    }

    public String getAnswer3() {
        return mAnswer3;
    }

    public void setAnswer3(String answer3) {
        mAnswer3 = answer3;
    }

    public String getAnswer4() {
        return mAnswer4;
    }

    public void setAnswer4(String answer4) {
        mAnswer4 = answer4;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }
}
