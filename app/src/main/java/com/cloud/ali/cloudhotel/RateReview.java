package com.cloud.ali.cloudhotel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RateReview extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference comment_ref;
    DatabaseReference rating_Ref;

    RatingBar rating;
    EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_review);

        db = FirebaseDatabase.getInstance();
        comment_ref = db.getReference().child("comments");
        rating_Ref = db.getReference().child("ratings");

        comment = findViewById(R.id.comment);
        rating = findViewById(R.id.rating);

    }

    public void SubmitBtn(View v){
        String u_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rating_Ref.child(u_id).setValue(Integer.toString(rating.getNumStars()));
        comment_ref.child(u_id).setValue(comment.getText().toString());

        startActivity(new Intent(RateReview.this, Home .class));

    }

}
