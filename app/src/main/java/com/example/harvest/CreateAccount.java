package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private static final String TAG = CreateAccount.class.getSimpleName();

    //UI elements
    private TextView registerUser;
    private EditText editTextFullName, editTextUsername, editTextEmail, editTextPassword;

    //firebase and firestore variables
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //initialise firestore variables
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        //initialise UI elements

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextFullName= (EditText) findViewById(R.id.fullname);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextUsername = (EditText) findViewById(R.id.username);

        registerUser = (TextView) findViewById(R.id.createAccount);
        registerUser.setOnClickListener(view -> {
            registerUser();
        });

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();

        //make sure fields aren't empty
        if (fullName.isEmpty()){
            editTextFullName.setError("Full name is a required field");
            editTextFullName.requestFocus();
            return;
        }
        if (username.isEmpty()){
            editTextUsername.setError("Username is a required field");
            editTextUsername.requestFocus();
            return;
        }
        if (email.isEmpty()){
            editTextEmail.setError("Email is a required field");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){//check that string is in email format
            editTextEmail.setError("Invalid email address");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Password is a required field");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextPassword.setError("Password length must be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        //use firebase authentication to make a new user with email and password in database

        mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                //create hashmap with details of new user
                HashMap<String , Object> user = new HashMap<>();
                user.put("fullName" , fullName);
                user.put("email", email);
                user.put("username" , username);
                user.put("id" , mAuth.getCurrentUser().getUid());

                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(CreateAccount.this, "Account created successfully. Please log in", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(CreateAccount.this , MainActivity.class);
                                startActivity(intent);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateAccount.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, e.toString());
                            }
                        });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAccount.this,"Account creation failed",Toast.LENGTH_LONG).show();
            }
        });





    }
}