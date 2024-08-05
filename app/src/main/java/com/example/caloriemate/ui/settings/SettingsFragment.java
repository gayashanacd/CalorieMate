package com.example.caloriemate.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.caloriemate.StartViewActivity;
import com.example.caloriemate.databinding.FragmentSettingsBinding;
import com.example.caloriemate.ui.program.ProgramActivityStep1;
import com.example.caloriemate.ui.program.ProgramActivityStep2;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings =  requireActivity().getSharedPreferences("PREFS_CM", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("IS_LOGGED", false);
                editor.putString("USERID", "");
                editor.putString("USERNAME", "");
                editor.putString("PASSWORD", "");
                startActivity(new Intent(requireActivity(), StartViewActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}