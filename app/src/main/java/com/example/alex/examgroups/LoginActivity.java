package com.example.alex.examgroups;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import Controller.Controller;
/**
 * Created by alex on 09/06/2018.
 * Log in screen of the app. Allows the user to log in in the app.
 */
public class LoginActivity extends AppCompatActivity {

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
