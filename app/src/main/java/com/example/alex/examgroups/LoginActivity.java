package com.example.alex.examgroups;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import Controller.Controller;
/**
 * Created by alex on 09/06/2018.
 * Log in screen of the app. Allows the user to log in in the app.
 */
public class LoginActivity extends AppCompatActivity {

    private Controller controller;
    private Button registerButton, loginButton;
    private FirebaseAuth mAuth;
    private TextInputEditText emailET, passwordET;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailET = (TextInputEditText) findViewById(R.id.username_textiet);
        passwordET = (TextInputEditText) findViewById(R.id.password_textiet);

        registerButton = (Button) findViewById(R.id.register_screen_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        loginButton = (Button) findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singIn();
            }
        });
    }

    private void singIn() {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        if(email.isEmpty()){
            emailET.setError("Email is empty!");
        }else if(password.isEmpty()){
            passwordET.setError("Password is empty!");
        }else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        openMainMenuActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void openMainMenuActivity() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }


    public void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
