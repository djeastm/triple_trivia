package com.davidjeastman.hardcoretrivia;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by David Eastman on 6/24/2017.
 */

public class Question implements Serializable{
    public static final String TAG = "Question";

    private int mId;
    private UUID mUUID;
    private int mTriple;
    private int mPosition;
    private String mCorrectAnswer;
    private String mAnswer2;
    private String mAnswer3;
    private String mAnswer4;
    private String mQuestion;
    private int difficulty;
    private boolean mQuestionSeen;
    private boolean mPlayerCorrect;
    private String mPlayerAnswer;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Question() {
        this(UUID.randomUUID());
    }

    public Question(UUID id) {
        mUUID = id;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public int getTriple() {
        return mTriple;
    }

    public void setTriple(int triple) {
        mTriple = triple;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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
