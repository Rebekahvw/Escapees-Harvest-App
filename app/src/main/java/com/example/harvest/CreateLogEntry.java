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
    private Button addLog;
    private Button returnHome;

    private CollectionReference usersRef = db.collection("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log_entry);

        produceET = (EditText) findViewById(R.id.produceEditText);
        weightET = (EditText) findViewById(R.id.weightEditText);

        returnHome = (Button)findViewById(R.id.returnHome);
        returnHome.setOnClickListener(view -> {
            Intent intent = new Intent(CreateLogEntry.this , ProfileActivity.class);
            startActivity(intent);
        });

        addLog = (Button) findViewById(R.id.addLog);
        addLog.setOnClickListener(view -> {
            createLogEntry();
        });
    }
    void createLogEntry(){
        String produceType = produceET.getText().toString().trim();
        String weightString=weightET.getText().toString().trim();
        float weight = Float.valueOf(weightString);

        if (produceType.isEmpty()){
            produceET.setError("Enter produce type to proceed");
            produceET.requestFocus();
            return;
        }
        if (weightString.isEmpty()){
            produceET.setError("Enter weight of produce to proceed");
            produceET.requestFocus();
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String timeCreated = formatter.format(date);

        String logID=usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").getId();

        LogEntry entry = new LogEntry(FirebaseAuth.getInstance().getCurrentUser().getUid(),produceType,weight,timeCreated);

        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").document(logID).collection("Log entries").add(entry)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //  Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(CreateLogEntry.this, "Log entry added successfully", Toast.LENGTH_LONG).show();
                        produceET.getText().clear();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateLogEntry.this, "Log creation failed", Toast.LENGTH_LONG).show();
                    }
                });

    }
}
