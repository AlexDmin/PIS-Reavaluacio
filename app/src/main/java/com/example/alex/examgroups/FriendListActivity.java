package com.example.alex.examgroups;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class FriendListActivity extends AppCompatActivity{
    private ListView friendList;
    private Button addFriends;
    private ArrayList<String> friends;
    private ArrayAdapter<String> adapter;
    private EditText friendInput;
    private String function, exam, friendKey;
    public static boolean active;

    private FirebaseAuth mAuth;
    private com.google.firebase.database.FirebaseDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        setTitle("Friend list");

        //Getting info from previous activity
        Bundle b = getIntent().getExtras();
        function = b.getString("function");
        exam = b.getString("exam");

        //Initialization
        friendList = findViewById(R.id.friends_list_view);
        addFriends = findViewById(R.id.add_friend_button);
        friends = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.exam_info, R.id.exam_info_tv, friends);
        friendInput = new EditText(this);

        mAuth = FirebaseAuth.getInstance();
        db = com.google.firebase.database.FirebaseDatabase.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        //Filling friends list
        DatabaseReference userFriendList = db.getReference("Users").child(userID).child("Friends");
        userFriendList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fillFriendList(dataSnapshot);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Method for building and using a dialog
        buildAlertDialog();

        //Setting onClickListener for friends list
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onFriendClick(position);
            }
        });

    }

    //This method triggers on listViewClick. It can invite friends to an exam or view a friend's profile.
    private void onFriendClick(int position) {
        final Object friend = friendList.getItemAtPosition(position);
        final String friendName = (String)friend;
        if(function.equals("Add to exam")) {
            DatabaseReference friendRef = db.getReference("Users");
            friendRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        if(ds.child("Username").getValue().equals(friendName)){
                            friendKey = ds.getKey();
                            DatabaseReference examRef = db.getReference("Exams").child(exam).child("Users").child(friendKey);
                            examRef.setValue(friendKey);
                            Toast.makeText(getApplicationContext(), "Friend added to " + exam.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            DatabaseReference friendRef = db.getReference("Users");
            friendRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        if(ds.child("Username").getValue().equals(friendName)){
                            friendKey = ds.getKey();
                            openProfileActivity(friendKey);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    //Method for building and using a dialog for adding a friend
    private void buildAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add firend");
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setMessage("Enter the name of the user");
        builder.setView(friendInput);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String friendUser = friendInput.getText().toString();
                final String userID = mAuth.getCurrentUser().getUid();
                final DatabaseReference userListRef = db.getReference("Users");
                userListRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = false;
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            if(ds.child("Username").getValue().toString().equals(friendUser)){
                                if(!ds.getKey().equals(userID)) {
                                    exists = true;
                                    userListRef.child(userID).child("Friends").child(ds.getKey()).setValue(friendUser);
                                }else{
                                    exists = true;
                                }
                            }
                        }
                        if(!exists){
                            Toast.makeText(getApplicationContext(), "The user does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                friendInput.setText(" ");
                dialog.dismiss();
            }
        });

        final AlertDialog addFriend = builder.create();

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendInput.setText("");
                addFriend.show();
            }
        });

    }

    //Method for filling friend list from firebase
    private void fillFriendList(DataSnapshot dataSnapshot) {
        adapter.clear();
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            String friendUsername = ds.getValue(String.class);
            friends.add(friendUsername);
        }
        friendList.setAdapter(adapter);
    }

    //Method for opening profile activity
    public void openProfileActivity(String key){
        Intent intent = new Intent(this, YourProfileActivity.class);
        Bundle b = new Bundle();
        b.putString("previousAct", key);
        intent.putExtras(b);
        startActivity(intent);
    }



    public static boolean getActive() {
        return active;
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }
}
