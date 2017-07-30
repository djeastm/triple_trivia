package com.davidjeastman.tripletrivia.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.davidjeastman.tripletrivia.Profile;
import com.davidjeastman.tripletrivia.Question;
import com.davidjeastman.tripletrivia.database.TriviaDbSchema.QuestionTable;
import com.davidjeastman.tripletrivia.database.TriviaDbSchema.ProfileTable;

import java.util.UUID;

/**
 * Created by David Eastman on 6/24/2017.
 */

public class TriviaCursorWrapper extends CursorWrapper {

    public TriviaCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Profile getProfile() {
        String uuidString = getString(getColumnIndex(ProfileTable.Cols.UUID));
        String name = getString(getColumnIndex(ProfileTable.Cols.NAME));
        String location = getString(getColumnIndex(ProfileTable.Cols.LOCATION));
        int stage = getInt(getColumnIndex(ProfileTable.Cols.STAGE));
        int level = getInt(getColumnIndex(ProfileTable.Cols.LEVEL));
        int skill = getInt(getColumnIndex(ProfileTable.Cols.SKILL));
        int points = getInt(getColumnIndex(ProfileTable.Cols.POINTS));
        int rank = getInt(getColumnIndex(ProfileTable.Cols.RANK));


        Profile profile = new Profile(UUID.fromString(uuidString));
        profile.setName(name);
        profile.setLocation(location);
        profile.setStage(stage);
        profile.setLevel(level);
        profile.setSkill(skill);
        profile.setPoints(points);
        profile.setRank(rank);

        return profile;
    }

    public Question getQuestion() {
        // Built-in
        int _id = getInt(getColumnIndex(QuestionTable.Cols._ID));
        String uuidString = getString(getColumnIndex(QuestionTable.Cols.UUID));
        int triple = getInt(getColumnIndex(QuestionTable.Cols.TRIPLE));
        int position = getInt(getColumnIndex(QuestionTable.Cols.POSITION));
        String correctAnswer = getString(getColumnIndex(QuestionTable.Cols.CORRECT_ANSWER));
        String answer2 = getString(getColumnIndex(QuestionTable.Cols.ANSWER2));
        String answer3 = getString(getColumnIndex(QuestionTable.Cols.ANSWER3));
        String answer4 = getString(getColumnIndex(QuestionTable.Cols.ANSWER4));
        String questionString = getString(getColumnIndex(QuestionTable.Cols.QUESTION));
        int difficulty = getInt(getColumnIndex(QuestionTable.Cols.DIFFICULTY));
        // User-affected
        String questionSeen = getString(getColumnIndex(QuestionTable.Cols.QUESTION_SEEN));
        String playerCorrect = getString(getColumnIndex(QuestionTable.Cols.PLAYER_CORRECT));
        String playerAnswer = getString(getColumnIndex(QuestionTable.Cols.PLAYER_ANSWER));

        Question question = new Question(UUID.fromString(uuidString));
        question.setId(_id);
        question.setTriple(triple);
        question.setPosition(position);
        question.setCorrectAnswer(correctAnswer);
        question.setAnswer2(answer2);
        question.setAnswer3(answer3);
        question.setAnswer4(answer4);
        question.setQuestion(questionString);
        question.setDifficulty(difficulty);
        question.setQuestionSeen(Boolean.parseBoolean(questionSeen));
        question.setPlayerCorrect(Boolean.parseBoolean(playerCorrect));
        question.setPlayerAnswer(playerAnswer);

        return question;
    }
}
