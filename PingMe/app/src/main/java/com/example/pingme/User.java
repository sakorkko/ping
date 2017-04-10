package com.example.pingme;

public class User {

    public String name; // user's name
    public String id; // android_id. Get with Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    public String token; // firebase instance token. Get with FirebaseInstanceId.getInstance().getToken();

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String id, String token) {
        this.name = name;
        this.id = id;
        this.token = token;
    }
}
