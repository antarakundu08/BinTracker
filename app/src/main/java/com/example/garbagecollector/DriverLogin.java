package com.example.garbagecollector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DriverLogin extends AppCompatActivity {

    private EditText user, password;
    private Button login;
    private TextView tv;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        user=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.button);
        tv=findViewById(R.id.textView);
        progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }
    private void Login() {
        String email = user.getText().toString()+"DRIvER@gmail.com";
        String psw = password.getText().toString();
        if (TextUtils.isEmpty(email)) {
            user.setError("Enter username..");
            return;
        } else if (TextUtils.isEmpty(psw)) {
            password.setError("Enter password..");
            return;
        }
        progressDialog.setMessage("Please Wait.. ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        mAuth.signInWithEmailAndPassword(email, psw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DriverLogin.this, "Logged In", Toast.LENGTH_LONG).show();
                            Intent i =new Intent(DriverLogin.this, HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(DriverLogin.this, "Login Failed", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();

                    }
                });
    }
}
