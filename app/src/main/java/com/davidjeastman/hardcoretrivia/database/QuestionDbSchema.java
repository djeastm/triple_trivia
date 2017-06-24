package com.davidjeastman.hardcoretrivia.database;

/**
 * Created by David Eastman on 6/24/2017.
 */

public class QuestionDbSchema {
    public static final class QuestionTable {
        public static final String NAME = "questions";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String BATCH = "batch";
            public static final String ORDER = "order";
            public static final String CORRECT_ANSWER = "correct_answer";
            public static final String ANSWER2 = "answer2";
            public static final String ANSWER3 = "answer3";
            public static final String ANSWER4 = "answer4";
            public static final String QUESTION = "question";

        }
    }
}
