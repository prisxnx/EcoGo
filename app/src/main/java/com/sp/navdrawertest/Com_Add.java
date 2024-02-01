package com.sp.navdrawertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Com_Add extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText addCaptionEditText;
    private EditText addPlaceEditText;
    private EditText addStateEditText;
    private EditText addDOVEditText;
    private ImageView addPic;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private StorageReference mStoragef;
    private static final int GALLERY_REQUEST_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_add);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        addCaptionEditText = findViewById(R.id.add_caption);
        addPlaceEditText = findViewById(R.id.add_place);
        addStateEditText = findViewById(R.id.add_state);
        addDOVEditText = findViewById(R.id.add_DOV);
        addPic = findViewById(R.id.add_pic);
        Button addPostButton = findViewById(R.id.add_post);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mStoragef = storage.getReference();

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve data from EditText fields
                String caption = addCaptionEditText.getText().toString();
                String place = addPlaceEditText.getText().toString();
                String state = addStateEditText.getText().toString();
                String dateOfVisit = addDOVEditText.getText().toString();

                if (TextUtils.isEmpty(caption) || TextUtils.isEmpty(place) || TextUtils.isEmpty(state) || TextUtils.isEmpty(dateOfVisit)) {
                    Toast.makeText(Com_Add.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("image");
                // Create a new document in the "posts" collection
                DocumentReference postRef = firestore.collection("posts").document();

                // Create a Post object with the retrieved data
                Post post = new Post(caption, place, state, dateOfVisit.toString());

                // Set the document with the Post object
                postRef.set(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Document successfully written
                                Toast.makeText(Com_Add.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failures
                                Toast.makeText(Com_Add.this, "Error adding post", Toast.LENGTH_SHORT).show();
                            }
                        });

                // For now, let's just display a toast message
                Toast.makeText(Com_Add.this, "Post Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Handle the back button click
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    // Override onActivityResult to handle the result from the gallery intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            Uri selectedImageUri = data.getData();

            // Set the selected image to the ImageView
            addPic.setImageURI(selectedImageUri);
        }
    }
}