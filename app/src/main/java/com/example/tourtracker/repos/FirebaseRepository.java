package com.example.tourtracker.repos;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.tourtracker.models.ExpenseModel;
import com.example.tourtracker.models.MomentsModel;
import com.example.tourtracker.models.TourModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseRepository {

    private FirebaseFirestore db;
    private final String TOUR_COLLECTION="Tours";
    private final String EXPENSE_COLLECTION="Expenses";
    private final String MOMENTS_COLLECTION="Moments";




    public FirebaseRepository() {
        db=FirebaseFirestore.getInstance();

    }


    public void addNewTour(TourModel tourModel){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        final DocumentReference documentReference=db.collection(TOUR_COLLECTION).document();
        tourModel.setTourId(documentReference.getId());
        tourModel.setUserId(uid);
        documentReference.set(tourModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    
    public MutableLiveData<List<TourModel>>getAllToursByUserId(String uid){
        MutableLiveData<List<TourModel>> tourModelListLivedata=new MutableLiveData<>();
        db.collection(TOUR_COLLECTION) .whereEqualTo("userId",uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    return;
                }
                tourModelListLivedata.postValue(value.toObjects(TourModel.class));
            }
        });



        return tourModelListLivedata;
    }







    public MutableLiveData<TourModel> getTourById(String id){
        MutableLiveData<TourModel> tourModelListLivedata=new MutableLiveData<>();
        db.collection(TOUR_COLLECTION).document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    return;
                }
                tourModelListLivedata.postValue(value.toObject(TourModel.class));
            }
        });
        return tourModelListLivedata;
    }








    public void addNewExpense(ExpenseModel expenseModel){
        final DocumentReference documentReference=db.collection(EXPENSE_COLLECTION).document();
        expenseModel.expId=documentReference.getId();
        documentReference.set(expenseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("TAG","onComplete");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG","on failure");
            }
        });
    }

    public void updateExpense(String expenseID,String title,double amount){

        final DocumentReference documentReference=db.collection(EXPENSE_COLLECTION).document(expenseID);

        Map<String ,Object> map=new HashMap<>();
        map.put("title",title);
        map.put("amount",amount);

        Log.e("TAG2","on failure"+expenseID);

        documentReference.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("TAG2","onComplete");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG2","on failure  :"+e.getLocalizedMessage());
            }
        });
    }

    public MutableLiveData<List<MomentsModel>> getAllMomentsByTourId(String id){
        MutableLiveData<List<MomentsModel>> momentModelMutableLiveData=new MutableLiveData<>();
        db.collection(MOMENTS_COLLECTION).whereEqualTo("tourid",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.e("TAG", "onEvent: "+error.getLocalizedMessage());
                    return;
                }
                momentModelMutableLiveData.postValue(value.toObjects(MomentsModel.class));
            }
        });
        return momentModelMutableLiveData;
    }

    public MutableLiveData<MomentsModel> getAMomentByMomentId(String id){
        MutableLiveData<MomentsModel>momentModelMutableLiveData=new MutableLiveData<>();
        db.collection(MOMENTS_COLLECTION).document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                momentModelMutableLiveData.postValue(value.toObject(MomentsModel.class));
            }
        });
        return momentModelMutableLiveData;
    }

    public MutableLiveData<List<ExpenseModel>> getExpenseByTourId(String id){
        MutableLiveData<List<ExpenseModel>> expenseModelMutableLiveData=new MutableLiveData<>();
        db.collection(EXPENSE_COLLECTION).whereEqualTo("tourId",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    return;
                }
                expenseModelMutableLiveData.postValue(value.toObjects(ExpenseModel.class));
            }
        });
        return expenseModelMutableLiveData;
    }





    public void addNewMoments(MomentsModel momentsModel){
        final DocumentReference documentReference=db.collection(MOMENTS_COLLECTION).document();
        momentsModel.momentId=documentReference.getId();
        momentsModel.imageName=String.valueOf(System.currentTimeMillis());
        documentReference.set(momentsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("TAG","onComplete");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG","on failure");
            }
        });
    }



    public void deleteTour(String id){
        WriteBatch batch=db.batch();
        db.collection(MOMENTS_COLLECTION).whereEqualTo("tourid",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot q: task.getResult()){
                    final MomentsModel momentsModel=q.toObject(MomentsModel.class);
                    batch.delete(db.collection(MOMENTS_COLLECTION).document(momentsModel.momentId));
                }
                db.collection(EXPENSE_COLLECTION).whereEqualTo("tourId",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot q: task.getResult()){
                            final ExpenseModel expenseModel=q.toObject(ExpenseModel.class);
                            batch.delete(db.collection(EXPENSE_COLLECTION).document(expenseModel.expId));
                        }
                        batch.delete(db.collection(TOUR_COLLECTION).document(id));
                        batch.commit();

                    }
                });
            }
        });
    }

    public void deleteExpenseById(String expensId ){
        db.collection(EXPENSE_COLLECTION).document(expensId)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("TAG2", "Expense delete success: ");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG2", "Expense delete failed: "+e.getLocalizedMessage());

            }
        });
    }


    public void deleteMomentsById(String momentsId ){
        db.collection(MOMENTS_COLLECTION).document(momentsId)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("TAG2", "Expense delete success: ");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG2", "Expense delete failed: "+e.getLocalizedMessage());

            }
        });
    }








}
