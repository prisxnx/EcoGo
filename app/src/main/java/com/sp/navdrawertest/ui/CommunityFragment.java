package com.sp.navdrawertest.ui;

import android.os.Bundle;
import android.util.Log;
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

import com.sp.navdrawertest.CommunityViewModel;
import com.sp.navdrawertest.R;
import com.sp.navdrawertest.RvAdapterTwo;
import com.sp.navdrawertest.postInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.sp.navdrawertest.databinding.FragmentCommunityBinding;

public class CommunityFragment extends Fragment {
private FragmentCommunityBinding binding;
private RvAdapterTwo rvAdapter;
private RecyclerView recyclerView;

private CommunityViewModel viewModel;

public View onCreateView(@NonNull LayoutInflater inflater,
                         ViewGroup container, Bundle savedInstanceState){
    binding=FragmentCommunityBinding.inflate(inflater,container,false);
    View root = binding.getRoot();
    recyclerView =binding.comRecycler;

    rvAdapter = new RvAdapterTwo(new ArrayList<>()); // Initialize with an empty list
    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    recyclerView.setAdapter(rvAdapter);

    return root;
}

@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
    super.onViewCreated(view,savedInstanceState);

    viewModel= new ViewModelProvider(this).get(CommunityViewModel.class);

    viewModel.getDataSource().observe(getViewLifecycleOwner(),new
            Observer<ArrayList<postInfo>>(){
                @Override
                public void onChanged(ArrayList<postInfo> newPostInfo){
                    Log.d("CommunityFragment", "Data changed: " + newPostInfo.size());
                    updateRecyclerView(newPostInfo);
                }
            });

    viewModel.fetchDataFromDatabase();
}

private void updateRecyclerView(ArrayList<postInfo> newData){
    if(rvAdapter==null){
        rvAdapter=new RvAdapterTwo(newData);
        recyclerView.setLayoutManager(new
                LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
    } else {
        rvAdapter.setPostInfoList(newData);
    }
}

@Override
    public void onDestroyView(){
    super.onDestroyView();
    binding=null;
}
}