package com.example.alex.examgroups;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import Controller.Controller;

/**
 * Created by alex on 11/06/2018.
 * Displays a list of the exams the user is currently on.
 */

public class YourExamsActivity extends AppCompatActivity {
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
