package com.example.alex.examgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Controller.Controller;
import Model.User;

/**
 * Created by alex on 12/06/2018.
 */

public class NewExamActivity extends AppCompatActivity {
    private Controller controller;
    private TextInputEditText name, date, value, classroom, description;
    private Button createExam;
    private FirebaseAuth mAuth;
    private User user;
    private FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exam);

        name = findViewById(R.id.new_exam_nameiet);
        date = findViewById(R.id.new_exam_dateiet);
        value = findViewById(R.id.new_exam_valueiet);
        classroom = findViewById(R.id.new_exam_classiet);
        description = findViewById(R.id.new_exam_descriptioniet);
        createExam = (Button) findViewById(R.id.create_exam_button);
        createExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExam();
            }
        });

        user = new User();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

    }
    public void createExam() {

        final String userID = mAuth.getCurrentUser().getUid();
        String examName = name.getText().toString().trim();
        String examDate = date.getText().toString().trim();
        String examValue = value.getText().toString().trim();
        String examClassroom = classroom.getText().toString().trim();
        String examDescription = description.getText().toString().trim();

        DatabaseReference newExam = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child("Exams").child(examName);

        Map newPost = new HashMap();
        newPost.put("Name", examName);
        newPost.put("Date", examDate);
        newPost.put("Value", examValue);
        newPost.put("Classroom", examClassroom);
        newPost.put("Description", examDescription);


        newExam.setValue(newPost);
        newExam.child("Users").child(userID).setValue(userID);

        finish();
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

}
