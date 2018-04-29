package com.cloud.ali.cloudhotel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cloud.ali.cloudhotel.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewComment extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference comment_ref;

    ArrayList<String> comment_list;
    ArrayAdapter user_adapter;
    ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comment);

        comment_list = new ArrayList<>();

        db = FirebaseDatabase.getInstance();
        comment_ref = db.getReference().child("comments");

        list_view = findViewById(R.id.list_view);

/*        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email = users_list.get(position).getEmail();
                Intent i = new Intent(ViewUsers.this,AdminUserInfo.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });*/

        comment_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();

                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {
                    String new_Comment = child.getValue(String.class);

                    comment_list.add(new_Comment);
                }

                user_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, comment_list);
                list_view.setAdapter(user_adapter);
                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
                        ShowNotice(position);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void ShowNotice(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this comment ?");
        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                comment_list.remove(pos);

                for (String r:comment_list) {
                    Log.d("listRoom",r);
                }
                db.getReference().child("comments").setValue(comment_list);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
