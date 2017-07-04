package com.davidjeastman.tufftrivia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.davidjeastman.tufftrivia.database.TriviaCursorWrapper;
import com.davidjeastman.tufftrivia.database.TriviaDbHelper;
import com.davidjeastman.tufftrivia.database.TriviaDbSchema.ProfileTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by David Eastman on 6/25/2017.
 */

public class ProfileManager {
    public static final String APP_NAME = "TuffTrivia";
    public static final String TAG = "ProfileManager";
    public static final String APP_DIRECTORY = APP_NAME;

    private static ProfileManager sProfileManager;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ProfileManager(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TriviaDbHelper(mContext)
                .getWritableDatabase();
    }

    public static ProfileManager get(Context context) {
        if (sProfileManager == null) {
            sProfileManager = new ProfileManager(context);
        }
        return sProfileManager;
    }

    private static ContentValues getContentValues(Profile Profile) {
        ContentValues values = new ContentValues();
        values.put(ProfileTable.Cols.UUID, Profile.getId().toString());
        values.put(ProfileTable.Cols.NAME, Profile.getName());
        values.put(ProfileTable.Cols.LOCATION, Profile.getLocation());
        values.put(ProfileTable.Cols.STAGE, Profile.getStage());
        values.put(ProfileTable.Cols.LEVEL, Profile.getLevel());
        values.put(ProfileTable.Cols.SKILL, Profile.getSkill());
        values.put(ProfileTable.Cols.POINTS, Profile.getPoints());
        values.put(ProfileTable.Cols.RANK, Profile.getRank());

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

    public void addProfile(Profile e) {
        ContentValues values = getContentValues(e);
        mDatabase.insert(ProfileTable.NAME, null, values);
    }

    public void updateProfile(Profile Profile) {
        String uuidString = Profile.getId().toString();
        ContentValues values = getContentValues(Profile);
        mDatabase.update(ProfileTable.NAME, values,
                ProfileTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public Profile getProfile() {

        TriviaCursorWrapper cursor = queryProfiles(
                null,
                new String[0]
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getProfile();
        } finally {
            cursor.close();
        }
    }

    private TriviaCursorWrapper queryProfiles(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ProfileTable.NAME,
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
