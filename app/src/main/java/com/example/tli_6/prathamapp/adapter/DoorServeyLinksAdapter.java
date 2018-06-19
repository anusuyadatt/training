package com.example.tli_6.prathamapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.model.DoorToDoorModel;

import java.util.ArrayList;
import java.util.List;


public class DoorServeyLinksAdapter extends RecyclerView.Adapter<DoorServeyLinksAdapter.MyViewHolder> {
    private ArrayList<String> arrayList;
   int youth_position;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button num_of_youths;

        public MyViewHolder(View view) {
            super(view);
            num_of_youths = (Button) view.findViewById(R.id.num_of_youths);

        }
    }


    public DoorServeyLinksAdapter(ArrayList<String> arrayList,int youth_position) {
        this.arrayList = arrayList;
        this.youth_position = youth_position;
    }

    @Override
    public DoorServeyLinksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.youth_info_button, parent, false);

        return new DoorServeyLinksAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DoorServeyLinksAdapter.MyViewHolder holder, int position) {
        String doorToDoorModel;
      if(youth_position != 0)  {
          Log.d("youth_position", "youth_positionAdapter" +youth_position);
          doorToDoorModel = "YOUTH INFORMATION_"+youth_position+"SUBMITTED"; }
      else{ doorToDoorModel = arrayList.get(position); }

        holder.num_of_youths.setText(doorToDoorModel);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}