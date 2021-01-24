package com.example.tourtracker.viewModels;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends AndroidViewModel {
    public MutableLiveData<Authentication>authenticationMutableLiveData;
    private FirebaseAuth auth;
    Context context;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        auth=FirebaseAuth.getInstance();
        authenticationMutableLiveData=new MutableLiveData<>();

        if (auth.getCurrentUser()==null){
            authenticationMutableLiveData.postValue(Authentication.UNAUTHENTICATED);
        }
        else {
            authenticationMutableLiveData.postValue(Authentication.AUTHENTICATED);
        }
    }



    public enum Authentication{
        AUTHENTICATED,UNAUTHENTICATED;
    }




    public void login(String email,String password){
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                authenticationMutableLiveData.postValue(Authentication.AUTHENTICATED);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(), "Error"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }




    public void register(String email,String password){
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                authenticationMutableLiveData.postValue(Authentication.AUTHENTICATED);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(), "Error :"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    public void signOut(){
        if (auth.getCurrentUser()!=null){
            auth.signOut();
            authenticationMutableLiveData.postValue(Authentication.UNAUTHENTICATED);
        }
    }
}
