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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText first_name;
    EditText last_name;
    EditText email;
    EditText password;
    EditText confirm_password;

    String user_id;

    FirebaseDatabase db;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference().child("users");

        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);

    }

    public void signUpPressed(View v){

        if (password.getText() != confirm_password.getText()){
//            Log.e("Password don't match", "");
//            return;
        }

        String email_str = email.getText().toString();
        String password_str = password.getText().toString();
        mAuth.createUserWithEmailAndPassword(email_str, password_str)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            user_id = user.getUid();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        

        if (user == null){
            startActivity(new Intent(this, Login.class));
        }else{
            NewUser();
        }
    }

    private void NewUser(){
        User u = new User(first_name.getText().toString(), last_name.getText().toString(),
                user_id, email.getText().toString(), password.getText().toString(), 1);

        myRef.child(user_id).setValue(u);

        startActivity(new Intent(this, Home.class));
    }
}
