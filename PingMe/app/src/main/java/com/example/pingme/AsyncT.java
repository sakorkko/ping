package com.example.pingme;

import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

class AsyncT extends AsyncTask<String,Void,Void> {

    @Override
    protected Void doInBackground(String... pingParams) {

        String text1, text2, pingLocation;
        // Title for ping
        text1 = pingParams[0];

        // Body for ping
        text2 = pingParams[1];

        // Location for ping
        pingLocation = pingParams[2];

        // For sending messages to pings topic. Required for messaging multiple devices as targeting whole app isn't supported on Android
        String myTopic = "/topics/pings";

        // For getting the senders token and passing it with the ping. Needed for targeting replies to the ping's sender
        String myToken = FirebaseInstanceId.getInstance().getToken();

        try {
            // url for  Firebase Cloud Messaging send requests
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // Firebase server key for PingMe. Needed for send requests
            httpURLConnection.setRequestProperty("Authorization", "key=AAAAJyfN94c:APA91bFQLg7EjnglzRT3eSgqobASxYoK0tJBsz5RYDqrH4DvZw8H9994H0VgXknminPWJIoHh7N5RmQOj1HKny3kaWuICys1oiD6sQqGTquQSf5d0oRkV45hRP1GUUt6LjUraWuHZAd4qRUd817Q6SRBvQQOg87HdA");
            httpURLConnection.connect();

            // myTopic should be replaced with targeted person's InstanceID Token
            String jsonData = "{\"to\":\"" + myTopic + "\",\"notification\":{\"title\":\"" + text1 + "\",\"body\":\"" + text2 + "\"},\"data\":{\"location\":\"" + pingLocation + "\",\"token\":\"" + myToken + "\"},\"priority\":10}";

            DataOutputStream outdata = new DataOutputStream(httpURLConnection.getOutputStream());
            outdata.writeBytes( jsonData );
            outdata.flush();
            outdata.close();

            if (httpURLConnection.getResponseCode() != 200) {
                throw new IOException("Bad response: " + httpURLConnection.getResponseCode() + " " + httpURLConnection.getResponseMessage());
            }
            httpURLConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}