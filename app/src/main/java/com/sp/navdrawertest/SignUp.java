package com.sp.navdrawertest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity{

    EditText signupUsername, signupPassword, signupConfirm;
    TextView loginRedirect;
    ImageView PFP;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    // Add constants for image pick intents
    private static final int PICK_IMAGE_REQUEST = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 102;
    private static final int REQUEST_CAMERA_PERMISSION = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signupUsername = findViewById(R.id.username_CA);
        signupPassword = findViewById(R.id.Password_CA);
        signupConfirm = findViewById(R.id.ConfirmPass_CA);
        loginRedirect = findViewById(R.id.login_CA);
        PFP = findViewById(R.id.pfp);
        signupButton = findViewById(R.id.signup_button);

        PFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch image picker dialog
                showImagePickerDialog();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();
                String confirm = signupConfirm.getText().toString();
                String profilePic = PFP.toString();

                if (!password.equals(confirm)) {
                    signupPassword.setError("Passwords do not match");
                    signupPassword.requestFocus();
                    return; // Exit the method, preventing further execution
                }

                Helper user = new Helper(profilePic,username, password, confirm);
                reference.child(username).setValue(user);

                Toast.makeText(SignUp.this,"You have signed up successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option")
                .setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                pickImageFromGallery();
                                break;
                            case 1:
                                captureImageFromCamera();
                                break;
                        }
                    }
                });
        builder.show();
    }

    // Method to launch gallery picker
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Method to launch camera capture
    private void captureImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE_REQUEST:
                    if (data != null && data.getData() != null) {
                        // Handle the selected image URI from the gallery
                        Uri selectedImageUri = data.getData();
                        PFP.setImageURI(selectedImageUri);
                        // Now you can use this URI to display the image or upload it to Firebase, etc.
                        // Example: PFP.setImageURI(selectedImageUri);
                    }
                    break;

                case REQUEST_IMAGE_CAPTURE:
                    if (data != null && data.getExtras() != null) {
                        // Handle the captured image from the camera
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        PFP.setImageBitmap(imageBitmap);
                        // Now you can use this Bitmap to display the image or upload it to Firebase, etc.
                        // Example: PFP.setImageBitmap(imageBitmap);
                    }
                    break;
            }
        }
    }
}