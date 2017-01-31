package com.example.pingme;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

class AsyncT extends AsyncTask<Void,Void,Void> {

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization", "key=AAAAJyfN94c:APA91bFQLg7EjnglzRT3eSgqobASxYoK0tJBsz5RYDqrH4DvZw8H9994H0VgXknminPWJIoHh7N5RmQOj1HKny3kaWuICys1oiD6sQqGTquQSf5d0oRkV45hRP1GUUt6LjUraWuHZAd4qRUd817Q6SRBvQQOg87HdA");
            httpURLConnection.connect();

            String jsonData = "{\"registration_ids\":[\"epN6RGBUdXg:APA91bG-L7CwMnF2E3zP4IrunF_SOKAa7AYUV3j4Q92IgY9l6_9s4O58weNqi0QQUEAR0zoBl0TUYmSJPrMKgiUZL2CPnMppJctJ17T2-LkQphOUX3dTwn19qTcZHxfLXQPLvuXAoVrq\"],\"notification\":{\"title\":\"test\",\"body\":\"Test Message Using POST from app\"},\"priority\":10}";

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