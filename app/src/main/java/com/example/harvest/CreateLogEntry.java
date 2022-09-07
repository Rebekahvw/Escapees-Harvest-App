package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;



public class CreateLogEntry extends AppCompatActivity {

    private EditText produceET;
    private EditText weightET;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button addLogEntry;
    private Button returnHome;
    private Button seeEntries;
    private CollectionReference usersRef = db.collection("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log_entry);

        Intent j = getIntent();
        String logID = j.getStringExtra("logID");

        //initialise UI elements and set their onclicklisteners
        produceET = (EditText) findViewById(R.id.produceET);
        weightET = (EditText) findViewById(R.id.weightET);

        //go to activity that displays log entries
        seeEntries=(Button)findViewById(R.id.goToLogEntries);
        seeEntries.setOnClickListener(view -> {
            Intent intent = new Intent(CreateLogEntry.this , LogEntryHome.class);
            startActivity(intent);
        });

        //go to profile
        returnHome = (Button)findViewById(R.id.returnHome);
        returnHome.setOnClickListener(view -> {
            Intent intent = new Intent(CreateLogEntry.this , ProfileActivity.class);
            startActivity(intent);
        });

        //add info to firestore
        addLogEntry = (Button) findViewById(R.id.addLogEntry);
        addLogEntry.setOnClickListener(view -> {
            createLogEntry(logID);
        });
    }

    void createLogEntry(String logID){
        String produce = produceET.getText().toString().trim();
        String weight = weightET.getText().toString().trim();

        //make sure fields arent empty
        if (produce.isEmpty()){
            produceET.setError("Enter produce type");
            produceET.requestFocus();
            return;
        }

        if (weight.isEmpty()){
            weightET.setError("Enter produce weight");
            weightET.requestFocus();
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String timeCreated = formatter.format(date);


        LogEntry entry = new LogEntry(FirebaseAuth.getInstance().getCurrentUser().getUid(),produce,weight,timeCreated);
        String ID = (usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())).getId();

        //create log entry in firestore
        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").document(ID).collection("Log Entries").add(entry)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //  Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(CreateLogEntry.this, "Log entry added successfully", Toast.LENGTH_LONG).show();
                        produceET.getText().clear();
                        weightET.getText().clear();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateLogEntry.this, "Log entry creation failed", Toast.LENGTH_LONG).show();
                    }
                });

    }

}