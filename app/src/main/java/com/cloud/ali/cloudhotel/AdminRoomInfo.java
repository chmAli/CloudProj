package com.cloud.ali.cloudhotel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.ali.cloudhotel.models.Room;
import com.cloud.ali.cloudhotel.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AdminRoomInfo extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference room_ref;

    String name;
    Room room;

    EditText room_num;
    CheckBox wifi;
    CheckBox ac;
    CheckBox tv;
    RadioButton one_bed;
    RadioButton two_bed;
    RadioButton three_bed;
    EditText room_price;
    TextView book_by;
    CheckBox avail;

    ImageView img_1;
    ImageView img_2;
    ImageView img_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room_info);

        rooms_list = new ArrayList<>();

        Intent i = getIntent();
        name = i.getStringExtra("name");

        room_num = findViewById(R.id.room_num);
        wifi = findViewById(R.id.wifi);
        ac = findViewById(R.id.ac);
        tv = findViewById(R.id.tv);
        one_bed = findViewById(R.id.one_bed);
        two_bed = findViewById(R.id.two_bed);
        three_bed = findViewById(R.id.three_bed);
        room_price = findViewById(R.id.price);
        book_by = findViewById(R.id.book_by);
        avail = findViewById(R.id.avail);
        img_1 = findViewById(R.id.img_1);
        img_2 = findViewById(R.id.img_2);
        img_3 = findViewById(R.id.img_3);

        img_1.setOnClickListener(img_1_listen);
        img_2.setOnClickListener(img_2_listen);
        img_3.setOnClickListener(img_3_listen);



        db = FirebaseDatabase.getInstance();
        room_ref = db.getReference().child("rooms");

        room_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> list=dataSnapshot.getChildren();

                Log.d("intent name", name);

                for (DataSnapshot child:list) {
                    Room temp = child.getValue(Room.class);
                    Log.d("loop name", Integer.toString(temp.getRoom_num()));
                    if (Integer.toString(temp.getRoom_num()).equals(name)) {
                        room = temp;
                        Log.d("in loop", "");
                        break;
                    }
                }

                if (room != null) {
                    Log.d("", room_num.getText().toString());
                    room_num.setText(Integer.toString(room.getRoom_num()));

                    wifi.setChecked(room.isWifi());
                    ac.setChecked(room.isAc());
                    tv.setChecked(room.isTv());

                    if (room.getBeds() == 1) {
                        one_bed.setChecked(true);
                        two_bed.setChecked(false);
                        three_bed.setChecked(false);
                    }else if (room.getBeds() == 2){
                        one_bed.setChecked(false);
                        two_bed.setChecked(true);
                        three_bed.setChecked(false);
                    }else{
                        one_bed.setChecked(false);
                        two_bed.setChecked(false);
                        three_bed.setChecked(true);
                    }

                    room_price.setText(Integer.toString(room.getPrice()));
                    book_by.setText(room.getBooker());

                    avail.setChecked(room.isAvail());

                    for (int img_i=0; img_i<3; ++img_i) {
                        if (img_i == 1)
                            img = img_1;
                        else if (img_i == 2)
                            img = img_2;
                        else
                            img = img_3;
                        try {
                            DpCreate(Integer.toString(room.getRoom_num()) + "_" + Integer.toString(img_i));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                else
                    room_num.setText("No Room");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    ArrayList<Room> rooms_list;

    public void DeleteRoom(View v){

        room_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                rooms_list.clear();

                for (DataSnapshot child:list) {
                    Room new_room = child.getValue(Room.class);
                    rooms_list.add(new_room);
                }

                for (int i=0; i<rooms_list.size(); ++i){

                    if (rooms_list.get(i).getRoom_num() == room.getRoom_num()){
//                        rooms_list.remove(i);
                        room_ref.child(Integer.toString(rooms_list.get(i).getRoom_num())).removeValue();
                        break;
                    }
                }

                //UpdateRoomDatabase(rooms_list);

                startActivity(new Intent(AdminRoomInfo.this, ViewRooms.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void UpdateRoom(View v){

        int num_bed = 1;
        if (three_bed.isChecked())
            num_bed = 3;
        else if (two_bed.isChecked())
            num_bed = 2;

        room.setTv(tv.isChecked());
        room.setBeds(num_bed);
        room.setWifi(wifi.isChecked());
        room.setAc(ac.isChecked());
        room.setPrice(Integer.parseInt(room_price.getText().toString()));
        room.setRoom_num(Integer.parseInt(room_num.getText().toString()));
        room.setAvail(avail.isChecked());
        room.setBooker(book_by.getText().toString());

        room_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                rooms_list.clear();

                for (DataSnapshot child:list) {
                    Room new_room = child.getValue(Room.class);
                    rooms_list.add(new_room);
                }

                for (int i=0; i<rooms_list.size(); ++i){

                    if (rooms_list.get(i).getRoom_num() == room.getRoom_num()){
                        //rooms_list.remove(i);
                        //rooms_list.add(i, room);
                        room_ref.child(Integer.toString(rooms_list.get(i).getRoom_num())).setValue(room);
                        break;
                    }
                }

                //UpdateRoomDatabase(rooms_list);

                startActivity(new Intent(AdminRoomInfo.this, ViewRooms.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void UpdateRoomDatabase(ArrayList<Room> var){
//        Log.d("replacing rooms list" , Integer.toString(var.size()));
//        db.getReference().child("rooms").setValue(var);

        for (int i=0; i<var.size(); ++i){

            db.getReference().child("rooms").child(Integer.toString(var.get(i).getRoom_num())).setValue(var.get(i));

        }

    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener img_1_listen = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            // Yes we will handle click here but which button clicked??? We don't know
            ShowNotice(1);
            img_pos = 1;
        }
    };

    private View.OnClickListener img_2_listen = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            // Yes we will handle click here but which button clicked??? We don't know
            ShowNotice(2);
            img_pos = 2;
        }
    };

    private View.OnClickListener img_3_listen = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            // Yes we will handle click here but which button clicked??? We don't know
            ShowNotice(3);
            img_pos = 3;
        }
    };

    public void ShowNotice(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this comment ?");
        // Add the buttons
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (pos == 1)
                    img = img_1;
                else if (pos == 2)
                    img = img_2;
                else
                    img = img_3;

                OpenGallery();

            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String name = Integer.toString(room.getRoom_num()) + "_" + Integer.toString(pos);
                StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("images/"+name+".jpg");
                riversRef.delete();
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private static final int Img_Marker = 0;
    ImageView img;
    int img_pos = 1;

    public void OpenGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Img_Marker);

        Log.d("in opengallery", "success");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("in activity", "success");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Img_Marker && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("in activity if", "success");
            CloudImage(uri, Integer.toString(room.getRoom_num()) + "_" + Integer.toString(img_pos));
        }

    }

    public void CloudImage(Uri file, final String name){
        Log.d("in cloud image", "success");
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("images/"+name+".jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.d("in final test", "success");

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        db.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                child("img").setValue(downloadUrl.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        Log.d("in final test", "failure");
                    }
                });
    }

    public void DpCreate(String name) throws IOException {
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("images/"+name+".jpg");

        final File localFile = File.createTempFile("images", "jpg");
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getPath());
                        img.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

}
