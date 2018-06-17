package com.example.alex.examgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import Controller.Controller;
import Model.FirebaseDatabase;

/**
 * Created by alex on 11/06/2018.
 * Allows the user to sign up on the app.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Controller controller;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText repeatPassword;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (TextInputEditText) findViewById(R.id.email_register_textiet);
        password = (TextInputEditText) findViewById(R.id.password_register_textiet);
        repeatPassword = (TextInputEditText) findViewById(R.id.password_repeat_textiet);
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        String useremail = this.email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String repeatPass = repeatPassword.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()) {
            email.setError("Invalid email!");
        }

        if(pass.length() < 6){
            password.setError("The password is too short!");
        }

        if(pass.equals(repeatPass) && !useremail.isEmpty() && !pass.isEmpty() && !repeatPass.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(useremail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "User registrated succesfully", Toast.LENGTH_SHORT).show();
                        openLoginActivity();
                    }else if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "The user is already registered", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "An error ocurred!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else if (useremail.isEmpty()){
            email.setError("Email is empty!");
        }else if (pass.isEmpty()) {
            password.setError("Password is empty!");
        }else if (repeatPass.isEmpty()) {
            repeatPassword.setError("Password is empty!");
        }else{
            repeatPassword.setError("Passwords doesn't match!");
        }
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void succesfullRegister() {
        Toast.makeText(getApplicationContext(), "User registered succesfully!", Toast.LENGTH_SHORT).show();
    }
}
