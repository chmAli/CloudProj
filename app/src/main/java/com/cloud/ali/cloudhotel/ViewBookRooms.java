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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewBookRooms extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference rooms_ref;

    ArrayList<Room> rooms_list;
    ArrayAdapter room_adapter;
    ListView list_view;
    ArrayList<String> room_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book_rooms);

        rooms_list = new ArrayList<>();

        db = FirebaseDatabase.getInstance();
        rooms_ref = db.getReference().child("rooms");

        list_view = findViewById(R.id.list_view);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = Integer.toString(rooms_list.get(position).getRoom_num());
                Intent i = new Intent(ViewBookRooms.this, UserBookRoomInfo.class);
                i.putExtra("name", name);
                startActivity(i);
            }
        });

        room_names = new ArrayList<>();

        rooms_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();

                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {
                    Room new_room = child.getValue(Room.class);

                    Log.d("room", new_room.getBooker());
                    Log.d("user", FirebaseAuth.getInstance().getCurrentUser().getEmail());

                    if (new_room.getBooker().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
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
