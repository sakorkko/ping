package com.example.pingme;

/**
 * Created by Joonas on 20.2.2017.
 */

public class MyProfile {

    private String mName;
    private String mId;
    private String[] mFriends;

    public MyProfile(String name, String id, String[] friends){
        mName = name;
        mId = id;
        mFriends = friends;
    }

    public String getName(){
        return mName;
    }

    public String getId(){
        return mId;
    }

    public String[] getFriends(){
        return mFriends;
    }



}
