package com.example.expensemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import Model.Data;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {


    //Firebase DB

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;

    //TextView
    private TextView incomeTotalSum;

    //Recyclerview

    private RecyclerView recyclerView;
    private RecyclerView FirebaseRecyclerAdapter;

    //Update Edit view

    private EditText editName;
    private EditText editAmount;
    private EditText editType;
    private EditText editNote;

    //Button For Update and delete
    private Button btnUpdate;
    private Button btnDelete;

    //Data item value

    private String type;
    private String note;
    private String name;
    private int amount;

    private String post_key;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview =  inflater.inflate(R.layout.fragment_income, container, false);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeDatabase").child(uid);

        incomeTotalSum=myview.findViewById(R.id.income_txt_result);

        incomeTotalSum=myview.findViewById(R.id.income_txt_result);

        recyclerView=myview.findViewById(R.id.recycler_id_income);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalvalue = 0;
                for (DataSnapshot mynapshot:dataSnapshot.getChildren()){

                    Data data = mynapshot.getValue(Data.class);
                    totalvalue  += data.getAmount();
                    String stTotalvalue = String.valueOf(totalvalue);

                    incomeTotalSum.setText(stTotalvalue+".00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Error Here
        FirebaseRecyclerOptions<Data> options=
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mIncomeDatabase,Data.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> firebaseRecyclelerAdapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final Data model) {

                holder.setAmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNode());
                holder.setAmount(model.getAmount());
                holder.setDate(model.getData());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key=getRef(position).getKey();

                        amount=model.getAmount();
                        type=model.getType();
                        note=model.getNode();



                        updateDataItem();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclelerAdapter);


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        private void setNote(String note){
            TextView mNote=mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }
        private void setAmount(int amount){
            TextView mAmount=mView.findViewById(R.id.amount_txt_income);
            String stamount=String.valueOf(amount);
            mAmount.setText(stamount);
        }
    }


    private void updateDataItem(){
        androidx.appcompat.app.AlertDialog.Builder mydialog=new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myview);

        editAmount=myview.findViewById(R.id.amount_edt);
        editType=myview.findViewById(R.id.type_edt);
        editNote=myview.findViewById(R.id.note_edt);


        //Set data to edit text


        editAmount.setText(String.valueOf(amount));
        editAmount.setSelection(String.valueOf(amount).length());
        editType.setText(type);
        editType.setSelection(type.length());
        editNote.setText(note);
        editNote.setSelection(note.length());

        btnUpdate=myview.findViewById(R.id.btnUP_update);
        btnDelete=myview.findViewById(R.id.btnUP_del);

        final AlertDialog dialog = mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type=editType.getText().toString().trim();
                note=editNote.getText().toString().trim();

                String mdamount=String.valueOf(amount);
                mdamount=editAmount.getText().toString().trim();

                int myamount= Integer.parseInt(mdamount);
                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data= new Data(myamount,type,note,post_key,mDate);
                mIncomeDatabase.child(post_key).setValue(data);
                dialog.dismiss();

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIncomeDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });
        dialog.show();

    }
}