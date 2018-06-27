package com.example.alex.examgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Model.Exam;

/**
 * Created by alex on 11/06/2018.
 * Displays a list of the exams the user is currently on.
 */

public class YourExamsActivity extends AppCompatActivity {
    private ListView examsList;
    private ArrayList<String> exams;
    private ArrayAdapter<String> adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference examsRef;
    private FirebaseDatabase db;
    private Exam exam;
    public static YourExamsActivity yourExamsActivity;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_exams);
        yourExamsActivity = this;
        setTitle("Active exams");

        //Initialization
        exam = new Exam();
        examsList = findViewById(R.id.your_exams_listView);
        exams = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.exam_info, R.id.exam_info_tv, exams);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        examsRef = db.getReference("Exams");

        //Filling the exams list view
        examsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fillExamsListView(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Setting onClick method for the listView
        examsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openExam(position);
            }
        });
    }

    //Method for opening the exam selected
    private void openExam(int position) {
        ExamTopicActivity.setActive(false);
        Object exam = examsList.getItemAtPosition(position);
        final String examName = (String)exam;
        DatabaseReference examRef = db.getReference("Exams");
        examRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getKey().equals(examName)){
                        startTopicsActivity(examName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    //Method for filling the exams list view with the firebase database information
    private void fillExamsListView(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            if(ds.child("Users").hasChild(userID)){
                exam = ds.getValue(Exam.class);
                if(!exams.contains(exam.getName().toString())){
                    exams.add(exam.getName().toString());
                }
            }
        }
        examsList.setAdapter(adapter);
    }

    //Method for starting the topics activity of the exam selected
    private void startTopicsActivity(String examName) {
        if(!ExamInfoActivity.getActive() && !ExamTopicsActivity.getActive() && !ExamTopicActivity.getActive() && !FriendListActivity.getActive() &&!NewExamActivity.getActive()){
            Intent intent = new Intent(this, ExamInfoActivity.class);
            Bundle b = new Bundle();
            b.putString("exam", examName);
            b.putString("previousAct", "yourExams");
            intent.putExtras(b);
            startActivity(intent);
        }
    }



}
