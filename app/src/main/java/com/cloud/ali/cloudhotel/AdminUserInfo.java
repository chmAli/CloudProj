package com.cloud.ali.cloudhotel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cloud.ali.cloudhotel.models.Room;
import com.cloud.ali.cloudhotel.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminUserInfo extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference user_ref;

    String email_check;
    User user;

    TextView first_name;
    TextView last_name;
    TextView email;

    ArrayList<User> users_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_info);

        users_list = new ArrayList<>();

        Intent i = getIntent();
        email_check = i.getStringExtra("email");

        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);

        db = FirebaseDatabase.getInstance();
        user_ref = db.getReference().child("users");

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> list=dataSnapshot.getChildren();

                Log.d("intent name", email_check);

                for (DataSnapshot child:list) {
                    User temp = child.getValue(User.class);
                    if (temp.getEmail().equals(email_check)) {
                        user = temp;
                        break;
                    }
                }

                if (user != null) {
                    first_name.setText(user.getFirst_name());
                    last_name.setText(user.getLast_name());
                    email.setText(user.getEmail());
                }
                else
                    first_name.setText("No Person");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void DeleteUser(View v){

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                users_list.clear();

                for (DataSnapshot child:list) {
                    User new_user = child.getValue(User.class);
                    users_list.add(new_user);
                }

                Log.d("check email", email_check);
                Log.d("loop size", Integer.toString(users_list.size()));

                for (int i=0; i<users_list.size(); ++i){

                    Log.d("i value", Integer.toString(i));
                    String email_test = users_list.get(i).getEmail();
                    Log.d("loop email", email_test);


                    if (email_test.equals(email_check)){
                        Log.d("in loop","test");
//                        users_list.remove(i);

                        user_ref.child(users_list.get(i).getU_id()).removeValue();

                        break;
                    }
                }

                //UpdateUserDatabase(users_list);

                startActivity(new Intent(AdminUserInfo.this, ViewUsers.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void UpdateUserDatabase(ArrayList<User> var){

        db.getReference().child("users").setValue("");

        Log.d("var size", Integer.toString(var.size()));

        for (int i=0; i<var.size(); ++i){

            db.getReference().child("users").child(var.get(i).getU_id()).setValue(var.get(i));

        }
    }
}
