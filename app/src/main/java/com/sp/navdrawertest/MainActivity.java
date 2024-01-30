package com.sp.navdrawertest;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
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

import com.sp.navdrawertest.databinding.ActivityMainBinding;
import com.sp.navdrawertest.ui.CarbonFootprintFragment;
import com.sp.navdrawertest.ui.CommunityFragment;
import com.sp.navdrawertest.ui.ProfileFragment;
import com.sp.navdrawertest.ui.QRFragment;
import com.sp.navdrawertest.ui.home.HomeFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Com_Add.class);
                startActivity(intent);
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

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment_content_main);
                navController.navigate(item.getItemId());
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
                        Toast.makeText(MainActivity.this, "delete account", Toast.LENGTH_SHORT).show();
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
}