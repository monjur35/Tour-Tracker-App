package com.example.tourtracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tourtracker.R;
import com.example.tourtracker.adapter.ViewExpenseAdapter;
import com.example.tourtracker.databinding.FragmentExpenseDetailsBinding;
import com.example.tourtracker.models.ExpenseModel;
import com.example.tourtracker.models.TourModel;
import com.example.tourtracker.viewModels.TourViewModel;

import java.util.List;


public class ExpenseDetailsFragment extends Fragment  implements ViewExpenseAdapter.EditDeleteListener{
    FragmentExpenseDetailsBinding binding;
    TourViewModel tourViewModel;
    TourDetailsFragment tourDetailsFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentExpenseDetailsBinding.inflate(inflater);
        tourViewModel= new ViewModelProvider(getActivity()).get(TourViewModel.class);
        tourDetailsFragment=new TourDetailsFragment();
        Log.d("TAG", "tourId :  " );



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        final Bundle bundle=getArguments();
        String tourID=bundle.getString("tourid");
        Log.e("TAG", "onViewCreated: "+tourID);

                tourViewModel.fetchExpenseByTourId(tourID).observe(getViewLifecycleOwner(), new Observer<List<ExpenseModel>>() {
                    @Override
                    public void onChanged(List<ExpenseModel> expenseModels) {
                        final ViewExpenseAdapter expenseAdapter =new ViewExpenseAdapter(getActivity(),expenseModels,ExpenseDetailsFragment.this);
                        Log.e("TAG", "expensedetails : "+expenseModels.size() );
                        final LinearLayoutManager llm=new LinearLayoutManager(getActivity());
                        binding.expenseRv.setLayoutManager(llm);
                        binding.expenseRv.setAdapter(expenseAdapter);




                        binding.totalExpenseTv.setText(String.valueOf(tourViewModel.getTotalExpense(expenseModels)));
                    }
                });



    }

    @Override
    public void edit(String expId,String title,double amount) {
        tourViewModel.updateExpense(expId,title,amount);

    }

    @Override
    public void delete(String expID) {
        tourViewModel.deleteExpense(expID);


    }
}