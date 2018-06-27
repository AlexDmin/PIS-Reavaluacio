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
 * Created by alex on 13/06/2018.
 */

public class OldExamsActivity extends AppCompatActivity {
    private ListView oldExamsListView;
    private ArrayList<String> oldExams;
    private ArrayAdapter<String> adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference oldExamsRef;
    private FirebaseDatabase db;
    private Exam oldExam;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_exams);
        setTitle("Old exams list");

        //Initialization
        oldExam = new Exam();
        oldExamsListView = findViewById(R.id.old_exams_listView);
        oldExams = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.exam_info, R.id.exam_info_tv, oldExams);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        oldExamsRef = db.getReference("Old exams");

        //Filling the old exams listView with firebase
        oldExamsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fillOldExamsListView(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Setting listener for the old exams listView
        oldExamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openExamActivity(position);
            }
        });
    }

    //Method for opening the exam selected
    private void openExamActivity(int position) {
        ExamTopicActivity.setActive(false);
        Object exam = oldExamsListView.getItemAtPosition(position);
        final String examName = (String)exam;
        DatabaseReference examRef = db.getReference("Old exams");
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

    //Method for filling the old exams ListView with the firebase info
    private void fillOldExamsListView(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            if(ds.child("Users").hasChild(userID)){
                oldExam = ds.getValue(Exam.class);
                if(!oldExams.contains(oldExam.getName().toString())){
                    oldExams.add(oldExam.getName().toString());
                }
            }
        }
        oldExamsListView.setAdapter(adapter);
    }

    //Method for opening the topics activity
    private void startTopicsActivity(String examName) {
        if(!ExamInfoActivity.getActive() && !ExamTopicsActivity.getActive() && !ExamTopicActivity.getActive() && !FriendListActivity.getActive()){
            Intent intent = new Intent(this, ExamInfoActivity.class);
            Bundle b = new Bundle();
            b.putString("exam", examName);
            b.putString("previousAct", "oldExams");
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}
