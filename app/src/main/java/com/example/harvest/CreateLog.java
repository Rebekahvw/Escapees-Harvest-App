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



public class CreateLog extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText logName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button addLog;
    private Button returnHome;
    //from youtube example
    private static final String TAG = "CreateLog";
//
//    private static final String KEY_TITLE = "title";
//    private static final String KEY_DESCRIPTION = "description";
    private CollectionReference usersRef = db.collection("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log);
       // mDatabase = FirebaseDatabase.getInstance().getReference("logs");
     //   mDatabase = FirebaseDatabase.getInstance().getReference();
        logName = (EditText) findViewById(R.id.logName);
        returnHome = (Button)findViewById(R.id.returnHome);
        returnHome.setOnClickListener(view -> {
            Intent intent = new Intent(CreateLog.this , ProfileActivity.class);
            startActivity(intent);
        });

        addLog = (Button) findViewById(R.id.addLog);
        addLog.setOnClickListener(view -> {
            createLog();
        });
    }

    void createLog(){
        String logNamed = logName.getText().toString().trim();

        if (logNamed.isEmpty()){
            logName.setError("Enter log name to proceed ");
            logName.requestFocus();
            return;
        }
//        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
//        String uid = currentFirebaseUser.getUid();
//        Log log = new Log(uid, logName.getText().toString().trim());
//
//        mDatabase.child("logs").setValue(log).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(CreateLog.this, "Log created",Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Toast.makeText(CreateLog.this,"Log creation failed",Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        //define type for the Key (String) and value that we want to pass (object because we can pass different types)
//        HashMap<String , Object> logs = new HashMap<>();//use hashmap which is a specific implementation of the map interface
//        logs.put("logName" , logName.getText().toString().trim());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String timeCreated = formatter.format(date);

        OurLog logs = new OurLog(FirebaseAuth.getInstance().getCurrentUser().getUid(),logName.getText().toString().trim(),timeCreated);


//        db.collection("userLogs").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(logs, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(CreateLog.this, "Log has been created successfully",Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(CreateLog.this , ProfileActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                }
//                else{
//                    Toast.makeText(CreateLog.this, "Log creation failed",Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        //correct, but just checking something
       usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").add(logs)
               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                   @Override
                   public void onSuccess(DocumentReference documentReference) {
                       //  Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                       Toast.makeText(CreateLog.this, "Log added successfully", Toast.LENGTH_LONG).show();
                       logName.getText().clear();
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(CreateLog.this, "Log creation failed", Toast.LENGTH_LONG).show();
                       Log.w(TAG, e.toString());
                   }
               });

    }

}