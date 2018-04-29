package com.cloud.ali.cloudhotel;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Profile extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference user_Ref;

    User user;

    private static final int Img_Marker = 0;
    ImageView img;
    EditText first_name;
    EditText last_name;
    TextView email;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseDatabase.getInstance();
        user_Ref = db.getReference().child("users");

        img = findViewById(R.id.img);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);

        user_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();

                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {
                    User new_user = child.getValue(User.class);

                    if (new_user.getU_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        user = new_user;
                        break;
                    }
                }

                first_name.setText(user.getFirst_name());
                last_name.setText(user.getLast_name());
                email.setText(user.getEmail());

                try {
                    DpCreate(user.getEmail());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        
    }

    public void OkBtn(View v){
        user.setFirst_name(first_name.getText().toString());
        user.setLast_name(last_name.getText().toString());

        user_Ref.child(user.getU_id()).setValue(user);

        if (user.getType() == 1)
            startActivity(new Intent(this, Home.class));
        else
            startActivity(new Intent(this, AdminHome.class));

    }
    
    public void OpenGallery(View v){
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
            CloudImage(uri, FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

    }

    public void DpCreate(String email) throws IOException {
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("images/"+email+".jpg");

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

    public void CloudImage(Uri file, final String email){
        Log.d("in cloud image", "success");
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("images/"+email+".jpg");

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
    
}
