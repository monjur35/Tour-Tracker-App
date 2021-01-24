package com.example.tourtracker.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.tourtracker.R;
import com.example.tourtracker.databinding.FragmentNewTourBinding;
import com.example.tourtracker.models.TourModel;
import com.example.tourtracker.viewModels.TourViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;


public class newTourFragment extends Fragment {
    private FragmentNewTourBinding binding;
    private TourViewModel tourViewModel;
    private String formattedDate;
    private long timeStamp;
    private int year1;
    private int month1;
    private int diffDay;

    public newTourFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentNewTourBinding.inflate(inflater);
        tourViewModel=new ViewModelProvider(requireActivity()).get(TourViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.dateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDateFromUser();
            }
        });

        binding.toursaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tourName=binding.tourNameID.getText().toString();
                final String destination=binding.tourDestination.getText().toString();
                final String budget=binding.tourBudgetID.getText().toString();


                if (tourName.isEmpty()||destination.isEmpty()||budget.isEmpty() ||formattedDate.isEmpty()){
                    Toast.makeText(getActivity(), "Input all field", Toast.LENGTH_SHORT).show();

                }
                else{
                    final double budget1=Double.parseDouble(budget);
                    final TourModel tourModel=new TourModel(null,null,tourName,destination,budget1,formattedDate,timeStamp,month1,year1);
                    tourViewModel.createNewTour(tourModel);
                    Navigation.findNavController(view).navigate(R.id.action_newTourFragment_to_homeFragment);
                }
            }
        });



    }

    private void selectDateFromUser() {
        final Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),listener,year,month,day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            final Calendar calendar=Calendar.getInstance();
            calendar.set(year,month,day);
            final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            formattedDate=simpleDateFormat.format(calendar.getTime());
            timeStamp=calendar.getTimeInMillis();
            year1=calendar.get(Calendar.YEAR);
            month1=calendar.get(Calendar.YEAR);
            binding.dateID.setText(formattedDate);
            timediff();


        }
    };

    private void timediff(){
        final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        try {
            final Date mDate=simpleDateFormat.parse(formattedDate);
            long selectedTimeInMill=mDate.getTime();
            final long diffINMill=timeStamp-selectedTimeInMill;
            diffDay= (int) (diffINMill/(1000*60*60))%7;
            Log.e("time","time difference :"+diffDay);
            Log.e("time","time difference in millies :"+diffINMill);
            Log.e("time","time stamp  in millies :"+timeStamp);
            Log.e("time","time selected  in millies :"+selectedTimeInMill);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}