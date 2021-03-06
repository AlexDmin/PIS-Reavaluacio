package com.example.alex.examgroups;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 11/06/2018.
 * Allows the user to sign up on the app.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private TextInputEditText email, password, repeatPassword, username;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Initialization
        email = (TextInputEditText) findViewById(R.id.email_register_textiet);
        password = (TextInputEditText) findViewById(R.id.password_register_textiet);
        repeatPassword = (TextInputEditText) findViewById(R.id.password_repeat_textiet);
        username = (TextInputEditText) findViewById(R.id.username_register_textiet);
        registerButton = (Button) findViewById(R.id.register_button);

        //Setting the register button onClick method
        registerButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    //Method for registering the user
    @Override
    public void onClick(View v) {
        String useremail = this.email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String repeatPass = repeatPassword.getText().toString().trim();
        String user = username.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()) {
            email.setError("Invalid email!");
        }

        if (pass.length() < 6) {
            password.setError("The password is too short!");
        }

        if (pass.equals(repeatPass) && !useremail.isEmpty() && !pass.isEmpty() && !repeatPass.isEmpty() && !user.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(useremail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        addUserToNode();
                        Toast.makeText(getApplicationContext(), "User registrated succesfully", Toast.LENGTH_SHORT).show();
                        openLoginActivity();
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "The user is already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "An error ocurred!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else if (useremail.isEmpty()) {
            email.setError("Email is empty!");
        } else if (pass.isEmpty()) {
            password.setError("Password is empty!");
        } else if (repeatPass.isEmpty()) {
            repeatPassword.setError("Password is empty!");
        } else if (user.isEmpty()) {
            username.setError("Username is empty!");
        }else{
            repeatPassword.setError("Passwords doesn't match!");
        }
    }

    //Method for opening the login activity
    public void openLoginActivity() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //Method for adding the user info to firebase
    public void addUserToNode(){
        String userID = mAuth.getCurrentUser().getUid();
        String useremail = email.getText().toString().trim();
        String user = username.getText().toString().trim();

        DatabaseReference current_user = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        Map newPost = new HashMap();
        newPost.put("Username", user);
        newPost.put("User email", useremail);

        current_user.setValue(newPost);

    }

}
