package com.example.caloriemate.ui.visualisation;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.caloriemate.databases.CalarieMateDatabase;
import com.example.caloriemate.databinding.FragmentDashboardBinding;
import com.example.caloriemate.databinding.FragmentVisualisationBinding;
import com.example.caloriemate.models.DailyCaloryIntakeTuple;
import com.example.caloriemate.ui.program.ProgramActivityStep2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VisualisationFragment extends Fragment {

    private FragmentVisualisationBinding binding;
    CalarieMateDatabase cmDB;
    ArrayList barArraylist;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VisualisationViewModel homeViewModel =
                new ViewModelProvider(this).get(VisualisationViewModel.class);

        binding = FragmentVisualisationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cmDB = Room.databaseBuilder(requireContext(), CalarieMateDatabase.class, "calariemate.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        SharedPreferences settings =  requireActivity().getSharedPreferences("PREFS_CM", 0);
        String userId = settings.getString("USERID", "");

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<DailyCaloryIntakeTuple> dailyData = cmDB.mealDao().getDailyUsage(userId);
                String[] days = new String[dailyData.size()];
                barArraylist = new ArrayList();

                for(int i=0; i<dailyData.size(); i++){
                    days[i] = dailyData.get(i).getMealDate();
                    barArraylist.add(new BarEntry((float) i, (float) dailyData.get(i).getCalories()));
                }

                BarChart barChart = binding.barChartUsage;
                BarDataSet barDataSet = new BarDataSet(barArraylist,"Daily Calorie Intake");
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                barChart.setFitBars(true);
                barChart.invalidate();

                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);

                ValueFormatter formatter = new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        return days[(int) value];
                    }
                };

                xAxis.setGranularity(1f);
                xAxis.setValueFormatter(formatter);
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(14f);
                barChart.getDescription().setEnabled(true);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // setData();
                    }
                });
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