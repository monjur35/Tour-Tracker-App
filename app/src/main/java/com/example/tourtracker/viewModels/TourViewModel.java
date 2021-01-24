package com.example.tourtracker.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tourtracker.models.ExpenseModel;
import com.example.tourtracker.models.MomentsModel;
import com.example.tourtracker.models.TourModel;
import com.example.tourtracker.repos.FirebaseRepository;

import java.util.List;
import java.util.Map;

public class TourViewModel extends AndroidViewModel {
    private FirebaseRepository repository;

    public TourViewModel(@NonNull Application application) {
        super(application);
        repository=new FirebaseRepository();

    }
    public void createNewTour(TourModel tourModel){
        repository.addNewTour(tourModel);
    }

     public  MutableLiveData<List<TourModel>> fetchTours(String uid){
        return repository.getAllToursByUserId(uid);
     }

     public  MutableLiveData<TourModel> fetchTourById(String id){
        return repository.getTourById(id);
     }





    public void createNewExpense(ExpenseModel expenseModel){
        repository.addNewExpense(expenseModel);
    }
    public MutableLiveData<List<ExpenseModel>> fetchExpenseByTourId(String tourId){
        return repository.getExpenseByTourId(tourId);
    }
    public void updateExpense(String expId,String title,double amount){
        repository.updateExpense(expId,title,amount);
    }
    public void deleteExpense(String expId){
        repository.deleteExpenseById(expId);
    }






     public void addMoments(MomentsModel momentsModel){
        repository.addNewMoments(momentsModel);
     }


    public MutableLiveData<List<MomentsModel>> fetchMomentsByTourId(String tourId){
        return repository.getAllMomentsByTourId(tourId);
    }
    public MutableLiveData<MomentsModel> fetchMomentByMomentId(String momentId){
        return repository.getAMomentByMomentId(momentId);
    }
    public void deleteMomentById(String momenttId){
        repository.deleteMomentsById(momenttId);
    }




     public double getTotalExpense(List<ExpenseModel> expenseModelList){
        double totalAmount=0.0;
        for (ExpenseModel expenseModel:expenseModelList){
            totalAmount += expenseModel.amount;
        }
        return totalAmount;
     }

     public void deleteTour(String id){
        repository.deleteTour(id);
     }



}
