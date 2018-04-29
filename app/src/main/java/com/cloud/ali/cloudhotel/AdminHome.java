package com.cloud.ali.cloudhotel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void logoutBtn(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void AddRoom(View v){
        startActivity(new Intent(this, AddRoom.class));
    }

    public void ViewCooment(View v){
        startActivity(new Intent(this, ViewComment.class));
    }

    public void ProfileView(View v){
        startActivity(new Intent(this, Profile.class));
    }

    public void ViewRoom(View v){
        startActivity(new Intent(this, ViewRooms.class));
    }

    public void ViewUser(View v){
        startActivity(new Intent(this, ViewUsers.class));
    }

}
