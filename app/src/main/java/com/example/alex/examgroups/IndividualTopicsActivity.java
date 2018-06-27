package com.example.alex.examgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by alex on 13/06/2018.
 */

public class IndividualTopicsActivity extends AppCompatActivity {
    private ListView topicsListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> topicsList;

    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_topics);
        setTitle("Topics list");

        //Initialization
        topicsListView = findViewById(R.id.individual_topics_listView);
        topicsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.exam_info, R.id.exam_info_tv, topicsList);

        db = FirebaseDatabase.getInstance();
        DatabaseReference topicsRef = db.getReference("Topics");
        topicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    topicsList.add(ds.getKey().toString());
                }
                topicsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Setting onClickListener for the listView
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openTopicActivity(position);
            }
        });
    }

    //Method for opening an individual topic onListView click
    private void openTopicActivity(int position) {
        ExamTopicActivity.setActive(false);
        Object topic = topicsListView.getItemAtPosition(position);
        final String topicName = (String)topic;
        DatabaseReference topicRef = db.getReference("Topics");
        topicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getKey().equals(topicName)){
                        startTopicActivity(topicName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    //Method for starting topic activity
    private void startTopicActivity(String topicName) {
        if(!ExamTopicActivity.getActive()){
            Intent intent = new Intent(this, ExamTopicActivity.class);
            Bundle b = new Bundle();
            b.putString("exam", "");
            b.putString("topic", topicName);
            b.putString("previousAct", "individual");
            intent.putExtras(b);
            startActivity(intent);
        }
    }

}
