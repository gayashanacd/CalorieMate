package com.example.caloriemate.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.caloriemate.databases.CalarieMateDatabase;
import com.example.caloriemate.databinding.FragmentDashboardBinding;
import com.example.caloriemate.databinding.FragmentProfileBinding;
import com.example.caloriemate.models.Program;
import com.example.caloriemate.models.User;
import com.example.caloriemate.ui.program.ProgramActivityStep2;
import com.example.caloriemate.utils.ProgramType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    CalarieMateDatabase cmDB;
    User currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cmDB = Room.databaseBuilder(requireContext(), CalarieMateDatabase.class, "calariemate.db").build();

        SharedPreferences settings =  requireActivity().getSharedPreferences("PREFS_CM", 0);
        String userId = settings.getString("USERID", "");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                currentUser = cmDB.userDAO().getUserById(userId);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                    }
                });
            }
        });

        binding.buttonProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        cmDB.userDAO().updateUser(userId, binding.editTextProfileUsername.getText().toString(),
                                binding.editTextProfilePassword.getText().toString(),
                                binding.editTextProfileFirstName.getText().toString(),
                                binding.editTextProfileLastName.getText().toString());

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Successfully update the user !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        return root;
    }

    private void setData() {

        binding.editTextProfileUsername.setText(currentUser.getUserName());
        binding.editTextProfilePassword.setText(currentUser.getPassword());
        binding.editTextProfileFirstName.setText(currentUser.getFirstName());
        binding.editTextProfileLastName.setText(currentUser.getLastName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}