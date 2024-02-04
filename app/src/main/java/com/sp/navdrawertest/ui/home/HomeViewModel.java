package com.sp.navdrawertest.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sp.navdrawertest.adminInfo;
import com.sp.navdrawertest.postInfo;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<adminInfo>> adminPosts = new MutableLiveData<>();

    public void fetchAdminPosts() {
        DatabaseReference adminPostsRef = FirebaseDatabase.getInstance().getReference("AdminPost");
        adminPostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<adminInfo> adminPostsList = new ArrayList<>();
                if (dataSnapshot != null) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        adminInfo post = postSnapshot.getValue(adminInfo.class);
                        if (post != null) {
                            adminPostsList.add(post);
                        }
                    }
                    adminPosts.postValue(adminPostsList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public LiveData<ArrayList<adminInfo>> getAdminPosts() {
        return adminPosts;
    }
}