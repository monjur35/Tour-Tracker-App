package com.example.tourtracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tourtracker.R;
import com.example.tourtracker.databinding.FragmentLoginBinding;
import com.example.tourtracker.viewModels.LoginViewModel;

public class loginFragment extends Fragment {
    private FragmentLoginBinding binding;
    LoginViewModel loginViewModel;

    public loginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLoginBinding.inflate(inflater);
        loginViewModel=new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        loginViewModel.authenticationMutableLiveData.observe(getViewLifecycleOwner(), new Observer<LoginViewModel.Authentication>() {
            @Override
            public void onChanged(LoginViewModel.Authentication authentication) {
                if (authentication== LoginViewModel.Authentication.AUTHENTICATED){
                    Navigation.findNavController(container).navigate(R.id.action_loginFragment_to_homeFragment);
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=binding.emailID.getText().toString().trim();
                final String pass=binding.passwordID.getText().toString().trim();
                if (email.isEmpty()||pass.isEmpty()){
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    loginViewModel.login(email,pass);
                }
            }
        });


        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=binding.emailID.getText().toString().trim();
                final String pass=binding.passwordID.getText().toString().trim();
                if (email.isEmpty()||pass.isEmpty()){
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    loginViewModel.register(email,pass);
                }
            }
        });




    }
}