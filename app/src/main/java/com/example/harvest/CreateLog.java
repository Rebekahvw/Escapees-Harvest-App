package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateLog extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText logName;

    private Button addLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log);
       // mDatabase = FirebaseDatabase.getInstance().getReference("logs");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        logName = (EditText) findViewById(R.id.logName);

        addLog = (Button) findViewById(R.id.addLog);
        addLog.setOnClickListener(view -> {
            createLog();
        });
    }

    void createLog(){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String uid = currentFirebaseUser.getUid();
        Log log = new Log(uid, logName.getText().toString().trim());

        mDatabase.child("logs").setValue(log).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CreateLog.this, "Log created",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(CreateLog.this,"Log creation failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}