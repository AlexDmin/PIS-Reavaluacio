package com.example.alex.examgroups;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import Controller.Controller;

public class SettingsActivity extends AppCompatActivity {
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
