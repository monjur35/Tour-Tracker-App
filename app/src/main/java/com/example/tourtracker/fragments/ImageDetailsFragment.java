package com.example.tourtracker.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tourtracker.R;
import com.example.tourtracker.databinding.FragmentImageDetailsBinding;
import com.example.tourtracker.models.MomentsModel;
import com.example.tourtracker.models.TourModel;
import com.example.tourtracker.viewModels.TourViewModel;
import com.squareup.picasso.Picasso;


public class ImageDetailsFragment extends Fragment {

    FragmentImageDetailsBinding binding;
    private TourViewModel tourViewModel;
    private  String momentId;
    private  String tourid;

    public ImageDetailsFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_delete_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_menu:

                break;

            case R.id.delete_menu:

                Log.e("TAG", "delete phptp: "+ momentId);

                tourViewModel.deleteMomentById(momentId);
                final Bundle bundle=new Bundle();
                bundle.putString("id",tourid);
                Navigation.findNavController(getView()).navigate(R.id.action_imageDetailsFragment_to_tourDetailsFragment,bundle);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentImageDetailsBinding.inflate(inflater);
        tourViewModel= new ViewModelProvider(requireActivity()).get(TourViewModel.class);

        final Bundle bundle=getArguments();
        momentId=bundle.getString("momentId");
        tourid = bundle.getString("id");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tourViewModel.fetchMomentByMomentId(momentId).observe(getViewLifecycleOwner(), new Observer<MomentsModel>() {
            @Override
            public void onChanged(MomentsModel momentsModel) {
                Picasso.get().load(momentsModel.imageDownloadUrl).into(binding.imageViewDetailsId);
                final String tourId=momentsModel.tourid;
                tourViewModel.fetchTourById(tourId).observe(getViewLifecycleOwner(), new Observer<TourModel>() {
                    @Override
                    public void onChanged(TourModel tourModel) {
                        binding.tourNameIDinDetails.setText(tourModel.getTourName());
                    }
                });

            }
        });






    }
}