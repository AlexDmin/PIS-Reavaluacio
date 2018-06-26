package com.example.alex.examgroups;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Controller.Controller;

/**
 * Created by alex on 12/06/2018.
 */

public class ExamTopicActivity extends AppCompatActivity {
    private Controller controller;
    private TextInputEditText text;
    private Button edit;
    private TextView title;
    private String topic, exam;
    public static boolean active = false;

    private FirebaseDatabase db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_topic);


        Bundle b = getIntent().getExtras();
        topic = b.getString("topic");
        exam = b.getString("exam");

        text = findViewById(R.id.topic_text_iet);
        text.setEnabled(false);
        title = findViewById(R.id.topic_textView);
        edit = findViewById(R.id.edit_text_button);
        db = FirebaseDatabase.getInstance();
        final DatabaseReference topicRef = db.getReference("Exams").child(exam).child("Topics").child(topic);
        topicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String topic_title = dataSnapshot.getKey();
                String topic_text = dataSnapshot.child("Text").getValue(String.class);
                text.setText(topic_text);
                title.setText(topic_title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.isEnabled()) {
                    edit.setText("Edit");
                    text.setEnabled(false);
                    String newText = text.getText().toString();
                    topicRef.child("Text").setValue(newText);
                }else{
                    text.setEnabled(true);
                    edit.setText("Submit");
                }
            }
        });

    }
    public static boolean getActive() {
        return active;
    }

    public static void setActive(boolean isactive) {
        active = isactive;
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        active = false;
        super.onStop();

    }
}

