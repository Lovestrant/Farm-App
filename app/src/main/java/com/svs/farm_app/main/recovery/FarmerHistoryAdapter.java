package com.svs.farm_app.main.recovery;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svs.farm_app.R;

import java.util.List;

/**
 * Created by Wamae on 12-Dec-17.
 */

public class FarmerHistoryAdapter extends RecyclerView.Adapter<FarmerHistoryAdapter.MyViewHolder> {
    private List<FarmerHistory> farmerHistory = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSeasonName, tvTotalCollected, tvTotalPaid;

        public MyViewHolder(View view) {
            super(view);
            tvSeasonName = (TextView) view.findViewById(R.id.tvSeasonName);
            tvTotalCollected = (TextView) view.findViewById(R.id.tvTotalCollected);
            tvTotalPaid = (TextView) view.findViewById(R.id.tvTotalPaid);
        }
    }


    public FarmerHistoryAdapter(List<FarmerHistory> farmerHistory) {
        this.farmerHistory = farmerHistory;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.farmer_history_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FarmerHistory history = farmerHistory.get(position);
        holder.tvSeasonName.setText(history.seasonName);
        holder.tvTotalCollected.setText(Float.toString(history.totalAmount));
        holder.tvTotalPaid.setText(Float.toString(history.totalPaid));
    }

    @Override
    public int getItemCount() {
        return farmerHistory.size();
    }
}
