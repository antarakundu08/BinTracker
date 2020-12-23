package com.example.garbagecollector;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private Button logout,loc;
    private TextView textView,counts;
    private FusedLocationProviderClient fusedLocationProviderClient;
    Double lat=79.8904064, longt=23.1615008;
    private String itemsUrl;
    private int countReq = 0;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        loc = findViewById(R.id.loc);
        textView = findViewById(R.id.textView2);
        counts = findViewById(R.id.textView3);

        logout=findViewById(R.id.button2);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        //   NotificationChannel channel = new NotificationChannel("My Notification","Home Activity", NotificationManager.IMPORTANCE_DEFAULT);
        // NotificationManager manager = getSystemService(NotificationManager.class);
        // manager.createNotificationChannel(channel);
        //}

        loc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        //get the location here
                        fusedLocationProviderClient.getLastLocation()
                                .addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if(location != null){
                                            lat = location.getLatitude();
                                            longt = location.getLongitude();

                                            textView.setText(lat+" , "+longt);
                                            Toast.makeText(HomeActivity.this,"Success", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }
                }
            }
        });
        countRequests();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
            }
        });

    }
    private void countRequests() {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        databaseReference.child("Ganga Nagar/Ganga Nagar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                countReq = (int) dataSnapshot.getChildrenCount();
                //Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                counts.setText(Integer.toString(countReq) + " Requests, " + dataSnapshot.getKey());
                if(countReq>1){
                    vibe.vibrate(1000);
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                counts.setText(Integer.toString(countReq) + " Requests, " + dataSnapshot.getKey());
                if(countReq>1){
                    vibe.vibrate(1000);
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                counts.setText(Integer.toString(countReq) + " Requests, " + dataSnapshot.getKey());
                if(countReq>1){
                    vibe.vibrate(1000);
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /**databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (dataSnapshot.exists()) {
        countReq = (int) dataSnapshot.getChildrenCount();
        counts.setText(Integer.toString(countReq) + " Requests");
        } else {
        counts.setText("No Pending Requests");
        }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
        });**/

    }
}
