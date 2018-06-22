package com.example.alex.examgroups;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Controller.Controller;
import Model.User;

/**
 * Created by alex on 13/06/2018.
 */

public class YourProfileActivity extends AppCompatActivity {
    private Controller controller;
    private TextView username, currentExams, totalExams;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_profile);

        username = (TextView) findViewById(R.id.your_username_textView);
        currentExams = (TextView) findViewById(R.id.your_current_exams_textView);
        totalExams = (TextView) findViewById(R.id.your_total_participations_textView);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        userRef= db.getReferenceFromUrl("https://exam-groups.firebaseio.com/");
        retrieveUserData();
    }

    private void retrieveUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        String currentID = user.getUid();
        DatabaseReference myUser = userRef.child("Users").child(currentID);
        myUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Username").getValue(String.class);
                Integer current_exams = dataSnapshot.child("Current exams").getValue(Integer.class);
                Integer total_exams = dataSnapshot.child("Total exam participations").getValue(Integer.class);
                username.setText(name);
                currentExams.setText(current_exams.toString());
                totalExams.setText(total_exams.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}