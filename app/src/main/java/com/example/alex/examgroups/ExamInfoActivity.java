package com.example.alex.examgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Controller.Controller;

import static android.view.View.GONE;

/**
 * Created by alex on 11/06/2018.
 * Shows information of the exam.
 */

public class ExamInfoActivity extends AppCompatActivity {
    private Controller controller;
    private TextView name, date, value, classroom, description;
    private Button viewTopics, setExamAsComplete, invite;
    private String examName, previousAct;
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    public static boolean active = false;
    DatabaseReference examRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_info);

        Bundle b = getIntent().getExtras();
        examName = b.getString("exam");
        previousAct = b.getString("previousAct");


        name = findViewById(R.id.exam_name_textView);
        date = findViewById(R.id.exam_date_textView);
        value = findViewById(R.id.exam_value_textView);
        classroom = findViewById(R.id.exam_classroom_textView);
        description = findViewById(R.id.exam_description_textView);
        viewTopics = findViewById(R.id.view_topics_button);
        setExamAsComplete = findViewById(R.id.set_exam_as_complete_button);
        invite = findViewById(R.id.add_friends_to_exam_button);

        mAuth = FirebaseAuth.getInstance();
        final String userID = mAuth.getCurrentUser().getUid();

        db = FirebaseDatabase.getInstance();
        DatabaseReference adminRef = db.getReference("Exams").child(examName).child("Admin");
        if(active == true) {
            adminRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.getValue().equals(userID)) {
                        setExamAsComplete.setVisibility(GONE);
                        invite.setVisibility(GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }




        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendListActivity();
            }
        });

        if(previousAct.equals("oldExams")) {
            setExamAsComplete.setVisibility(GONE);
            invite.setVisibility(GONE);
            examRef = db.getReference("Old exams").child(examName);
        }else{
            examRef = db.getReference("Exams").child(examName);
        }

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

        setExamAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference fromExams = db.getReference("Exams").child(examName);
                final DatabaseReference toOldExams = db.getReference().child("Old exams").child(examName);

                fromExams.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        fromExams.child("Admin").setValue("");
                        toOldExams.setValue(dataSnapshot.getValue());
                        fromExams.removeValue();
                        YourExamsActivity.yourExamsActivity.finish();
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void openFriendListActivity() {
        if(!FriendListActivity.getActive()) {
            Intent intent = new Intent(this, FriendListActivity.class);
            Bundle b = new Bundle();
            b.putString("function", "Add to exam");
            b.putString("exam", examName);
            intent.putExtras(b);
            startActivity(intent);
        }
    }


    private void startTopicsActivity() {
        if(!ExamTopicsActivity.getActive() && !ExamTopicActivity.getActive()) {
            Intent intent = new Intent(this, ExamTopicsActivity.class);
            Bundle b = new Bundle();
            b.putString("exam", examName);
            if(previousAct.equals("oldExams")) {
                b.putString("previousAct", "oldExams");
            }else{
                b.putString("previousAct", "yourExams");
            }
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
