package com.example.tli_6.prathamapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.model.DoorToDoorModel;

import java.util.List;


public class DoorToDoorAdapter extends RecyclerView.Adapter<DoorToDoorAdapter.MyViewHolder> {
    private List<DoorToDoorModel> doorList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView num_of_youths,family_owner_name;

        public MyViewHolder(View view) {
            super(view);
            num_of_youths = (TextView) view.findViewById(R.id.num_of_youths);
            family_owner_name = (TextView) view.findViewById(R.id.family_owner_name);

        }
    }


    public DoorToDoorAdapter(List<DoorToDoorModel> doorList) {
        this.doorList = doorList;
    }

    @Override
    public DoorToDoorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.door_to_door_row, parent, false);

        return new DoorToDoorAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DoorToDoorAdapter.MyViewHolder holder, int position) {
        DoorToDoorModel doorToDoorModel = doorList.get(position);
        holder.num_of_youths.setText("No. Of Youths : "+doorToDoorModel.getNumOfYouths());
        holder.family_owner_name.setText("Family Owner : "+doorToDoorModel.getFamilyOwnerName());

    }

    @Override
    public int getItemCount() {
        return doorList.size();
    }
}