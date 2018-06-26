package com.example.alex.examgroups;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import Controller.Controller;

/**
 * Created by alex on 13/06/2018.
 */

public class YourProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    private Controller controller;
    private TextView username, email;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private ImageView profilePic;
    private FirebaseDatabase db;
    private StorageReference storageRef;
    private String userName = "";
    private String userkey;
    private Uri pathFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_profile);

        username = (TextView) findViewById(R.id.your_username_textView);
        email = (TextView) findViewById(R.id.your_email_textView);
        profilePic = findViewById(R.id.profile_imageView);

        mAuth = FirebaseAuth.getInstance();
        userkey = mAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        userRef= db.getReferenceFromUrl("https://exam-groups.firebaseio.com/");
        retrieveUserData();

        String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference user = db.getReference("User").child(userID).child("Username");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        storageRef = FirebaseStorage.getInstance().getReference();
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(username.getText().toString().equals(userName)) {
                    showFileChooser();
                //}else{
                    Toast.makeText(getApplicationContext(), "nope", Toast.LENGTH_SHORT).show();
                //}
            }
        });
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an image for profile pic"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            pathFile = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pathFile);
                profilePic.setImageBitmap(bitmap);
                uploadProfilePic();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProfilePic(){
        if(pathFile != null) {
            StorageReference riversRef = storageRef.child("images/"+ userkey + ".jpg");
            riversRef.putFile(pathFile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "File uploaded!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "An error ocurred!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void retrieveUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        String currentID = user.getUid();
        DatabaseReference myUser = userRef.child("Users").child(currentID);
        myUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Username").getValue(String.class);
                String emailS = dataSnapshot.child("User email").getValue(String.class);
                username.setText(name);
                email.setText(emailS);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}