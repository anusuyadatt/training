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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.tli_6.prathamapp.model.StudentModel;
import com.example.tli_6.prathamapp.utilities.WebUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentDetailsActivity extends AppCompatActivity implements HttpCommunicationListener, AdapterView.OnItemSelectedListener {
    Button submit_but;
    EditText sender_edt, village_pin, village_edt, std_name_edt;
    CheckBox rojar_chbxt;
    EditText mcode_txt;
    ProgressDialog mProgressDialog;
    HttpCommunicationLayer httpCommunicationLayer;
    StudentModel studentModel;
    String code;
    String program_code = "";
    Spinner program_spinner;
    ArrayList<String> programList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        App.getInstance().createDirectory();
        mProgressDialog = new ProgressDialog(this);
        submit_but = (Button) findViewById(R.id.submit_mob_actvy);
        submit_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        sender_edt = (EditText) findViewById(R.id.sender_edt);
        village_pin = (EditText) findViewById(R.id.village_pin);
        village_edt = (EditText) findViewById(R.id.village_edt);
        std_name_edt = (EditText) findViewById(R.id.std_name_edt);
        program_spinner = (Spinner) findViewById(R.id.pro_code_spnr);
        rojar_chbxt = (CheckBox) findViewById(R.id.rojar_chbx);
        mcode_txt = (EditText) findViewById(R.id.mcode_txt);
        code = App.getInstance().getMCode();
        mcode_txt.setText(code);
        populateList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, programList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        program_spinner.setOnItemSelectedListener(this);
        program_spinner.setAdapter(arrayAdapter);
        ActionBar actionBar=getSupportActionBar();

        actionBar.setTitle(getString(R.string.app_name)+" : "+getString(R.string.std_act));
    }

    void submit() {
        if (validateForm()) {
            if (App.getInstance().checkConnectivity()) {
                if (rojar_chbxt.isChecked())
                    confirmDialog();
                else
                   proceed();
            } else
                showAlertDialog("Network Error", "Internet not available");
        }

    }

    void populateList() {
        programList = new ArrayList<String>();
        Cursor cursor = AppDatabase.getDatabaseInstance().query(Constants.PROGRAM_LIST_TABLE, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("program_name"));
                String code = cursor.getString(cursor.getColumnIndex("program_code"));
                programList.add(name + " (" + code + ")");
            }
        }
        if (cursor != null)
            cursor.close();

    }

    boolean validateForm() {
        Toast toast;
        if (sender_edt.getText().toString().isEmpty() || village_pin.getText().toString().isEmpty() || village_edt.getText().toString().isEmpty() || std_name_edt.getText().toString().isEmpty()
                || program_code.isEmpty()) {
           toast= Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.LEFT,0,0);
            toast.show();
            return false;
        } else {

           String regex="[6789]\\d{9}";
            Pattern pattern= Pattern.compile(regex);
           Matcher matcher= pattern.matcher(sender_edt.getText().toString());



            if(matcher.matches() );
            else{
                toast= Toast.makeText(this, "phone number is not valid ", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.LEFT,0,0);
                toast.show();
            return false;
            }


            String regex1="[1-9]\\d{5}";
            Pattern pattern1= Pattern.compile(regex1);
            Matcher matcher1= pattern1.matcher(village_pin.getText().toString());

            if(matcher1.matches() );
            else{
                toast= Toast.makeText(this, "pin code is not valid ", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.LEFT,0,0);
                toast.show();
                return false;
            }

        }
        studentModel = new StudentModel();
        studentModel.setMcode(mcode_txt.getText().toString());
        studentModel.setSender(sender_edt.getText().toString());
        studentModel.setVillage(village_pin.getText().toString()+ " "+village_edt.getText().toString());
        studentModel.setStudentName(std_name_edt.getText().toString());
        studentModel.setProgramCode(program_code);
        if (rojar_chbxt.isChecked())
            studentModel.setRojarMitra("R");
        else
            studentModel.setRojarMitra("");
        studentModel.setDeviceId(WebUtils.getDeviceId());
        studentModel.setMessageId(WebUtils.getMessageID());
        studentModel.setMobileTimestamp(WebUtils.getTimeStamp());
        studentModel.setActivityDate(WebUtils.getDate(WebUtils.getTimeStamp()));
        return true;
    }

    public void onReceivingResponse(String response, int responseCode) {
        mProgressDialog.dismiss();
        Toast toast;
        if (responseCode != 0) {
            studentModel.setServerResponse(response);
            HttpResponseModel responseModel = new ProcessResponse().processStudentResponse(studentModel);
            if (responseModel.getResposneCode() != 0) {
                toast= Toast.makeText(this,  responseModel.getResposne(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.LEFT,0,0);
                toast.show();
                finish();
            } else
                showAlertDialog("Error ", responseModel.getResposne());
        } else
            showAlertDialog("Error", response);
    }

    @Override
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

    void confirmDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you sure this student comes under rojar mitra?");
        alertDialog.setIcon(R.drawable.alt);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                proceed();
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        alertDialog.show();
    }

    void proceed() {
        if(!App.getInstance().isBackgroungProcessRunning()) {
            showDialog(Constants.REGISTER);
            httpCommunicationLayer = new HttpCommunicationLayer();
            httpCommunicationLayer.setHttpCommunicationListener(this);
            httpCommunicationLayer.connectToServer(Constants.STUDENT_DETAILS_SUBMIT, studentModel);
        }
        else{
            Toast toast=Toast.makeText(this,getString(R.string.backgn_process),Toast.LENGTH_LONG);
          toast.setGravity(Gravity.TOP|Gravity.LEFT,0,0);
            toast.show();
            }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
        program_code = program_spinner.getSelectedItem().toString();
        int i = program_code.indexOf("(");
        int j = program_code.indexOf(")");
        program_code = program_code.substring(i + 1, j);


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}