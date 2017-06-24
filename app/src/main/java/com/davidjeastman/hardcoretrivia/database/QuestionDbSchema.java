package com.davidjeastman.hardcoretrivia.database;

/**
 * Created by David Eastman on 6/24/2017.
 */

public class QuestionDbSchema {
    public static final class QuestionTable {
        public static final String NAME = "questions";

        public static final class Cols {
            // Built-in
            public static final String UUID = "uuid";
            public static final String TRIPLE = "triple";
            public static final String ORDER = "order";
            public static final String CORRECT_ANSWER = "correct_answer";
            public static final String ANSWER2 = "answer2";
            public static final String ANSWER3 = "answer3";
            public static final String ANSWER4 = "answer4";
            public static final String QUESTION = "question";
            // Player-affected
            public static final String QUESTION_SEEN = "question_seen";
            public static final String PLAYER_CORRECT = "player_correct";
            public static final String PLAYER_ANSWER = "player_answer";
        }
    }
}
