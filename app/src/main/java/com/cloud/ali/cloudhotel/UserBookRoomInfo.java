package com.cloud.ali.cloudhotel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cloud.ali.cloudhotel.models.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserBookRoomInfo extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference room_ref;

    String name;
    Room room;

    TextView room_num;
    TextView room_price;
    TextView ac;
    TextView tv;
    TextView wifi;
    TextView num_of_beds;

    ArrayList<Room> rooms_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_room_info);

        rooms_list = new ArrayList<>();

        Intent i = getIntent();
        name = i.getStringExtra("name");

        room_num = findViewById(R.id.room_num);
        room_price = findViewById(R.id.price);
        ac = findViewById(R.id.ac);
        tv = findViewById(R.id.tv);
        wifi = findViewById(R.id.wifi);
        num_of_beds = findViewById(R.id.bed_count);

        db = FirebaseDatabase.getInstance();
        room_ref = db.getReference().child("rooms");

        room_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> list=dataSnapshot.getChildren();

                for (DataSnapshot child:list) {
                    Room temp = child.getValue(Room.class);
                    if (Integer.toString(temp.getRoom_num()).equals(name)) {
                        room = temp;
                        break;
                    }
                }

                if (room != null){
                    room_num.setText(Integer.toString(room.getRoom_num()));
                    room_price.setText(Integer.toString(room.getPrice()));

                    if (room.isWifi())
                        wifi.setText("included");
                    else
                        wifi.setText("not included");

                    if (room.isTv())
                        tv.setText("included");
                    else
                        tv.setText("not included");

                    if (room.isAc())
                        ac.setText("included");
                    else
                        ac.setText("not included");

                    num_of_beds.setText(Integer.toString(room.getBeds()));
                }else
                    room_num.setText("No Room");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void UnBookRoom(View v){
        room.setBooked(false);
        room.setBooker("");

        room_ref.child(Integer.toString(room.getRoom_num())).setValue(room);

        startActivity(new Intent(this, ViewBookRooms.class));

    }

}
