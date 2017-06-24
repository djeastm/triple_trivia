package com.davidjeastman.hardcoretrivia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.davidjeastman.hardcoretrivia.database.QuestionBaseHelper;
import com.davidjeastman.hardcoretrivia.database.QuestionCursorWrapper;
import com.davidjeastman.hardcoretrivia.database.QuestionDbSchema.QuestionTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Created by David Eastman on 6/24/2017.
 */

public class QuestionBank {
    public static final String APP_NAME = "HardcoreTrivia";
    public static final String APP_DIRECTORY = APP_NAME;

    private static QuestionBank sQuestionBank;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private QuestionBank(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new QuestionBaseHelper(mContext)
                .getWritableDatabase();
    }

    public static QuestionBank get(Context context) {
        if (sQuestionBank == null) {
            sQuestionBank = new QuestionBank(context);
        }
        return sQuestionBank;
    }

    private static ContentValues getContentValues(Question Question) {
        ContentValues values = new ContentValues();
        values.put(QuestionTable.Cols.UUID, Question.getId().toString());
        values.put(QuestionTable.Cols.BATCH, Question.getTriple());
        values.put(QuestionTable.Cols.ORDER, Question.getOrder());
        values.put(QuestionTable.Cols.CORRECT_ANSWER, Question.getCorrectAnswer());
        values.put(QuestionTable.Cols.ANSWER2, Question.getAnswer2());
        values.put(QuestionTable.Cols.ANSWER3, Question.getAnswer3());
        values.put(QuestionTable.Cols.ANSWER4, Question.getAnswer4());
        values.put(QuestionTable.Cols.QUESTION, Question.getQuestion());

        return values;
    }

    private static String convertToJSON(List<String> list) {
        if (list != null) {
            JSONObject json = new JSONObject();
            try {
                json.put("itemList", new JSONArray(list));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json.toString();
        }

        return null;
    }

//    public void addQuestion(Question e) {
//        ContentValues values = getContentValues(e);
//        mDatabase.insert(QuestionTable.NAME, null, values);
//    }
//
//    public void deleteQuestion(Question c) {
//        String uuidString = c.getId().toString();
//        mDatabase.delete(QuestionTable.NAME, QuestionTable.Cols.UUID + " = ?",
//                new String[]{uuidString});
//    }

    public List<Question> getQuestions() {
        return getQuestions(null);
    }

    public List<Question> getQuestions(String searchTerm) {
        String queryWhereClause;

        List<Question> entries = new ArrayList<>();

        if (searchTerm != null) {
            queryWhereClause = "BATCH LIKE '%" + searchTerm +
                    "%' OR ORDER LIKE '%"+ searchTerm + "%'";
        } else {
            queryWhereClause = null;
        }

        QuestionCursorWrapper cursor = queryQuestions(queryWhereClause, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                entries.add(cursor.getQuestion());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Collections.sort(entries, new Comparator<Question>() {
            @Override
            public int compare(Question e1, Question e2) {
                return e1.getTriple().compareTo(e2.getTriple());
            }
        });
        return entries;
    }

    public Question getQuestion(UUID id) {
        QuestionCursorWrapper cursor = queryQuestions(
                QuestionTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getQuestion();
        } finally {
            cursor.close();
        }
    }

//    public void updateQuestion(Question Question) {
//        String uuidString = Question.getId().toString();
//        ContentValues values = getContentValues(Question);
//        mDatabase.update(QuestionTable.NAME, values,
//                QuestionTable.Cols.UUID + " = ?",
//                new String[]{uuidString});
//    }

    private QuestionCursorWrapper queryQuestions(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                QuestionTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new QuestionCursorWrapper(cursor);
    }
}
