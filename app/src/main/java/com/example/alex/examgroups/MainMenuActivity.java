package com.example.alex.examgroups;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import Controller.Controller;
/**
 * Created by alex on 09/06/2018.
 * Main menu of the app. Provides the user all the functions.
 */

public class MainMenuActivity extends AppCompatActivity{
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

}
