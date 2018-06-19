package com.example.tli_6.prathamapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.activities.DailyActivity;
import com.example.tli_6.prathamapp.model.DailyActivityModel;
import com.example.tli_6.prathamapp.model.StudentModel;
import com.example.tli_6.prathamapp.model.UserModel;

import java.util.ArrayList;

/**
 * Created by tli-6 on 5/19/17.
 */
public class MobiliserAdapter extends ArrayAdapter<DailyActivityModel> {
    private Context context;
    private int layoutResource;
    private ArrayList<DailyActivityModel> arrayList;

    public MobiliserAdapter(Context context, int layoutResource, ArrayList<DailyActivityModel> models) {
        super(context, layoutResource, models);
        this.context = context;
        this.layoutResource = layoutResource;
        this.arrayList = models;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TextView textView1 = null;
        TextView textView2 = null;
        TextView textView3 = null;
        TextView textView4 = null;
        TextView textView5 = null;
        TextView textView6 = null;
        TextView textView7 = null;
        TextView textView8 = null;
        TextView textView9 = null;
        TextView textView10 = null;
        TextView textView11 = null;
        TextView textView12 = null;
        TextView textView13 = null;
        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);
            textView1 = (TextView) row.findViewById(R.id.text1);
            textView2 = (TextView) row.findViewById(R.id.text2);
            textView3 = (TextView) row.findViewById(R.id.text3);
            textView4 = (TextView) row.findViewById(R.id.text4);
            textView5 = (TextView) row.findViewById(R.id.text5);
            textView6 = (TextView) row.findViewById(R.id.text6);
            textView7 = (TextView) row.findViewById(R.id.text7);
            textView8 = (TextView) row.findViewById(R.id.text8);
            textView9 = (TextView) row.findViewById(R.id.text9);
            textView10 = (TextView) row.findViewById(R.id.text10);
            textView11 = (TextView) row.findViewById(R.id.text11);
            textView12 = (TextView) row.findViewById(R.id.text12);
            textView13 = (TextView) row.findViewById(R.id.text13);
        } else {
            textView1 = (TextView) row.findViewById(R.id.text1);
            textView2 = (TextView) row.findViewById(R.id.text2);
            textView3 = (TextView) row.findViewById(R.id.text3);
            textView4 = (TextView) row.findViewById(R.id.text4);
            textView5 = (TextView) row.findViewById(R.id.text5);
            textView6 = (TextView) row.findViewById(R.id.text6);
            textView7 = (TextView) row.findViewById(R.id.text7);
            textView8 = (TextView) row.findViewById(R.id.text8);
            textView9 = (TextView) row.findViewById(R.id.text9);
            textView10 = (TextView) row.findViewById(R.id.text10);
            textView11 = (TextView) row.findViewById(R.id.text11);
            textView12 = (TextView) row.findViewById(R.id.text12);
            textView13 = (TextView) row.findViewById(R.id.text13);
        }
        textView11.setVisibility(View.VISIBLE);
        textView12.setVisibility(View.VISIBLE);
        textView3.setVisibility(View.GONE);
        textView4.setVisibility(View.GONE);
        DailyActivityModel dailyActivityModel = arrayList.get(position);
        textView1.setText("Sent DateTime");
        textView3.setText("Sender ");
        textView5.setText("Village Name");
        textView7.setText("No. Students Reached");
        textView9.setText("No. Students Assessed");
        textView11.setText("No. Students Ready To Join");

        textView13.setText(dailyActivityModel.getServerResponse());

        textView2.setText(dailyActivityModel.getMobileTimestamp());
        textView4.setText(dailyActivityModel.getSender());
        textView6.setText(dailyActivityModel.getVillage());
        textView8.setText(dailyActivityModel.getStudentsReached());
        textView10.setText(dailyActivityModel.getStudentsAssessed());
        textView12.setText(dailyActivityModel.getStudentsReadyToJoin());
        return row;
    }
    public  void refreshList(ArrayList<DailyActivityModel> models){
        arrayList.clear();
        arrayList.addAll(models);
        notifyDataSetChanged();

    }
}

