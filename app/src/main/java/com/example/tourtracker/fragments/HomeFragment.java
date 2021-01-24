package com.example.tourtracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tourtracker.R;
import com.example.tourtracker.adapter.TourAdapter;
import com.example.tourtracker.databinding.FragmentHomeBinding;
import com.example.tourtracker.databinding.FragmentLoginBinding;
import com.example.tourtracker.models.TourModel;
import com.example.tourtracker.viewModels.LoginViewModel;
import com.example.tourtracker.viewModels.TourViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class HomeFragment extends Fragment {
   private LoginViewModel loginViewModel;
   private FragmentHomeBinding binding;
   private TourViewModel tourViewModel;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOutMenu:
                loginViewModel.signOut();
                break;

            case R.id.map_menu:
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_mapsFragment);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater);
        loginViewModel=new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        tourViewModel=new ViewModelProvider(requireActivity()).get(TourViewModel.class);
        loginViewModel.authenticationMutableLiveData.observe(getViewLifecycleOwner(), new Observer<LoginViewModel.Authentication>() {
            @Override
            public void onChanged(LoginViewModel.Authentication authentication) {
                if (authentication== LoginViewModel.Authentication.UNAUTHENTICATED){
                    Navigation.findNavController(container).navigate(R.id.action_homeFragment_to_loginFragment);
                }
                else {

                    fetchTours();
                }
            }
        });


        binding.fabId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(container).navigate(R.id.action_homeFragment_to_newTourFragment);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

    public void  fetchTours(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        tourViewModel.fetchTours(uid).observe(getViewLifecycleOwner(), tourModels -> {
            if (tourModels.size()==0){
                binding.progressBArID.setVisibility(View.VISIBLE);
            }
            final TourAdapter adapter=new TourAdapter(tourModels,getContext());////////////////////////getActivity///////////////////////////////////
            final LinearLayoutManager llm=new LinearLayoutManager(getContext());
            binding.recylerRV.setLayoutManager(llm);
            binding.recylerRV.setAdapter(adapter);
            binding.progressBArID.setVisibility(View.GONE);

        });

    }
}