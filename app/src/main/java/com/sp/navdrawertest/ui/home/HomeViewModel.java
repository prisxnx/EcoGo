package com.sp.navdrawertest.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sp.navdrawertest.postInfo;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<postInfo>> dataSource = new MutableLiveData<>();

    public void fetchDataFromDatabase() {
        DatabaseReference postInfoRef = FirebaseDatabase.getInstance().getReference("PostInfo");
        postInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<postInfo> posts = new ArrayList<>();
                if (dataSnapshot != null) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        postInfo post = postSnapshot.getValue(postInfo.class);
                        if (post != null) {
                            posts.add(post);
                        }
                    }
                    dataSource.postValue(posts);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public LiveData<ArrayList<postInfo>> getDataSource() {
        return dataSource;
    }
}