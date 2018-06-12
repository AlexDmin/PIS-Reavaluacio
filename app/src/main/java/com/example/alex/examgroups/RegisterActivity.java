package com.example.alex.examgroups;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import Controller.Controller;

/**
 * Created by alex on 11/06/2018.
 * Allows the user to sign up on the app.
 */

public class RegisterActivity extends AppCompatActivity{
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
