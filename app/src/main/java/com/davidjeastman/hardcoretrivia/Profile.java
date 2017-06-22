package com.davidjeastman.hardcoretrivia;

import java.util.UUID;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class Profile {
    private UUID mId;
    private String mName;

    public Profile() {
        mId = UUID.randomUUID();
        mName = "John Doe";

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
}
