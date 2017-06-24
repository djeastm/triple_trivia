package com.davidjeastman.hardcoretrivia.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.davidjeastman.hardcoretrivia.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.davidjeastman.hardcoretrivia.Question;
import com.davidjeastman.hardcoretrivia.database.QuestionDbSchema.QuestionTable;

/**
 * Created by David Eastman on 6/24/2017.
 */

public class QuestionCursorWrapper extends CursorWrapper {

    public QuestionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Question getQuestion() {
    String uuidString = getString(getColumnIndex(QuestionTable.Cols.UUID));
        String batch = getString(getColumnIndex(QuestionTable.Cols.TRIPLE));
        String order = getString(getColumnIndex(QuestionTable.Cols.ORDER));
        String correctAnswer = getString(getColumnIndex(QuestionTable.Cols.CORRECT_ANSWER));
        String answer2 = getString(getColumnIndex(QuestionTable.Cols.ANSWER2));
        String answer3 = getString(getColumnIndex(QuestionTable.Cols.ANSWER3));
        String answer4 = getString(getColumnIndex(QuestionTable.Cols.ANSWER4));
        String questionString = getString(getColumnIndex(QuestionTable.Cols.QUESTION));

        Question question = new Question(UUID.fromString(uuidString));
        question.setAnswer2(batch);
        question.setOrder(order);
        question.setCorrectAnswer(correctAnswer);
        question.setAnswer2(answer2);
        question.setAnswer3(answer3);
        question.setAnswer4(answer4);
        question.setQuestion(questionString);

        return question;
    }

    private List<String> convertFromSQLString(String sqlString) {
        if (sqlString != null) {
            List<String> array = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONObject(sqlString).getJSONArray("itemList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    array.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return array;
        }
        else return new ArrayList<>();
    }
}
