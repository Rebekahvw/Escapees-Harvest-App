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

public class ProfileActivity extends AppCompatActivity {

    private Button logOut;
    private Button addLog;
    private TextView logDisplayTextView;
    //Create private variables for firebase user and for database reference, so that we reference which "table" we are modifying
    private FirebaseUser user;
    private DatabaseReference reference;
    private LinearLayout logLayout;
    private String userID; //used in database; makes sure we select the right user

    //Attempts to fetch and display logs using firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");//what we wanna add nodes to
    private DocumentReference logRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private CollectionReference allLogsRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logOut = (Button) findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
        addLog = (Button) findViewById(R.id.addLog);
        addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, CreateLog.class));
            }
        });
        LinearLayout pageLayout = (LinearLayout) findViewById(R.id.pageLayout);
        //logDisplayTextView = (TextView) findViewById(R.id.logDisplay); //textView to list logs

        //adding a linear layout dynamically so we can add textviews to it
        logLayout=new LinearLayout(this);
        logLayout.setOrientation(LinearLayout.VERTICAL);
        pageLayout.addView(logLayout);


        loadLogs();
    }

    //for now, filter latest on top. but later, use the below line of code with it's ascending counterpart
    //in a switch so that the user can choose how to order the results
    // Query.Direction order = Query.Direction.DESCENDING;
    public void loadLogs(){
        allLogsRef.orderBy("timeCreated", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String logInfo="";
                            //QuerySnapshot is our whole collection, which has multiple document snapshots
                            //each document snapshot represents a log
                            OurLog log = documentSnapshot.toObject(OurLog.class);

                            //getting the ID of the actual document (so actual log) so we can use it later
                            String docID = log.getDocumentID();
                            String nameOfLog = log.getLogName();
                            String timeCreated=log.getTimeCreated();
                            TextView logTV = new TextView(ProfileActivity.this);
                            logInfo+=nameOfLog+"\n"+"Created: "+timeCreated+"\n\n";
                            logTV.setText(logInfo);
                            logLayout.addView(logTV);
                            logTV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(ProfileActivity.this, LogEntryHome.class);
                                    i.putExtra("logID",docID);
                                    startActivity(i);
                                }
                            });

                        }
                    //    logDisplayTextView.setText(logInfo);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                       // Log.
                    }
                });
    }
}