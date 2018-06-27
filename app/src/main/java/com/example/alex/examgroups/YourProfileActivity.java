package com.example.alex.examgroups;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * Created by alex on 13/06/2018.
 */

public class YourProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    private TextView username, email;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private ImageView profilePic;
    private FirebaseDatabase db;
    private StorageReference storageRef;
    private String userName = "";
    private String userkey;
    private Uri pathFile;
    private String previousAct, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_profile);
        setTitle("Profile");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Getting info from the previous activity
        Bundle b = getIntent().getExtras();
        previousAct = b.getString("previousAct");

        username = (TextView) findViewById(R.id.your_username_textView);
        email = (TextView) findViewById(R.id.your_email_textView);
        profilePic = findViewById(R.id.profile_imageView);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        userRef= db.getReferenceFromUrl("https://exam-groups.firebaseio.com/");

        //Retrieving the user data
        retrieveUserData();

        //Knowing if the user wants to open his profile or a friend profile
        getUser();

        //Getting the username
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

        //Method for selecting a profile pic
        setProfilePic();
    }

    //Method for selecting a profile pic
    private void setProfilePic() {
        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("images/"+ userkey + ".jpg");

        if(profileRef != null) {
            final long ONE_MEGABYTE = 1024 * 1024;
            profileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    profilePic.setImageBitmap(Bitmap.createScaledBitmap(bmp, profilePic.getWidth(),
                            profilePic.getHeight(), false));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(previousAct.equals("MainMenu")) {
                    showFileChooser();
                }
            }
        });
    }

    //Method for knowing if the user wants to open his profile or a friend profile
    private void getUser() {
        if(previousAct.equals("MainMenu")) {
            userkey = mAuth.getCurrentUser().getUid();
        }else{
            userkey = previousAct;
        }
        userID = mAuth.getCurrentUser().getUid();
    }

    //Method for showing the file chooser to the user
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an image for profile pic"), PICK_IMAGE_REQUEST);
    }

    //Method for uploading the profile pic to de app
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

    //Method for uploading the profile pic to de firebase
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

    //Method for getting user data
    private void retrieveUserData() {
        String currentID;
        if(previousAct.equals("MainMenu")){
            currentID = mAuth.getCurrentUser().getUid();
        }else{
            currentID = previousAct;
        }
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