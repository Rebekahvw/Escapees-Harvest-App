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

    private Button returnHome;
    private Button addEntry;
    private TextView logDisplayTextView;
    private String logID;
    //Create private variables for firebase user and for database reference, so that we reference which "table" we are modifying
    private LinearLayout entryLayout;
    //Attempts to fetch and display logs using firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");//what we wanna add nodes to
    private DocumentReference logRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private CollectionReference allLogsRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs");
  // private CollectionReference allEntriesRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").document(logID).collection("Log Entries");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entry_home);
       // Intent j = getIntent();
       // String logID = j.getStringExtra("logID");
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
      //  LinearLayout pageLayout = (LinearLayout) findViewById(R.id.pageLayout);
        logDisplayTextView = (TextView) findViewById(R.id.logDisplay); //textView to list logs

        //adding a linear layout dynamically so we can add textviews to it
     //   entryLayout=new LinearLayout(this);
     //   entryLayout.setOrientation(LinearLayout.VERTICAL);
     //   pageLayout.addView(entryLayout);

        loadLogEntries(logID);
    }

    //for now, filter latest on top. but later, use the below line of code with it's ascending counterpart
    //in a switch so that the user can choose how to order the results
    // Query.Direction order = Query.Direction.DESCENDING;
    public void loadLogEntries(String logID){
        allLogsRef.document(logID).collection("Log Entries").orderBy("timeCreated", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String entryInfo="";
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            //QuerySnapshot is our whole collection, which has multiple document snapshots
                            //each document snapshot represents a log
                            LogEntry log = documentSnapshot.toObject(LogEntry.class);

                            //getting the ID of the actual document (so actual log) so we can use it later
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
                        // Log.
                    }
                });
    }
}