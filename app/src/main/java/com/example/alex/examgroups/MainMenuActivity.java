package com.example.alex.examgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import Controller.Controller;
/**
 * Created by alex on 09/06/2018.
 * Main menu of the app. Provides the user all the functions.
 */

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{
    private Controller controller;
    FirebaseAuth mAuth;
    private Button myExams, newExam, topics, newTopic, oldExams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        findViewById(R.id.your_exams_button).setOnClickListener(this);
        findViewById(R.id.new_exam_button).setOnClickListener(this);
        findViewById(R.id.topics_button).setOnClickListener(this);
        findViewById(R.id.new_topic_button).setOnClickListener(this);
        findViewById(R.id.old_exams_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.profile) {
            Intent intent = new Intent(this, YourProfileActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.friends) {
            Intent intent = new Intent(this, FriendListActivity.class);
            startActivity(intent);
        }else{
            mAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.your_exams_button:
                startActivity(new Intent(this, YourExamsActivity.class));
                break;
            case R.id.new_exam_button:
                startActivity(new Intent(this, NewExamActivity.class));
                break;
            case R.id.topics_button:
                startActivity(new Intent(this, SearchByTopicActivity.class));
                break;
            case R.id.new_topic_button:
                startActivity(new Intent(this, NewTopicActivity.class));
                break;
            case R.id.old_exams_button:
                startActivity(new Intent(this, OldExamsActivity.class));
                break;
        }
    }
}
