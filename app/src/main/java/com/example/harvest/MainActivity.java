package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView register, login;
    private EditText editTextLoginEmail, editTextLoginPassword;
   // private Button logIn;
    private FirebaseAuth mAuth;
    private TextView forgotPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        //initialise UI elements and set onclick listeners
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        login = findViewById(R.id.login);
        login.setOnClickListener(view -> {
            loginUser();
                });

        editTextLoginEmail = (EditText) findViewById(R.id.loginEmail);
        editTextLoginPassword = (EditText) findViewById(R.id.loginPassword);

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.register:
                startActivity(new Intent(MainActivity.this, CreateAccount.class));
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
                break;

        }
    }

    //method for verifying details of a user trying to sign in
    private void loginUser(){
        String email = editTextLoginEmail.getText().toString().trim();
        String password = editTextLoginPassword.getText().toString().trim();

        //check that fields arent empty
        if (email.isEmpty()){
            editTextLoginEmail.setError("Email is a required field");
            editTextLoginEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextLoginEmail.setError("Invalid email address");
            editTextLoginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextLoginPassword.setError("Password is a required field");
            editTextLoginPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextLoginPassword.setError("Password length must be at least 6 characters");
            editTextLoginPassword.requestFocus();
            return;
        }

        //firebase authentication
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        //redirect to user profile
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        Toast.makeText(MainActivity.this, "Login successful",Toast.LENGTH_LONG).show();
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email inbox to verify this account", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Login failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}