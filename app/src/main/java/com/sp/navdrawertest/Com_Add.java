package com.sp.navdrawertest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Com_Add extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText addCaptionEditText;
    private EditText addPlaceEditText;
    private EditText addStateEditText;
    private EditText addDOVEditText;
    private ImageView addPic;
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

                // Perform actions with the retrieved data (e.g., post to server, etc.)
                // Add your logic here

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