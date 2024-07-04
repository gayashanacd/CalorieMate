package com.example.caloriemate.ui.visualisation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VisualisationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public VisualisationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}