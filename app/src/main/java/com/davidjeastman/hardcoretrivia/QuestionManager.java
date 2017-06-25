package com.davidjeastman.hardcoretrivia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.davidjeastman.hardcoretrivia.database.TriviaCursorWrapper;
import com.davidjeastman.hardcoretrivia.database.TriviaDbHelper;
import com.davidjeastman.hardcoretrivia.database.TriviaDbSchema.QuestionTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Eastman on 6/24/2017.
 */

public class QuestionManager {
    public static final String APP_NAME = "HardcoreTrivia";
    public static final String TAG = "QuestionManager";
    public static final String APP_DIRECTORY = APP_NAME;

    private static final int numTriplesInSet = 3;
    private static final double SKILL_BOOST = 0.25;

    private static QuestionManager sQuestionManager;

    private Context mContext;
    private SQLiteDatabase mDatabase;

//    List<Question> questions;

    private QuestionManager(Context context) {
//        questions = getQuestions();
//        sortQuestions(questions);
        mContext = context.getApplicationContext();
        mDatabase = new TriviaDbHelper(mContext)
                .getWritableDatabase();
    }

    public static QuestionManager get(Context context) {
        if (sQuestionManager == null) {
            sQuestionManager = new QuestionManager(context);
        }
        return sQuestionManager;
    }

    private static ContentValues getContentValues(Question Question) {
        ContentValues values = new ContentValues();
        values.put(QuestionTable.Cols._ID, Question.getId());
        values.put(QuestionTable.Cols.UUID, Question.getUUID().toString());
        values.put(QuestionTable.Cols.TRIPLE, Question.getTriple());
        values.put(QuestionTable.Cols.POSITION, Question.getPosition());
        values.put(QuestionTable.Cols.CORRECT_ANSWER, Question.getCorrectAnswer());
        values.put(QuestionTable.Cols.ANSWER2, Question.getAnswer2());
        values.put(QuestionTable.Cols.ANSWER3, Question.getAnswer3());
        values.put(QuestionTable.Cols.ANSWER4, Question.getAnswer4());
        values.put(QuestionTable.Cols.QUESTION, Question.getQuestion());
        values.put(QuestionTable.Cols.DIFFICULTY, Question.getDifficulty());
        values.put(QuestionTable.Cols.QUESTION_SEEN, Question.isQuestionSeen());
        values.put(QuestionTable.Cols.PLAYER_CORRECT, Question.isPlayerCorrect());
        values.put(QuestionTable.Cols.PLAYER_ANSWER, Question.getPlayerAnswer());

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
//        String uuidString = c.getUUID().toString();
//        mDatabase.delete(QuestionTable.NAME, QuestionTable.Cols.UUID + " = ?",
//                new String[]{uuidString});
//    }

//    public List<Question> getQuestions() {
//        List<Question> questions = new ArrayList<>();
//        TriviaCursorWrapper cursor = queryQuestions(null, null);
//        try {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                questions.add(cursor.getQuestion());
//                cursor.moveToNext();
//            }
//        } finally {
//            cursor.close();
//        }
//        return questions;
//    }

//    private static void sortQuestions(List<Question> questions) {
//
//        Collections.sort(questions, new Comparator<Question>() {
//            @Override
//            public int compare(Question o1, Question o2) {
//
//                Integer x1 = o1.getTriple();
//                Integer x2 = o2.getTriple();
//                int sComp = x1.compareTo(x2);
//
//                if (sComp != 0) {
//                    return sComp;
//                } else {
//                    x1 = o1.getPosition();
//                    x2 = o2.getPosition();
//                    return x1.compareTo(x2);
//                }
//            }});
//    }

    public List<Question> getNextTripleSet(double skill) {
        int numQuestions = 3 * numTriplesInSet;
        List<Question> tripleSet = new ArrayList<>(numQuestions);

        TriviaCursorWrapper cursor = queryQuestions(
                QuestionTable.Cols.QUESTION_SEEN + " = ?"
                        + " AND " +
                        QuestionTable.Cols.POSITION + " = ?"
                ,
                new String[]{"false", "1"}
        );
        try {
            if (cursor.getCount() == 0) {
                Log.e(TAG, "No questions");
                return null;
            } else {
                int t = 0; // number of skill appropriate triples
                cursor.moveToFirst();
                while (t < numTriplesInSet) {
                    TriviaCursorWrapper cursor2 = queryQuestions(
                            QuestionTable.Cols.TRIPLE + " = ?",
                            new String[]{String.valueOf(cursor.getQuestion().getTriple())}
                    );
                    int difficultySum = 0;
                    cursor2.moveToFirst();
                    List<Question> tripleCandidates = new ArrayList<>(3);
                    for (int i = 0; i < 3; i++) {
                        tripleCandidates.add(cursor2.getQuestion());
                        difficultySum += cursor2.getQuestion().getDifficulty();
                        cursor2.moveToNext();
                    }
                    double averageDifficulty = (double) difficultySum / 3;
                    Log.i(TAG, String.valueOf(averageDifficulty));
                    if (averageDifficulty <= skill) {
                        tripleSet.addAll(tripleCandidates);
                        t++;
                    }
                    // Otherwise, skip the too-difficult triple and go on to the next
                    if (cursor.isLast()) {
                        Log.e(TAG, "Skill: "+ skill
                                +". Not enough valid triples at this player's skill level. " +
                                        "Boosting skill level by "+SKILL_BOOST+" and restarting.");
                        skill = skill + SKILL_BOOST;
                        tripleSet.clear();
                        t = 0;
                        cursor.moveToFirst();
                        continue;
                    }
                    cursor.moveToNext();
                }
                return tripleSet;
            }
        } finally {
            cursor.close();
        }
    }

    public List<Question> getQuestions(int tripleId) {
        List<Question> questions = new ArrayList<>(3);

        TriviaCursorWrapper cursor = queryQuestions(
                QuestionTable.Cols.TRIPLE + " = ?",
                new String[]{String.valueOf(tripleId)}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            } else if (cursor.getCount() == 3) {
                cursor.moveToFirst();
                questions.add(0, cursor.getQuestion());
                cursor.moveToNext();
                questions.add(1, cursor.getQuestion());
                cursor.moveToNext();
                questions.add(2, cursor.getQuestion());
                return questions;
            } else {
                Log.e(TAG, "Not enough questions");
                return null;
            }
        } finally {
            cursor.close();
        }
    }

    public void updateQuestion(Question Question) {
        String uuidString = Question.getUUID().toString();
        ContentValues values = getContentValues(Question);
        mDatabase.update(QuestionTable.NAME, values,
                QuestionTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private TriviaCursorWrapper queryQuestions(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                QuestionTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new TriviaCursorWrapper(cursor);
    }
}
