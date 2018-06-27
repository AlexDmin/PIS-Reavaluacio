package com.example.alex.examgroups;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Model.User;

/**
 * Created by alex on 12/06/2018.
 */

public class NewExamActivity extends AppCompatActivity {
    private TextInputEditText name, date, value, classroom, description;
    private Button createExam;
    private FirebaseAuth mAuth;
    private User user;
    private FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exam);
        setTitle("New exam");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Initialization
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

    //Method for creating an exam and adding it into the firebase
    public void createExam() {

        final String userID = mAuth.getCurrentUser().getUid();
        final String examName = name.getText().toString().trim();
        final String examDate = date.getText().toString().trim();
        final String examValue = value.getText().toString().trim();
        final String examClassroom = classroom.getText().toString().trim();
        final String examDescription = description.getText().toString().trim();

        final DatabaseReference newExam = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child("Exams");
        newExam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(examName)){
                    Map newPost = new HashMap();
                    newPost.put("Name", examName);
                    newPost.put("Date", examDate);
                    newPost.put("Value", examValue);
                    newPost.put("Classroom", examClassroom);
                    newPost.put("Description", examDescription);


                    newExam.child(examName).setValue(newPost);
                    newExam.child(examName).child("Users").child(userID).setValue(userID);
                    newExam.child(examName).child("Admin").setValue(userID);
                    Toast.makeText(getApplicationContext(), "Exam created!", Toast.LENGTH_SHORT).show();

                }
                if(!FriendListActivity.getActive() == true){
                    startMainMenuActivity();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void startMainMenuActivity(){
        ExamInfoActivity.setActive(false);
        finish();
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

}
