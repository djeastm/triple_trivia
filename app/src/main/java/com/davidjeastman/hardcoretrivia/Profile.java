package com.davidjeastman.hardcoretrivia;

import java.util.UUID;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class Profile {
    private UUID mId;
    private String mName;
    private String mLocation;
    private int mStage;
    private int mLevel;
    private int mSkill;
    private int mPoints;
    private int mRank;

    public Profile() { this(UUID.randomUUID()); }

    public Profile(UUID id) {
        mId = id;
        mName = "John Doe";
        mLocation = "Nowhere";
        mStage = 1;
        mLevel = 1;
        mSkill = 1;
        mPoints = 0;
        mRank = 999;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public int getStage() {
        return mStage;
    }

    public void setStage(int stage) {
        mStage = stage;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public int getSkill() {
        return mSkill;
    }

    public void setSkill(int skill) {
        mSkill = skill;
    }

    public int getPoints() {
        return mPoints;
    }

    public void setPoints(int points) {
        mPoints = points;
    }

    public int getRank() {
        return mRank;
    }

    public void setRank(int rank) {
        mRank = rank;
    }
}
