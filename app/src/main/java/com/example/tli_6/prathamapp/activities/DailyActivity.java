package com.example.tli_6.prathamapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tli_6.prathamapp.Constants;
import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.listener.HttpCommunicationListener;
import com.example.tli_6.prathamapp.logic.HttpCommunicationLayer;
import com.example.tli_6.prathamapp.logic.ProcessResponse;
import com.example.tli_6.prathamapp.model.DailyActivityModel;
import com.example.tli_6.prathamapp.model.HttpResponseModel;
import com.example.tli_6.prathamapp.utilities.WebUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DailyActivity extends AppCompatActivity implements HttpCommunicationListener {
Button submit_but;
    EditText sender_edt,village_edt,std_reached_edt,std_ass_edt,std_ready_edt;
    EditText mcode_txt;
    ProgressDialog mProgressDialog;
    HttpCommunicationLayer httpCommunicationLayer;
    DailyActivityModel dailyActivityModel;
    String code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        mProgressDialog=new ProgressDialog(this);
       submit_but=  (Button)findViewById(R.id.submit_mob_actvy);
        submit_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        sender_edt=(EditText)findViewById(R.id.sender_edt);
        village_edt=(EditText)findViewById(R.id.village_edt);
        std_reached_edt=(EditText)findViewById(R.id.std_reached__edt);
        std_ass_edt=(EditText)findViewById(R.id.std_ass_edt);
        std_ready_edt=(EditText)findViewById(R.id.std_ready__edt);
        mcode_txt=(EditText) findViewById(R.id.mcode_txt);
        code=App.getInstance().getMCode();
mcode_txt.setText(code);
        ActionBar actionBar=getSupportActionBar();

        actionBar.setTitle(getString(R.string.app_name)+" : "+getString(R.string.daily_act));
    }
    void submit(){
        if(validateForm()){
            if (App.getInstance().checkConnectivity()) {
                if(!App.getInstance().isBackgroungProcessRunning()) {
                    showDialog(Constants.REGISTER);
                /*mProgressView.setVisibility(true ? View.VISIBLE : View.GONE);
                mLoginFormView.setVisibility(true ? View.GONE : View.VISIBLE);*/
                    httpCommunicationLayer = new HttpCommunicationLayer();
                    httpCommunicationLayer.setHttpCommunicationListener(this);
                    httpCommunicationLayer.connectToServer(Constants.DAILY_ACTIVITY_SUBMIT, dailyActivityModel);
                }
                else{
                    Toast toast=Toast.makeText(this,getString(R.string.backgn_process),Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.LEFT,0,0);
                    toast.show();
                }
            } else
                showAlertDialog("Network Error", "Internet not available");
        }

    }
    boolean validateForm(){
        Toast toast;
        if(sender_edt.getText().toString().isEmpty()||village_edt.getText().toString().isEmpty()||std_reached_edt.getText().toString().isEmpty()
                ||std_ass_edt.getText().toString().isEmpty()||std_ready_edt.getText().toString().isEmpty())
        {
            toast= Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                   toast.show();
            return  false;
        }

        else {
            try {
                if(std_reached_edt.getText().toString().length()<=4);
                else
                {
                    toast=  Toast.makeText(this, "No of student reach should less than or equal four digit ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                    toast.show();
                    return false;
                }
                int std_reached = Integer.parseInt(std_reached_edt.getText().toString());
                if (std_reached >= 0) ;
                else {
                  toast=  Toast.makeText(this, "Only 0 or positive number allowed in student reached field ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                    toast.show();
                    return false;
                }

                if(std_ass_edt.getText().toString().length()<=4);
                else
                {
                    toast=  Toast.makeText(this, "No of student asses should less than or equal four digit ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                    toast.show();
                    return false;
                }
                int std_assesed = Integer.parseInt(std_ass_edt.getText().toString());
                if (std_assesed >= 0) ;
                else {
                   toast=  Toast.makeText(this, "Only 0 or positive number allowed  in student assess field", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                    toast.show();
                    return false;
                }
                if(std_ready_edt.getText().toString().length()<=4);
                else
                {
                    toast=  Toast.makeText(this, "No of student ready to join should less than or equal to four digit ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                    toast.show();
                    return false;
                }
                int std_ready = Integer.parseInt(std_ready_edt.getText().toString());
                if (std_ready>= 0) ;
                else {
                   toast= Toast.makeText(this, "Only 0 or positive number allowed  in student ready to join field", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                    toast.show();
                    return false;
                }
                if(std_ready>std_reached||std_assesed>std_reached)
                {
                    toast=Toast.makeText(this,"Assessed field and reached field should not be more than reached field",Toast.LENGTH_LONG);
                 toast.setGravity(Gravity.TOP|Gravity.LEFT,0,0);
                    toast.show();
                    return false;
                }
            }
            catch (NumberFormatException ne){
              toast= Toast.makeText(this,"Enter only number in last 3 fields",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                toast.show();
            }
                String regex="[6789]\\d{9}";
                Pattern pattern= Pattern.compile(regex);
                Matcher matcher= pattern.matcher(sender_edt.getText().toString());

                if(matcher.matches());
                else{
                   toast= Toast.makeText(this,"phone number is not valid ",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                    toast.show();
                    return false;
                }
        }
        dailyActivityModel=new DailyActivityModel();

        dailyActivityModel.setMcode(mcode_txt.getText().toString());
        dailyActivityModel.setSender(sender_edt.getText().toString());
        dailyActivityModel.setVillage(village_edt.getText().toString());
        dailyActivityModel.setStudentsRached(std_reached_edt.getText().toString());
        dailyActivityModel.setStudentsAsessed(std_ass_edt.getText().toString());
        dailyActivityModel.setStudentsReadyToJoin(std_ready_edt.getText().toString());
        dailyActivityModel.setDeviceId(WebUtils.getDeviceId());
        dailyActivityModel.setMessageId(WebUtils.getMessageID());
        dailyActivityModel.setMobileTimestamp(WebUtils.getTimeStamp());
        dailyActivityModel.setActivityDate(WebUtils.getDate(WebUtils.getTimeStamp()));
        return  true;
    }
  public   void onReceivingResponse(String response,int responseCode){
      mProgressDialog.dismiss();
      if (responseCode != 0) {
          Toast toast;
          dailyActivityModel.setServerResponse(response);
          HttpResponseModel responseModel=new ProcessResponse().processDailyActivityResponse(dailyActivityModel);
          if(responseModel.getResposneCode()!=0) {
             toast= Toast.makeText(this,responseModel.getResposne(),Toast.LENGTH_LONG);
              toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
              toast.show();
              finish();
          }
          else
              showAlertDialog("Error ",responseModel.getResposne());
      } else
          showAlertDialog("Error", response);
  }    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Constants.REGISTER:



                DialogInterface.OnClickListener loadingButtonListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                httpCommunicationLayer.cancelTask();
                                dialog.dismiss();

                            }
                        };
                mProgressDialog.setTitle("Connecting to server");
                mProgressDialog.setMessage("Please wait,it will take some time");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setButton(getString(R.string.cancel), loadingButtonListener);
                return mProgressDialog;


        }

        return null;
    }
    void showAlertDialog(String title, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.alt);
        alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        alertDialog.show();
    }
}
