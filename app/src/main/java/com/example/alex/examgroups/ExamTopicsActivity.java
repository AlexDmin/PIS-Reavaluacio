package com.example.alex.examgroups;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Controller.Controller;

import static android.view.View.GONE;

/**
 * Created by alex on 12/06/2018.
 */

public class ExamTopicsActivity extends AppCompatActivity{
    private Controller controller;
    private ListView topicsListView;
    private Button addTopic;
    private ArrayList<String> topics;
    private ArrayAdapter<String> adapter;
    private EditText topicInput;
    private String exam, previousAct;
    public static boolean active = false;


    private com.google.firebase.database.FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_topics);

        Bundle b = getIntent().getExtras();
        exam = b.getString("exam");
        previousAct = b.getString("previousAct");

        topicsListView = findViewById(R.id.exam_topics_ListView);
        addTopic = findViewById(R.id.add_topic_button);
        db = com.google.firebase.database.FirebaseDatabase.getInstance();
        topics = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.exam_info, R.id.exam_info_tv, topics);
        topicInput = new EditText(this);

        if(previousAct.equals("oldExams")){
            addTopic.setVisibility(GONE);
        }

        DatabaseReference topicsList = db.getReference("Exams").child(exam).child("Topics");
        topicsList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String topicName = ds.getKey();
                    topics.add(topicName);
                }
                topicsListView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExamTopicActivity.setActive(false);
                Object topic = topicsListView.getItemAtPosition(position);
                final String topicName = (String)topic;
                DatabaseReference examRef = db.getReference("Exams").child(exam).child("Topics");
                examRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.getKey().equals(topicName)) {
                                startTopicActivity(topicName);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add topic");
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setMessage("Enter the name of the topic");
        builder.setView(topicInput);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final String topicName = topicInput.getText().toString();
                final DatabaseReference topicListRef = db.getReference("Exams").child(exam).child("Topics");
                topicListRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChild(topicName)){
                            Toast.makeText(getApplicationContext(), "Topic added", Toast.LENGTH_SHORT).show();
                            topicListRef.child(topicName).child("Text").setValue("");
                            ExamTopicActivity.setActive(true);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                topicInput.setText(" ");
                dialog.dismiss();
            }
        });

        final AlertDialog addTopicD = builder.create();

        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topicInput.setText("");
                addTopicD.show();
            }
        });
    }

    private void startTopicActivity(String topicName) {
        if (!ExamTopicActivity.getActive()) {
            Intent intent = new Intent(this, ExamTopicActivity.class);
            Bundle b = new Bundle();
            b.putString("topic", topicName);
            b.putString("exam", exam);
            if(previousAct.equals("oldExams")) {
                b.putString("previousAct", "oldExams");
            }else{
                b.putString("previousAct", "yourExams");
            }
            intent.putExtras(b);
            startActivity(intent);
        }


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

    public static boolean getActive() {
        return active;
    }

}
