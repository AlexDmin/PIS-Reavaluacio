package com.example.alex.examgroups;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by alex on 12/06/2018.
 */

public class ExamTopicActivity extends AppCompatActivity {
    private TextInputEditText text;
    private Button edit;
    private TextView title;
    private String topic, exam, previousAct;
    private DatabaseReference topicRef;
    public static boolean active = false;

    private FirebaseDatabase db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_topic);
        setTitle("Exam topic");

        //Getting info from previous activity
        Bundle b = getIntent().getExtras();
        topic = b.getString("topic");
        exam = b.getString("exam");
        previousAct = b.getString("previousAct");

        //Initialization
        text = findViewById(R.id.topic_text_iet);
        text.setEnabled(false);

        title = findViewById(R.id.topic_textView);
        edit = findViewById(R.id.edit_text_button);
        db = FirebaseDatabase.getInstance();

        //Setting visibility of buttons in function of the previous activity
        if(previousAct.equals("oldExams")){
            edit.setVisibility(View.GONE);
        }

        //Getting topic data from firebase and calling method for setting content
        if(previousAct.equals("oldExams")){
            topicRef = db.getReference("Old exams").child(exam).child("Topics").child(topic);
        }else if(previousAct.equals("yourExams")){
            topicRef = db.getReference("Exams").child(exam).child("Topics").child(topic);
        }else{
            topicRef = db.getReference("Topics").child(topic);
        }

        topicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setEditTextContent(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ////Setting edit button onClickListener
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableOrDisableTextInputEditText(topicRef);
            }
        });

    }

    //Method for enabling or disabling the inputEditText
    private void enableOrDisableTextInputEditText(DatabaseReference topicRef) {
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

    //Method for setting editText info
    private void setEditTextContent(DataSnapshot dataSnapshot) {
        String topic_title = dataSnapshot.getKey();
        String topic_text = dataSnapshot.child("Text").getValue(String.class);
        text.setText(topic_text);
        title.setText(topic_title);
    }


    //Methods for controlling current state of the activity
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

