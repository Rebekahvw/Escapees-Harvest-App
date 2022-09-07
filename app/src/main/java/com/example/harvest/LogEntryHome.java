package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LogEntryHome extends AppCompatActivity {

    //UI elements
    private Button returnHome;
    private Button addEntry;
    private TextView logDisplayTextView;

    //firestore database, documents, and collections
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");//what we wanna add nodes to
    private DocumentReference logRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private CollectionReference allLogsRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entry_home);

        String logID = (usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())).getId();
        returnHome = (Button) findViewById(R.id.returnHome);

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogEntryHome.this, ProfileActivity.class));
            }
        });

        addEntry = (Button) findViewById(R.id.addLogEntry);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogEntryHome.this, CreateLogEntry.class));
            }
        });

        logDisplayTextView = (TextView) findViewById(R.id.logDisplay); //textView to list logs

        loadLogEntries(logID);
    }

    //fetch and display log entries, filter latest on top

    public void loadLogEntries(String logID){
        allLogsRef.document(logID).collection("Log Entries").orderBy("timeCreated", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String entryInfo="";
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            //QuerySnapshot is our whole collection, which has multiple document snapshots
                            //each document snapshot represents a log entry
                            LogEntry log = documentSnapshot.toObject(LogEntry.class);

                            //getting the ID of the actual document
                            String docID = log.getDocumentID();
                            String produceType = log.getProduceType();
                            String weight = log.getWeight();
                            String timeCreated=log.getTimeCreated();
                            entryInfo+="Produce Type: "+produceType+"\n"+"Weight: "+weight+"\n"+"Log created: "+timeCreated+"\n\n";

                        }
                        logDisplayTextView.setText(entryInfo);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogEntryHome.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}