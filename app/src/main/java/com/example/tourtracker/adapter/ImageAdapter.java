package com.example.tourtracker.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourtracker.R;
import com.example.tourtracker.models.MomentsModel;
import com.example.tourtracker.repos.FirebaseRepository;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>  {
    private Context context;
    private List<MomentsModel> momentsModelList;

    public ImageAdapter(Context context, List<MomentsModel> momentsModelList) {
        this.context = context;
        this.momentsModelList = momentsModelList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final  View view= LayoutInflater.from(context).inflate(R.layout.image_row,parent,false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Picasso.get().load(momentsModelList.get(position).imageDownloadUrl).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bundle bundle=new Bundle();
                bundle.putString("momentId",momentsModelList.get(position).momentId);
                bundle.putString("id",momentsModelList.get(position).tourid);
                Navigation.findNavController(view).navigate(R.id.action_tourDetailsFragment_to_imageDetailsFragment,bundle);

            }
        });

    }

    @Override
    public int getItemCount() {

        return momentsModelList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView=itemView.findViewById(R.id.imageId);
        }
    }

}



