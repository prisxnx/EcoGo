package com.sp.navdrawertest;

import static java.security.AccessController.getContext;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.sp.navdrawertest.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.sp.navdrawertest.ui.ProfileFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private TextView HeaderUsername;
    private DatabaseReference databaseReference;
    public String USERID,USERdata;
    public Bundle userDataBundle=new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            USERID = intent.getStringExtra("userId");
            userDataBundle.putString("userID",USERID);
            Log.d("MainActivity OnCreate", "USERID" + USERID);
            Log.d("MainActivity OnCreate", "userDataBundle" + userDataBundle);
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment postFragment = PostFragment.newInstance(USERID);
                postFragment.setArguments(userDataBundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, postFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about, R.id.nav_contactus, R.id.draw_logout, R.id.draw_deleteacc,R.id.draw_exitapp,R.id.bottom_CF,R.id.bottom_qr,R.id.bottom_com,R.id.bottom_profile,R.id.bottom_search)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        HeaderUsername=binding.navView.getHeaderView(0).findViewById(R.id.headerusername);
        HeaderUsername.setText(USERID);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_content_main);
                int itemId=item.getItemId();
                if(itemId==R.id.bottom_profile){
                    Fragment profileFragment = ProfileFragment.newInstance(USERID);
                    profileFragment.setArguments(userDataBundle);
                    Log.d("MainActivity BottomNav onNavItemSelect", "userDataBundle" + userDataBundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, profileFragment).commit();
                }
                navController.navigate(itemId);
                return true;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                int id = item.getItemId();

                if (id == R.id.bottom_search || id == R.id.bottom_profile || id == R.id.bottom_CF || id == R.id.bottom_qr || id == R.id.bottom_com) {
                    navController.navigate(id);
                    return true;
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    if (id == R.id.draw_deleteacc) {
                        deleteAccount();
                    } else if (id == R.id.draw_exitapp) {
                        Toast.makeText(MainActivity.this, "exit app", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.draw_logout) {
                        finish();
                    } else if(id==R.id.nav_home){
                        navController.navigate(id);

                    } else if(id==R.id.nav_about){
                        navController.navigate(id);
                    }
                    else if(id==R.id.nav_contactus){
                        navController.navigate(id);
                    }
                    return NavigationUI.onNavDestinationSelected(item, navController);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyHolder>{
        ArrayList<String> data;
        public RvAdapter(ArrayList<String> data){
            this.data=data;
        }
        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.rv_item,null,false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.rvTitle.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{
            TextView rvTitle;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                rvTitle = itemView.findViewById(R.id.rvTitle);
            }
        }
    }

    private void deleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Delete," proceed with account deletion
                        findUserID(new AccountDeletionCallback() {
                            @Override
                            public void onAccountDeletion() {
                                performAccountDeletion();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Cancel," do nothing
                    }
                })
                .show();
    }

    private void findUserID(final AccountDeletionCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String childUserId = childSnapshot.getKey();
                    if (childUserId.equals(USERID)) {
                        USERdata = childUserId;
                        // Perform account deletion here
                        callback.onAccountDeletion();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // This method will be called if there's an error while reading the data
                Log.e("Firebase", "Error reading data", error.toException());
            }
        });
    }

    private interface AccountDeletionCallback {
        void onAccountDeletion();
    }

    private void performAccountDeletion() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(USERdata);
        userRef.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Account deletion successful
                            Toast.makeText(MainActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        } else {
                            // Account deletion failed
                            Toast.makeText(MainActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}