package com.example.caloriemate.ui.visualisation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.caloriemate.databinding.FragmentDashboardBinding;
import com.example.caloriemate.databinding.FragmentVisualisationBinding;

public class VisualisationFragment extends Fragment {

    private FragmentVisualisationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VisualisationViewModel homeViewModel =
                new ViewModelProvider(this).get(VisualisationViewModel.class);

        binding = FragmentVisualisationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textVisulisation;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}