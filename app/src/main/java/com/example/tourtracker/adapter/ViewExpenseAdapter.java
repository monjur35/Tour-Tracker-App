package com.example.tourtracker.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourtracker.R;
import com.example.tourtracker.models.ExpenseModel;
import com.example.tourtracker.viewModels.TourViewModel;

import java.util.List;

import static com.google.gson.reflect.TypeToken.get;

public class ViewExpenseAdapter extends RecyclerView.Adapter<ViewExpenseAdapter.ExpenseViewHolder> {
    private Context context;
    private List<ExpenseModel>expenseModelList;
    private EditDeleteListener listener;



    public ViewExpenseAdapter(Context context, List<ExpenseModel> expenseModelList,Fragment fragment) {
        this.context = context;
        this.expenseModelList = expenseModelList;
        listener= (EditDeleteListener) fragment;
        Log.d("TAG", "ViewExpenseAdapter: "+expenseModelList.size());
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView= LayoutInflater.from(context).inflate(R.layout.expense_row,parent,false);
        Log.e("TAG", "onCreateViewHolder: "+expenseModelList.size() );
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {

        holder.expenseTitle.setText(expenseModelList.get(position).title);
        holder.expenseAmount.setText(String.valueOf(expenseModelList.get(position).amount) );
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showAlertDialogforLongClick(position);


                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        Log.e("TAG", "getItemCount: " +expenseModelList.size());
        return expenseModelList.size();
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder{

        TextView expenseTitle,expenseAmount;



        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseTitle=itemView.findViewById(R.id.expenseTitleId);
            expenseAmount=itemView.findViewById(R.id.amountExpenseId);





        }
    }






    private void showAlertDialogforLongClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("What do you want ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                showAlertDialogfordelete(position);


            }
        }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showCustomDialogForEdit(position);

            }
        });
        builder.create().show();
    }

    private void showCustomDialogForEdit(int position) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        final View layout=LayoutInflater.from(context).inflate(R.layout.add_expense_layout,null,true);

        final EditText expenseTitleinET= layout.findViewById(R.id.addExpenseTilte);
        final EditText amountET=layout.findViewById(R.id.addExpenseAmount);

        final Button add=layout.findViewById(R.id.addBtnInDialog);
        final Button update=layout.findViewById(R.id.updateBtnInDialog);
        final Button cancel=layout.findViewById(R.id.cancelBtnInDialog);

        builder1.setView(layout);

        expenseTitleinET.setText(expenseModelList.get(position).title,TextView.BufferType.EDITABLE);
        amountET.setText(String.valueOf( expenseModelList.get(position).amount),TextView.BufferType.EDITABLE);
        AlertDialog dialog=builder1.create();
        dialog.show();




        add.setVisibility(View.GONE);
        update.setVisibility(View.VISIBLE);

        update.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final String updatedExpenseTitle=expenseTitleinET.getText().toString().trim();
                final String updatedExpenseAmount=amountET.getText().toString().trim();

                if (updatedExpenseTitle.isEmpty() || updatedExpenseAmount.isEmpty()){
                    Toast.makeText(context, "Fill every field", Toast.LENGTH_SHORT).show();

                }
                else {
                    final String expenseId=expenseModelList.get(position).expId;
                    final  double amnt=Double.parseDouble(updatedExpenseAmount);


                    listener.edit(expenseId,updatedExpenseTitle,amnt);
                    dialog.dismiss();


                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });




    }


    private void showAlertDialogfordelete(int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Are you sure to delete this item?");
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                listener.delete(expenseModelList.get(position).expId);

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        builder1.create().show();
    }

    public interface EditDeleteListener{


        void edit(String expId,String title,double amount);
        void delete(String expId);

    }



}
