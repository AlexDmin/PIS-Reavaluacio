package com.example.alex.examgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by alex on 09/06/2018.
 * Main menu of the app. Provides the user all the functions.
 */

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    private Button myExams, newExam, topics, newTopic, oldExams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Initialization
        findViewById(R.id.your_exams_button).setOnClickListener(this);
        findViewById(R.id.new_exam_button).setOnClickListener(this);
        findViewById(R.id.topics_button).setOnClickListener(this);
        findViewById(R.id.new_topic_button).setOnClickListener(this);
        findViewById(R.id.old_exams_button).setOnClickListener(this);
        ExamInfoActivity.setActive(false);

        mAuth = FirebaseAuth.getInstance();
    }

    //Method for creating an options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Method that triggers when the user selects an option. It opens the necessary activity.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.profile) {
            Intent intent = new Intent(this, YourProfileActivity.class);
            Bundle b = new Bundle();
            b.putString("previousAct", "MainMenu");
            intent.putExtras(b);
            startActivity(intent);
        }else if(item.getItemId() == R.id.friends) {
            Intent intent = new Intent(this, FriendListActivity.class);
            Bundle b = new Bundle();
            b.putString("function", "Add friends");
            b.putString("exam", "");
            intent.putExtras(b);
            startActivity(intent);
        }else{
            mAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //Method that triggers when the user clicks on a button from the main menu activity. It opens the necessary activity.
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
                startActivity(new Intent(this, IndividualTopicsActivity.class));
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
