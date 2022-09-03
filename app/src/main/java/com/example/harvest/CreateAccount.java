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

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = CreateAccount.class.getSimpleName();

    private TextView banner, registerUser;
    private EditText editTextFullName, editTextUsername, editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
// ...
// Initialize Firebase Auth

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        registerUser = (TextView) findViewById(R.id.createAccount);
        registerUser.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextFullName= (EditText) findViewById(R.id.fullname);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextUsername = (EditText) findViewById(R.id.username);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.createAccount:
                registerUser();
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();

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

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
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

//        mAuth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            User user = new User(fullName, username, email);
//
//                            // Write a message to the database
//                            //created a user and a document in the users collection
//                            FirebaseDatabase.getInstance().getReference("Users")
//                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//                                        Toast.makeText(CreateAccount.this, "Account has been created successfully",Toast.LENGTH_LONG).show();
//                                    }
//                                    else{
//                                        Toast.makeText(CreateAccount.this,"Account creation failed",Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//                        }else{
//                            Toast.makeText(CreateAccount.this,"Account creation failed",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });

        mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

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
                              //  Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(CreateAccount.this, "Account created successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CreateAccount.this , ProfileActivity.class);
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

                //adding it to realtime database
//                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(CreateAccount.this, "Account has been created successfully",Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(CreateAccount.this , ProfileActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAccount.this,"Account creation failed",Toast.LENGTH_LONG).show();
            }
        });





    }
}