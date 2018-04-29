package com.cloud.ali.cloudhotel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cloud.ali.cloudhotel.models.Room;
import com.cloud.ali.cloudhotel.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewUsers extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference user_ref;

    ArrayList<User> users_list;
    ArrayAdapter user_adapter;
    ListView list_view;
    ArrayList<String> user_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        users_list = new ArrayList<>();

        db = FirebaseDatabase.getInstance();
        user_ref = db.getReference().child("users");

        list_view = findViewById(R.id.list_view);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email = users_list.get(position).getEmail();
                Intent i = new Intent(ViewUsers.this,AdminUserInfo.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });

        user_names = new ArrayList<>();

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();

                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {
                    User new_user = child.getValue(User.class);

                    if (new_user.getEmail() == FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        continue;

                    users_list.add(new_user);

                    user_names.add(new_user.getFirst_name() + " " + new_user.getLast_name());
                }

                user_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, user_names);
                list_view.setAdapter(user_adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
