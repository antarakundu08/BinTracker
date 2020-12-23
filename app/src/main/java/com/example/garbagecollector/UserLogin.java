package com.example.garbagecollector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class UserLogin extends AppCompatActivity {

    private static final int MY_REQUEST_CODE=7117;
    List<AuthUI.IdpConfig> providers;
    Button b,bb,a;
    String uid;
    Double lat,longt;
    private FusedLocationProviderClient fusedLocationProviderClient;
    Double ulat, ulongt;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        b=(Button)findViewById(R.id.button);
        bb = (Button)findViewById(R.id.button2);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(UserLogin.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                b.setEnabled(false);
                                showSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserLogin.this, ""+e.getMessage(), LENGTH_SHORT).show();
                    }
                });
            }
        });
        providers= Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace();
            }
        });


        showSignInOptions();

    }
    public void btnCurrentLocation(View view){
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        startActivity(new Intent(this, MapsActivity.class));

    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers).build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==MY_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode==RESULT_OK)
            {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                Toast.makeText(this, ""+user.getEmail(), LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, ""+response.getError().getMessage(), LENGTH_SHORT).show();
            }
        }
    }
    private void trace() {

        databaseReference.child("Ganga Nagar/driver").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                //et.setText(dataSnapshot.getValue().getClass().toString());
                HashMap<String, Double> hmap2 = new HashMap<String, Double>();
                hmap2.putAll((Map<? extends String, ? extends Double>) dataSnapshot.getValue());
                //et.setText((hmap2.get("latitude")).toString()+"  "+(hmap2.get("longitude")).toString());
                lat=hmap2.get("latitude");
                longt=hmap2.get("longitude");
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        //get the location here
                        fusedLocationProviderClient.getLastLocation()
                                .addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if(location != null){
                                            ulat = location.getLatitude();
                                            ulongt = location.getLongitude();
                                            //uet.setText("Your location-"+ulat+" "+ulongt);
                                        }
                                    }
                                });
                    }else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }
                }

                //det.setText("Driver's location-"+lat+" "+longt);
                final Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                "http://maps.google.com/maps?" +
                                        "saddr="+ulat+","+ulongt+"&daddr="+lat+","+longt));
                intent.setClassName(
                        "com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");
                startActivity(intent);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                HashMap<String, Double> hmap2 = new HashMap<String, Double>();
                hmap2.putAll((Map<? extends String, ? extends Double>) dataSnapshot.getValue());
                //et.setText((hmap2.get("latitude")).toString()+"  "+(hmap2.get("longitude")).toString());
                lat=hmap2.get("latitude");
                longt=hmap2.get("longitude");
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

    }

}
