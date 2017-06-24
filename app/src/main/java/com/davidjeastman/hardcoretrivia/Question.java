package com.davidjeastman.hardcoretrivia;

import java.util.UUID;

/**
 * Created by David Eastman on 6/24/2017.
 */

public class Question {
    public static final String TAG = "Question";

    private UUID mId;
    private int mTriple;
    private int mOrder;
    private String mCorrectAnswer;
    private String mAnswer2;
    private String mAnswer3;
    private String mAnswer4;
    private String mQuestion;
    private boolean mQuestionSeen;
    private boolean mPlayerCorrect;
    private String mPlayerAnswer;

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

    public int getTriple() {
        return mTriple;
    }

    public void setTriple(int triple) {
        mTriple = triple;
    }

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int order) {
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

    public boolean isQuestionSeen() {
        return mQuestionSeen;
    }

    public void setQuestionSeen(boolean questionSeen) {
        mQuestionSeen = questionSeen;
    }

    public boolean isPlayerCorrect() {
        return mPlayerCorrect;
    }

    public void setPlayerCorrect(boolean playerCorrect) {
        mPlayerCorrect = playerCorrect;
    }

    public String getPlayerAnswer() {
        return mPlayerAnswer;
    }

    public void setPlayerAnswer(String playerAnswer) {
        mPlayerAnswer = playerAnswer;
    }
}
