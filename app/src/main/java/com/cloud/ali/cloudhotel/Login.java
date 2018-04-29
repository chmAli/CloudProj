package com.cloud.ali.cloudhotel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cloud.ali.cloudhotel.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText email;
    EditText password;

    FirebaseDatabase db;
    DatabaseReference user_ref;

    String u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance();
        user_ref = db.getReference().child("users");

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

    }


    public void Login (View v){
        String email_str = this.email.getText().toString();
        String password_str = this.password.getText().toString();

        mAuth.signInWithEmailAndPassword(email_str, password_str)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            u_id = user.getUid();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    int type;

    private void updateUI(Object user) {
        if (user != null){

            user_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> list=dataSnapshot.getChildren();

                    for (DataSnapshot child:list) {
                        User temp=child.getValue(User.class);
                        if(temp.getU_id().equals(u_id))
                            type = temp.getType();

                        if (type == 1)
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        else
                            startActivity(new Intent(getApplicationContext(), AdminHome.class));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

}
