package com.example.alex.examgroups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewTopicActivity extends AppCompatActivity{
    private TextInputEditText topic;
    private Button createTopic;

    private FirebaseDatabase db;

    public static boolean exists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);
        exists = false;
        topic = findViewById(R.id.new_topic_textiet);
        createTopic = findViewById(R.id.create_topic_button);



        createTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExamTopicActivity.setActive(true);
                final DatabaseReference topicsRef = db.getInstance().getReference().child("Topics");
                final String topicName = topic.getText().toString();
                if (!topicName.isEmpty()) {
                    topicsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(topic.getText().toString())) {
                                topicsRef.child(topicName).child("Text").setValue("");
                                Toast.makeText(getApplicationContext(), "Topic created!", Toast.LENGTH_SHORT).show();
                                exists = true;
                                finish();
                            }else if(!exists){
                                Toast.makeText(getApplicationContext(), "Topic already exists!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Title can't be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
