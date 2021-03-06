package com.example.pingme;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.maps.model.LatLng;

import com.example.pingme.MapMaker;
import com.example.pingme.Pings;
import com.example.pingme.PingHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.security.AccessController.getContext;


public class StartPage extends AppCompatActivity{

    private MapMaker mapMine;
    PingHandler pingHandler = PingHandler.getInstance();
    private static final String TAG = "StartPage";

    private DatabaseReference usersRef;
    private DatabaseReference pingsRef;

    private FirebaseDatabase mFirebaseInstance;

    private ChildEventListener listener;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private String authUser;

    private String estTime;

    // private String androidId = Settings.Secure.ANDROID_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = R.id.pingMap;


        /*
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("pingReceiver"));
        */


        // subscribe to pings topic
        FirebaseMessaging.getInstance().subscribeToTopic("pings");

        // enable database persistence for offline use

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        // get instance of firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // firebase authentication listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    authUser = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    usersRef = FirebaseDatabase.getInstance().getReference("users");

                    usersRef.child(authUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                String userid = snapshot.getKey();
                                Log.d(TAG, "User found in database, userid: " + userid);
                            } else {
                                Log.d(TAG, "User not found in database, Creating new User");
                                mFirebaseInstance = FirebaseDatabase.getInstance();
                                // get reference to 'users' node
                                usersRef = mFirebaseInstance.getReference("users");

                                createUser();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            System.err.println("Listener was cancelled");
                        }
                    });

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // sign in using anonymous authentication
        signInAnonymously();

        // set databse reference
        pingsRef = FirebaseDatabase.getInstance().getReference("pings");

        // keep offline data in sync with online database
        pingsRef.keepSynced(true);

        // calculate server time offset
        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long offset = snapshot.getValue(Long.class);
                long estimatedServerTimeMs = System.currentTimeMillis() + offset;
                estTime = Long.toString(TimeUnit.MILLISECONDS.toSeconds(estimatedServerTimeMs));
                String offsetString = Long.toString(offset);
                String systemTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";
                Log.d(TAG, "system time: " + systemTime);
                Log.d(TAG, "offset from database time: " + offsetString);
                Log.d(TAG, "estimated real time: " + estTime);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        // database operations for retrieving pings
        listener = pingsRef.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                //Extract data from snapshot to ping
                Ping newPost = snapshot.getValue(Ping.class);

                Log.d(TAG, "timestamp: " + newPost.timestamp);
                Log.d(TAG, "estimated real time: " + estTime);
                // don't show ping if older than week
                if (Long.parseLong(estTime) - Long.parseLong(newPost.timestamp) > 604800){
                    // snapshot.getRef().setValue(null); // use if want to delete instead of just hide
                    Log.d(TAG, "ping " + snapshot.getKey() +" is older than week");
                }
                else {
                    //Location formatting
                    String location = newPost.location;
                    String[] parts = location.split(",");
                    String latitude = parts[0];
                    String longitude = parts[1];
                    LatLng latlong = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                //adds pings to list
                pingHandler.addPing(newPost.title, newPost.body, latlong, snapshot.getKey());
                mapMine.setMarkThere(newPost.title, pingHandler.getNewest().getId(), latlong);

                    Log.d("PingID", snapshot.getKey());
                    Log.d("TALLLA", newPost.title);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (googleServicesWork()){
            setContentView(R.layout.activity_start_page);
            if(!hasPermissions()){              //checks gps permissions
                cansIHasPermissons();
            }

            mapMine = new MapMaker(StartPage.this, true, id);     //sets up map
        }
        else{
            // no map
        }
    }


    @Override
    public void onDestroy() {
        pingsRef.removeEventListener(listener);
        super.onDestroy();
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signInAnonymously() {
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                        }
                    }
                });
        // [END signin_anonymously]
    }

    private void createUser() {

        List<String> friends = Arrays.asList();

        User user = new User("default", authUser, FirebaseInstanceId.getInstance().getToken(), friends);

        usersRef.child(authUser).setValue(user);
    }

    /*

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //This is to receive pings using the Cloudmessaging from firebase
            String action = intent.getAction();
            String pingTitle = intent.getStringExtra("title");
            String pingBody = intent.getStringExtra("body");
            String pingLocation = intent.getStringExtra("location");
            Log.d(TAG, "ping received: \n" + "title: " + pingTitle + " body: " + pingBody);
            String[] latlong =  pingLocation.split(",");
            double pingLatitude = Double.parseDouble(latlong[0]);
            double pingLongitude = Double.parseDouble(latlong[1]);

            LatLng pingPosition = new LatLng(pingLatitude, pingLongitude);

            PingHandler.getInstance().addPing(pingTitle, pingBody, pingPosition);
            mapMine.setMarkThere(pingTitle, PingHandler.getInstance().getNewest().getId(), pingPosition);
        }
    };
    */

    public void openList(View v){
        startActivity(new Intent(StartPage.this, PingList.class));
    }
    public void openCreate(View v) { startActivity(new Intent(StartPage.this, CreatePing.class));
    }



    public boolean googleServicesWork(){        // checks that google services are installed
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int working = api.isGooglePlayServicesAvailable(this);
        if (working == ConnectionResult.SUCCESS){
            return true;
        }
        else if (api.isUserResolvableError(working)){
            Dialog dial = api.getErrorDialog(this, working, 0);
            dial.show();
        }
        else{
            Toast.makeText(this, "I can't find play services. SAD!", Toast.LENGTH_LONG).show();
        }
        return false;
    }





    private boolean hasPermissions(){       //gps permissions
        int result = 0;
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        for (String perms : permissions){
            result = checkCallingOrSelfPermission(perms);
            if (!(result == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void cansIHasPermissons(){      //asks user for gps permissions
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, 123);
        }
    }

    @Override       //leads from canIhasPermissions. handels the results of the permission request
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean granted = true;

        switch (requestCode){
            case 123:
                for (int results : grantResults){
                    granted = granted && (results == PackageManager.PERMISSION_GRANTED);
                }
            default:
                granted = false;
        }
        if (granted){
            //user gave permission
        }
        else{
            Toast.makeText(this, "I can't find you if you don't let me", Toast.LENGTH_LONG).show();
        }
    }

    public void center(View v){

        mapMine.updatePosition();
        mapMine.goTo(mapMine.getGps(), 15);


    }
    public void openProfile(View v){
        startActivity(new Intent(StartPage.this, profile.class));
    }
}