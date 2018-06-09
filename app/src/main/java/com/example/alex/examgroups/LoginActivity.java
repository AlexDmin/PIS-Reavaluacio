package com.example.alex.examgroups;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import Controller.Controller;

public class LoginActivity extends AppCompatActivity {

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
