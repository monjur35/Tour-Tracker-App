package com.example.tourtracker.fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tourtracker.R;
import com.example.tourtracker.adapter.ImageAdapter;
import com.example.tourtracker.adapter.TourAdapter;
import com.example.tourtracker.adapter.ViewExpenseAdapter;
import com.example.tourtracker.databinding.FragmentTourDetailsBinding;
import com.example.tourtracker.models.ExpenseModel;
import com.example.tourtracker.models.MomentsModel;
import com.example.tourtracker.models.TourModel;
import com.example.tourtracker.viewModels.TourViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.internal.$Gson$Preconditions;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class TourDetailsFragment extends Fragment   {
    FragmentTourDetailsBinding binding;
    TourViewModel tourViewModel;
    private String tourId;
    public String tourName;


    public TourDetailsFragment() {


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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Are you sure to delete tis tour ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tourViewModel.deleteTour(tourId);
                        Navigation.findNavController(getView()).navigate(R.id.action_tourDetailsFragment_to_homeFragment);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog=builder.create();
                dialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTourDetailsBinding.inflate(inflater);
        tourViewModel= new ViewModelProvider(requireActivity()).get(TourViewModel.class);
        return binding.getRoot();

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle bundle=getArguments();
        tourId =bundle.getString("id");
        tourViewModel.fetchTourById(tourId).observe(getViewLifecycleOwner(), new Observer<TourModel>() {
            @Override
            public void onChanged(TourModel tourModel) {
                tourName=tourModel.getTourName();
                binding.tableTotalBudgetTv.setText(String.valueOf(tourModel.getBudget()));
                getExpenseById(tourModel);
            }
        });

        binding.addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddExpenseDialog();
            }
        });

        binding.viewExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Bundle bundle1=new Bundle();
                bundle1.putString("tourid",tourId);

                Navigation.findNavController(view).navigate(R.id.action_tourDetailsFragment_to_expenseDetailsFragment,bundle1);


            }
        });

        binding.imageAddIconId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takepictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takepictureIntent,1111);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getContext(), "No camera app found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tourViewModel.fetchMomentsByTourId(tourId).observe(getViewLifecycleOwner(), new Observer<List<MomentsModel>>() {
            @Override
            public void onChanged(List<MomentsModel> momentsModels) {
                final ImageAdapter imageAdapter=new ImageAdapter(getContext(),momentsModels);////////////////////////getActivity///////////////////////////////////
                final GridLayoutManager llm=new GridLayoutManager(getContext(),3);
                binding.momentsRv.setLayoutManager(llm);
                binding.momentsRv.setAdapter(imageAdapter);
                Log.e("TAG", "onChanged:  tour"+momentsModels.size() );
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);
        if (requestCode==1111 && resultCode==RESULT_OK){
            Bundle extras=intentData.getExtras();
            Bitmap imageBitmap= (Bitmap) extras.get("data");

           final StorageReference storageReference= FirebaseStorage.getInstance().getReference();
           final StorageReference photoReference=storageReference.child(tourName+"/"+System.currentTimeMillis());
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            byte [] data =byteArrayOutputStream.toByteArray();

            UploadTask uploadTask=photoReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                }
            });

            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                   if (!task.isSuccessful()){
                       throw task.getException();
                   }
                   return photoReference.getDownloadUrl();
                }




            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri =task.getResult();
                        long time=System.currentTimeMillis();
                        final MomentsModel momentsModel =new MomentsModel(null,tourId,null,time,downloadUri.toString());
                        tourViewModel.addMoments(momentsModel);


                    }else {

                    }
                }
            });

        }

    }











    private void getExpenseById(TourModel tourModel) {
        tourViewModel.fetchExpenseByTourId(tourId).observe(getViewLifecycleOwner(), new Observer<List<ExpenseModel>>() {
            @Override
            public void onChanged(List<ExpenseModel> expenseModels) {
                Log.e("TAG", "onChanged: "+expenseModels.size() );////////////////////////getActivity///////////////////////////////////


                binding.tableTotalExpenseTv.setText(String.valueOf(tourViewModel.getTotalExpense(expenseModels)));
                binding.tableRemainingBudgetTv.setText(String.valueOf(tourModel.getBudget()-tourViewModel.getTotalExpense(expenseModels)));

                if (tourModel.getBudget()<tourViewModel.getTotalExpense(expenseModels)){
                    binding.tableRemainingBudgetTv.setTextColor(getResources().getColor(R.color.primary));
                }



                




            }
        });

    }












    private void showAddExpenseDialog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Expense");
        final View layout=LayoutInflater.from(getActivity()).inflate(R.layout.add_expense_layout,null,true);
        builder.setView(layout);
        AlertDialog dialog=builder.create();
        dialog.show();
        Log.e("TAG","on Dialog");
        final Button addBtn=layout.findViewById(R.id.addBtnInDialog);
        final Button cancelBtn=layout.findViewById(R.id.cancelBtnInDialog);



        final EditText titleET=layout.findViewById(R.id.addExpenseTilte);
        final EditText amountET=layout.findViewById(R.id.addExpenseAmount);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title=titleET.getText().toString();
                final String amount=amountET.getText().toString();
                final long time=System.currentTimeMillis();

                if (title.isEmpty()&&amount.isEmpty()){
                    Toast.makeText(getContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                }else {
                    final ExpenseModel expenseModel=new ExpenseModel(null, tourId,title,Double.parseDouble(amount),time);
                    tourViewModel.createNewExpense(expenseModel);
                    Log.e("TAG","onsetListener");
                    dialog.dismiss();
                }
            }

        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }



}