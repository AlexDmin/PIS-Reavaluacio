package com.example.alex.examgroups;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import Controller.Controller;

/**
 * Created by alex on 12/06/2018.
 */

public class NewExamActivity extends AppCompatActivity {
    private Controller controller;
    private TextInputEditText name, date, value, classroom, description;
    private Button createExam;
    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();

    }
    public void createExam() {

        /*String userID = mAuth.getCurrentUser().getUid();
        String exam_name = name.getText().toString().trim();
        String exam_date = date.getText().toString().trim();
        String exam_value = value.getText().toString().trim();
        String exam_classroom = classroom.getText().toString().trim();
        String exam_description = description.getText().toString().trim();

        DatabaseReference new_Exam = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child("Exams");

        Map newPost = new HashMap();
        newPost.put("Name", exam_name);
        newPost.put("Date", exam_date);
        newPost.put("Value", exam_value);
        newPost.put("Classroom/s", exam_classroom);
        newPost.put("Description", exam_description);

        new_Exam.setValue(newPost);

        new_Exam.child("Users").child(userID);
        Toast.makeText(getApplicationContext(), "Entra", Toast.LENGTH_SHORT).show();
        */

        String userID = mAuth.getCurrentUser().getUid();
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

        newExam.child("Users").push().setValue(userID);
    }

}
