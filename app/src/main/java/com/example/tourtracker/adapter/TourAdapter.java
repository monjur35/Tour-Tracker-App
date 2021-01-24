package com.example.tourtracker.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourtracker.R;
import com.example.tourtracker.models.TourModel;

import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {
    private List<TourModel> tourModels;
    private Context context;

    public TourAdapter(List<TourModel> tourModels, Context context) {
        this.tourModels = tourModels;
        this.context = context;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         final View itemView= LayoutInflater.from(context).inflate(R.layout.tour_row,parent,false);

        return new TourViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        holder.nameTV.setText(tourModels.get(position).getTourName());
        holder.dateTv.setText(tourModels.get(position).getFormattedDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bundle bundle=new Bundle();
                bundle.putString("id",tourModels.get(position).getTourId());
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_tourDetailsFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tourModels.size();
    }

    class TourViewHolder extends RecyclerView.ViewHolder{
    TextView nameTV,dateTv;
        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV=itemView.findViewById(R.id.row_tour_name);
            dateTv=itemView.findViewById(R.id.row_tour_date);
        }
    }
}
