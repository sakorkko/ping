package com.example.pingme;

import com.google.firebase.database.IgnoreExtraProperties;

public class Ping {

        public String title;
        public String body;
        public String location;
        public String sender;
        public String timestamp;

        // Default constructor required for calls to
        // DataSnapshot.getValue(User.class)
        public Ping() {
        }

        public Ping(String title, String body, String location, String sender, String timestamp) {
            this.title = title;
            this.body = body;
            this.location = location;
            this.sender = sender;
            this.timestamp = timestamp;
        }
    }
