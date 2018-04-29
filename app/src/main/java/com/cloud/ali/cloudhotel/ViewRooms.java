package com.cloud.ali.cloudhotel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Arrays;

public class ViewRooms extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference rooms_ref;
    DatabaseReference users_ref;

    User user;

    ArrayList<Room> rooms_list;
    ArrayAdapter room_adapter;
    ListView list_view;
    ArrayList<String> room_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rooms);

        rooms_list = new ArrayList<>();

        db = FirebaseDatabase.getInstance();
        rooms_ref = db.getReference().child("rooms");
        users_ref = db.getReference().child("users");

        users_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();

                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {
                    User new_user = child.getValue(User.class);

                    Log.d("Current id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Log.d("user id", new_user.getU_id());

                    if (new_user.getU_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        Log.d("in ", "UID");
                        user = new_user;
                        break;
                    }
                }

                AfterUserWork();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    void AfterUserWork(){
        list_view = findViewById(R.id.list_view);

        if (user.getType() != 1) {
            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = Integer.toString(rooms_list.get(position).getRoom_num());
                    Intent i = new Intent(ViewRooms.this, AdminRoomInfo.class);
                    i.putExtra("name", name);
                    startActivity(i);
                }
            });
        }else{
            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = Integer.toString(rooms_list.get(position).getRoom_num());
                    Intent i = new Intent(ViewRooms.this, UserRoomInfo.class);
                    i.putExtra("name", name);
                    startActivity(i);
                }
            });
        }
        room_names = new ArrayList<>();

        rooms_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();

                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {
                    Room new_room = child.getValue(Room.class);

                    if (user.getType() != 1) {
                        rooms_list.add(new_room);
                        room_names.add("Room Num : " + Integer.toString(new_room.getRoom_num()));
                    }else if (new_room.isAvail() && !new_room.isBooked()){
                        rooms_list.add(new_room);
                        room_names.add("Room Num : " + Integer.toString(new_room.getRoom_num()));
                    }

                }

                room_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, room_names);
                list_view.setAdapter(room_adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
