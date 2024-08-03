package com.example.caloriemate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriemate.R;

import org.json.JSONObject;
import java.util.Iterator;
import java.util.List;

public class MealItemsAdapter extends RecyclerView.Adapter<MealItemsAdapter.DynamicViewHolder> {
    private List<JSONObject> jsonObjectList;
    private Context context;

    public MealItemsAdapter(List<JSONObject> jsonObjectList, Context context) {
        this.jsonObjectList = jsonObjectList;
        this.context = context;
    }

    @NonNull
    @Override
    public DynamicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_meal_item, parent, false);
        return new DynamicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicViewHolder holder, int position) {
        JSONObject jsonObject = jsonObjectList.get(position);
        holder.bind(jsonObject);
    }

    @Override
    public int getItemCount() {
        return jsonObjectList.size();
    }

    static class DynamicViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout container;

        public DynamicViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.dynamic_container);
        }

        public void bind(JSONObject jsonObject) {
            container.removeAllViews();
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.optString(key);

                TextView textView = new TextView(container.getContext());
                textView.setText(key + ": " + value);
                container.addView(textView);
            }
        }
    }
}
