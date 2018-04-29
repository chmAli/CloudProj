package com.cloud.ali.cloudhotel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cloud.ali.cloudhotel.models.Room;
import com.cloud.ali.cloudhotel.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRoom extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference room_ref;

    EditText room_num;
    CheckBox wifi;
    CheckBox ac;
    CheckBox tv;
    RadioButton one_bed;
    RadioButton two_bed;
    RadioButton three_bed;
    EditText price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);


        db = FirebaseDatabase.getInstance();
        room_ref = db.getReference().child("rooms");

        room_num = findViewById(R.id.room_number);
        wifi = findViewById(R.id.wifi);
        ac = findViewById(R.id.ac);
        tv = findViewById(R.id.tv);
        one_bed = findViewById(R.id.one_bed);
        two_bed = findViewById(R.id.two_bed);
        three_bed = findViewById(R.id.three_bed);
        price = findViewById(R.id.price);

    }

    public void AddRoom(View v){
        int num_bed = 1;
        if (three_bed.isChecked())
            num_bed = 3;
        else if (two_bed.isChecked())
            num_bed = 2;

        Room r = new Room(tv.isChecked(), num_bed, wifi.isChecked(), ac.isChecked(), Integer.parseInt(price.getText().toString()),
                Integer.parseInt(room_num.getText().toString()));

        room_ref.child(room_num.getText().toString()).setValue(r);

        startActivity(new Intent(this, AdminHome.class));
    }

}
