package com.cloud.ali.cloudhotel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void ViewRoom(View v){
        startActivity(new Intent(this, ViewRooms.class));
    }

    public void VieBookRoom(View v){
        startActivity(new Intent(this, ViewBookRooms.class));
    }

    public void RateReviewBtn(View v){
        startActivity(new Intent(this, RateReview.class));
    }

    public void ProfileView(View v){
        startActivity(new Intent(this, Profile.class));
    }

    public void logoutBtn(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

}
