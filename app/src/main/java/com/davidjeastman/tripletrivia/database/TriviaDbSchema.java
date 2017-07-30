package com.davidjeastman.tripletrivia.database;

/**
 * Created by David Eastman on 6/24/2017.
 */

public class TriviaDbSchema {
    public static final class QuestionTable {
        public static final String NAME = "questions";

        public static final class Cols {
            // Built-in
            public static final String _ID = "_id";
            public static final String UUID = "uuid";
            public static final String TRIPLE = "triple";
            public static final String POSITION = "position";
            public static final String CORRECT_ANSWER = "correct_answer";
            public static final String ANSWER2 = "answer2";
            public static final String ANSWER3 = "answer3";
            public static final String ANSWER4 = "answer4";
            public static final String QUESTION = "question";
            public static final String DIFFICULTY = "difficulty";
            // Player-affected
            public static final String QUESTION_SEEN = "question_seen";
            public static final String PLAYER_CORRECT = "player_correct";
            public static final String PLAYER_ANSWER = "player_answer";
        }
    }
    public static final class ProfileTable {
        public static final String NAME = "profile";

        public static final class Cols {

            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String LOCATION = "location";
            public static final String STAGE = "stage";
            public static final String LEVEL = "level";
            public static final String SKILL = "skill";
            public static final String POINTS = "points";
            public static final String RANK = "rank";

        }
    }
}
