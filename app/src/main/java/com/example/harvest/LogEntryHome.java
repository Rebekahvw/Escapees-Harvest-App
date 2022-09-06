package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LogEntryHome extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");//what we wanna add nodes to
    private DocumentReference logRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private CollectionReference logEntries;
    private String logID;
    private TextView entryDisplayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entry_home);
        Intent j = getIntent();
        logID = j.getStringExtra("logID");
        logEntries= db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").document(logID).collection("Log Entries");
        loadLogEntries();
        entryDisplayTextView = (TextView) findViewById(R.id.entryDisplay);
    }
    public void loadLogEntries(){
        logEntries.orderBy("timeCreated", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String entryInfo="";
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            //QuerySnapshot is our whole collection, which has multiple document snapshots
                            //each document snapshot represents a log
                            LogEntry entry = documentSnapshot.toObject(LogEntry.class);

                            //getting the ID of the actual document (so actual log) so we can use it later
                            String docID = entry.getDocumentID();
                            String produceType = entry.getProduceType();
                            String timeCreated=entry.getTimeCreated();
                            float weight = entry.getWeight();
                           // TextView logTV = new TextView(LogEntryHome.this);
                            entryInfo+=produceType+" :"+weight+"kg"+"\n"+"Created: "+timeCreated+"\n\n";
                          //  logTV.setText(entryInfo);

                        }
                           entryDisplayTextView.setText(entryInfo);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogEntryHome.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}