package com.sp.navdrawertest;

import android.app.Activity;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sp.navdrawertest.ui.CommunityFragment;

import org.checkerframework.checker.index.qual.LengthOf;
import org.checkerframework.checker.units.qual.Current;

import java.io.IOException;

public class PostFragment  extends Fragment {

    private DatabaseReference databaseReference;
    private EditText UserEnterSiteName, UserEnterState, UserEnterDate, UserEnterCaption;
    private Button UploadSiteInfoButton;
    private MaterialCardView SelectPhoto;
    private Uri ImageUri;
    private Bitmap bitmap;
    private ImageView PostImageView;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private StorageReference mStorageref;
    private String PhotoUrl;
    private FirebaseAuth firebaseAuth;
    private String CurrentUserId;
    private String DocId;
    private String currentUser;
    private AppCompatButton currentLocButton, manualLocButton;
    private LocationManager locationManager;
    private double latitude, longitude;
    private static final int MANUAL_MAP_REQUEST_CODE = 123;
    private boolean manualLocationSelected = false;
    private double manualLatitude, manualLongitude, currentLatitude, currentLongitude;

    public PostFragment(){

    }
    public static PostFragment newInstance(String userId) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve arguments and set currentUser
        if (getArguments() != null) {
            currentUser = getArguments().getString("userID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_posts,container, false);

        UserEnterCaption=view.findViewById(R.id.userentercaption);
        UserEnterSiteName=view.findViewById(R.id.userentersitename);
        UserEnterState=view.findViewById(R.id.userenterstate);
        UserEnterDate=view.findViewById(R.id.userenterdov);

        firestore= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mStorageref = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("PostInfo");
        SelectPhoto=view.findViewById(R.id.selectphoto);
        PostImageView=view.findViewById(R.id.postimageview);
        UploadSiteInfoButton=view.findViewById(R.id.uploadpostbutton);
        SelectPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CheckStoragePermission();
            }
        });

        UploadSiteInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ProgressBar();
                if (ImageUri != null) {
                    UploadImage();
                } else {
                    // Handle the case where ImageUri is null (no image selected)
                    Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        currentLocButton = view.findViewById(R.id.currentlocbutton);
        manualLocButton = view.findViewById(R.id.manuallocbutton);

        currentLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
                manualLocationSelected = false;
            }
        });

        manualLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //manualLocationSelected = true;
                // Start ManualMap activity for manual location selection
                Intent intent = new Intent(getContext(), ManualMap.class);
                startActivityForResult(intent, MANUAL_MAP_REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MANUAL_MAP_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Retrieve the selected latitude and longitude from ManualMap activity
            manualLatitude = data.getDoubleExtra("latitude", 0.0);
            manualLongitude = data.getDoubleExtra("longitude", 0.0);

            // Update the flag to indicate that a manual location is selected
            manualLocationSelected = true;

            // Now you can use these values as needed (e.g., store in Firebase)
            Toast.makeText(getContext(), "Selected Location: " + manualLatitude + ", " + manualLongitude, Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Check if the manual location is not selected, then register for location updates
            if (!manualLocationSelected) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } else {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        // Update the flag to indicate that a manual location is not selected
        manualLocationSelected = false;
    }

    // Create a LocationListener to handle location updates
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                // Get the latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                // Optionally, you can stop receiving location updates if you only need a one-time location
                locationManager.removeUpdates(this);

                // Now, you can use latitude and longitude as needed
                Toast.makeText(getContext(), "Current Location: " + currentLatitude + ", " + currentLongitude, Toast.LENGTH_SHORT).show();
            }
        }

        // Implement other LocationListener methods if needed
        @Override
        public void onProviderDisabled(String provider) {
            // Handle provider disabled
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Handle provider enabled
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Handle status changed
        }
    };



    private void CheckStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                PickImageFromGallery();
            }
        } else {
            PickImageFromGallery();
        }
    }


    private void PickImageFromGallery(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
    }
    ActivityResultLauncher<Intent> launcher =registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result->{
                if(result.getResultCode()== Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        ImageUri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    getActivity().getContentResolver(),
                                    ImageUri
                            );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(ImageUri!=null){
                        PostImageView.setImageBitmap(bitmap);
                    }
                }
            }
    );

    private void UploadImage() {
        if (ImageUri != null) {
            final StorageReference myRef = mStorageref.child("photo/" + ImageUri.getLastPathSegment());
            myRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri != null) {
                                PhotoUrl=uri.toString();
                                UploadSiteInfo();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void UploadSiteInfo() {
        String postsitename = UserEnterSiteName.getText().toString().trim();
        String postcaption = UserEnterCaption.getText().toString().trim();
        String postdov = UserEnterDate.getText().toString().trim();
        String poststate = UserEnterState.getText().toString().trim();


        if (TextUtils.isEmpty(postsitename) || TextUtils.isEmpty(postcaption) || TextUtils.isEmpty(postdov) || TextUtils.isEmpty(poststate)) {
            Toast.makeText(getContext(), "Please Fill ALL Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use the latest latitude and longitude based on the source (current or manual)
        if (manualLocationSelected && manualLatitude != 0.0 && manualLongitude != 0.0) {
            latitude = manualLatitude;
            longitude = manualLongitude;

        } else {
            latitude = currentLatitude;
            longitude = currentLongitude;
        }

        // Generate a unique key for the post in Realtime Database
        String postId = databaseReference.push().getKey();
        postInfo postInfo = new postInfo(postsitename, postcaption, postdov, poststate, String.valueOf(latitude), String.valueOf(longitude), "", PhotoUrl, currentUser); // Use currentUser here

        // Save the post using the generated key
        databaseReference.child(postId).setValue(postInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    replaceFragment(new CommunityFragment());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void replaceFragment(CommunityFragment communityFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, communityFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

