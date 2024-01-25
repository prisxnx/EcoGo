package com.sp.navdrawertest.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> dataSource = new MutableLiveData<>();

    public void setDataSource(ArrayList<String> data) {
        dataSource.setValue(data);
    }

    public LiveData<ArrayList<String>> getDataSource() {
        return dataSource;
    }
}