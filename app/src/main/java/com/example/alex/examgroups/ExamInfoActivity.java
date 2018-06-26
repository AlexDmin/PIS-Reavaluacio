package com.example.alex.examgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Controller.Controller;

/**
 * Created by alex on 11/06/2018.
 * Shows information of the exam.
 */

public class ExamInfoActivity extends AppCompatActivity {
    private Controller controller;
    private TextView name, date, value, classroom, description;
    private Button viewTopics;
    private String examName;
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    public static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_info);

        Bundle b = getIntent().getExtras();
        examName = b.getString("exam");

        name = findViewById(R.id.exam_name_textView);
        date = findViewById(R.id.exam_date_textView);
        value = findViewById(R.id.exam_value_textView);
        classroom = findViewById(R.id.exam_classroom_textView);
        description = findViewById(R.id.exam_description_textView);
        viewTopics = findViewById(R.id.view_topics_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        DatabaseReference examRef = db.getReference("Exams").child(examName);
        examRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String exam_name = dataSnapshot.child("Name").getValue(String.class);
                String exam_date = dataSnapshot.child("Date").getValue(String.class);
                String exam_value = dataSnapshot.child("Value").getValue(String.class);
                String exam_classroom = dataSnapshot.child("Classroom").getValue(String.class);
                String exam_description = dataSnapshot.child("Description").getValue(String.class);

                name.setText(exam_name);
                date.setText(exam_date);
                value.setText(exam_value);
                classroom.setText(exam_classroom);
                description.setText(exam_description);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTopicsActivity();
            }
        });

    }


    private void startTopicsActivity() {
        if(!ExamTopicsActivity.getActive() && !ExamTopicActivity.getActive()) {
            Intent intent = new Intent(this, ExamTopicsActivity.class);
            Bundle b = new Bundle();
            b.putString("exam", examName);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    public static boolean getActive() {
        return active;
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }
}
