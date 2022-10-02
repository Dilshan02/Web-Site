package com.example.safeme.Complaints.ComplaintsAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safeme.Complaints.ComplaintHelperClasses.ComplaintHelperClass;
import com.example.safeme.R;

import java.util.ArrayList;
import java.util.List;

public class ComplaintsAdapter extends RecyclerView.Adapter {

    List<ComplaintHelperClass> complaintHelperClasses;

    public ComplaintsAdapter(List<ComplaintHelperClass>complaintHelperClass)
    {
        this.complaintHelperClasses = complaintHelperClass;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_list, parent, false);
        ComplaintHolderClass complaintHolderClass = new ComplaintHolderClass(view);
        return complaintHolderClass;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ComplaintHolderClass complaintHolderClass = (ComplaintHolderClass) holder;
        ComplaintHelperClass complaintHelperClass = complaintHelperClasses.get(position);

        complaintHolderClass.CID.setText("CID : " + complaintHelperClass.getCID());
        complaintHolderClass.Type.setText("Type : " +complaintHelperClass.getCType());
        complaintHolderClass.Date.setText("Date : " +complaintHelperClass.getDate());
        complaintHolderClass.NIC.setText("NIC : " +complaintHelperClass.getNIC());
        complaintHolderClass.Status.setText("Status : " +complaintHelperClass.getStatus());
    }

    @Override
    public int getItemCount() {
        return complaintHelperClasses.size();
    }

    public void filteredList(ArrayList<ComplaintHelperClass>filterList){
        complaintHelperClasses = filterList;
        notifyDataSetChanged();
    }
    public class ComplaintHolderClass extends RecyclerView.ViewHolder{

        TextView CID,Type,NIC,Date,Status;
        ImageView Delete;

        public ComplaintHolderClass(@NonNull View itemView) {
            super(itemView);

            CID = itemView.findViewById(R.id.cid);
            Type = itemView.findViewById(R.id.type);
            NIC = itemView.findViewById(R.id.nic);
            Date = itemView.findViewById(R.id.date);
            Status = itemView.findViewById(R.id.status);
            Delete = itemView.findViewById(R.id.deleteComplaint);
        }
    }
}
