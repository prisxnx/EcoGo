package com.sp.navdrawertest.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sp.navdrawertest.R;
import com.sp.navdrawertest.RvAdapter;
import com.sp.navdrawertest.adminInfo;
import com.sp.navdrawertest.databinding.FragmentHomeBinding;
import com.sp.navdrawertest.postInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private RvAdapter rvAdapter;
    private HomeViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.horizontalRv;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Observe changes in the data source
        viewModel.getAdminPosts().observe(getViewLifecycleOwner(), new Observer<ArrayList<adminInfo>>() {
            @Override
            public void onChanged(ArrayList<adminInfo> newAdminPosts) {
                updateRecyclerView(newAdminPosts);
            }
        });

        // Fetch data from the Realtime Database
        viewModel.fetchAdminPosts();
    }

    private void updateRecyclerView(ArrayList<adminInfo> newData) {
        // Update the RecyclerView adapter or dataset
        if (rvAdapter == null) {
            rvAdapter = new RvAdapter(newData);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(rvAdapter);
        } else {
            // If adapter already exists, update the data and notify the adapter
            rvAdapter.setAdminInfoList(newData);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
