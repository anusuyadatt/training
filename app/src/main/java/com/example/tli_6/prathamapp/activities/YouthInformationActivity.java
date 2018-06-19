package com.example.tli_6.prathamapp.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.interfaces.MyServiceListener;
import com.example.tli_6.prathamapp.model.Model_TLI_API_Response;
import com.example.tli_6.prathamapp.service.Service_Call;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class YouthInformationActivity extends AppCompatActivity implements android.widget.AdapterView.OnItemSelectedListener {
    EditText youth_name ,youth_contact,youth_age;
    Spinner spinner_gender,spinner_education,spinner_tech_edu,spinner_current_status,spinner_training_interest,spinner_course,spinner_add_more_name;
    Button sbmt_youth;
    SQLiteDatabase appDatabase;
    ContentValues cv,cvLog,cv_login_update;
    String[] genders = new String[]{"Choose", "Male", "Female"};
    String[] education = new String[]{"Choose", "Only till 5th Standard","Only till 8th Standard", "SSC","HSC", "College Dropout","Graduate", "Post Graduate","PHD"};
    String[] tech_edu = new String[]{"Choose", "Yes", "No"};
    String[] current_status = new String[]{"Choose", "Studying", "School/College Dropout", "Working", "Unemployed"};
    String[] training_interest =  new String[]{"Choose", "Yes", "No"};
    String[] course = new String[]{"Choose", "Automative", "Beauty", "Construction", "Electrical", "Healthcare", "Hospitality", "Plumbing", "Other"};
    String[] add_more_name = new String[]{"Choose", "Yes", "No"};
    String  youth_gender,youth_edu,youth_techedu,youth_current_status,interest_in_vocation_training,youth_course,youth_add_more_name;
    public String DoorformData;
    Intent intent;


    private Model_TLI_API_Response model_TLI_api_response;
    String TAG = "VillageInformationActivity";
    ProgressDialog progressDialog;
    JSONObject  jsonDataObjRoot = null;
    long row=0;
    Long row_log;
    AlertDialog.Builder alertDialog ;
    public  String   mobilePattern  = "^[0-9]{2}[0-9]{8}$";
    int  youth_position = 0;
    int i = 0;
    int size;
    int auto_fill_flag = 0;
    int  numberOfYouths =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youth_information);

        final App globalVariable = (App)getApplicationContext();
      /*  if(String.valueOf(globalVariable.MY_UUID_SECURE).equalsIgnoreCase("") ){
            logOut();

        }*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (globalVariable.VillageName.equalsIgnoreCase("")) {
            toolbar.setTitle("VILLAGE : ABC ");
        }
        else {

            toolbar.setTitle("VILLAGE : " + globalVariable.VillageName);
        }
        setSupportActionBar(toolbar);


        AppDatabase dbhelper = AppDatabase.getInstance(YouthInformationActivity.this);
        if (appDatabase == null)
            appDatabase = dbhelper.getWritableDatabase();
        youth_name = (EditText)findViewById(R.id.your_youth_name);
        youth_contact = (EditText)findViewById(R.id.youth_contact);
        youth_age = (EditText)findViewById(R.id.youth_age);
        sbmt_youth = (Button)findViewById(R.id.sbmt_youth);

        spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
        spinner_education = (Spinner) findViewById(R.id.spinner_education);
        spinner_tech_edu = (Spinner) findViewById(R.id.spinner_tech_edu);
        spinner_current_status = (Spinner) findViewById(R.id.spinner_current_status);
        spinner_training_interest = (Spinner) findViewById(R.id.spinner_training_interest);
        spinner_course = (Spinner) findViewById(R.id.spinner_course);
        //   spinner_add_more_name = (Spinner) findViewById(R.id.spinner_add_more_name);

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_stylish_item,genders
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_stylish_item);
        spinner_gender.setAdapter(spinnerArrayAdapter);
        spinner_gender.setOnItemSelectedListener(this);
        final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
                this,R.layout.spinner_stylish_item,education
        );
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_stylish_item);
        spinner_education.setAdapter(spinnerArrayAdapter1);
        spinner_education.setOnItemSelectedListener(this);
        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this,R.layout.spinner_stylish_item,tech_edu
        );
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_stylish_item);
        spinner_tech_edu.setAdapter(spinnerArrayAdapter2);
        spinner_tech_edu.setOnItemSelectedListener(this);
        final ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(
                this,R.layout.spinner_stylish_item,current_status
        );
        spinnerArrayAdapter3.setDropDownViewResource(R.layout.spinner_stylish_item);
        spinner_current_status.setAdapter(spinnerArrayAdapter3);
        spinner_current_status.setOnItemSelectedListener(this);
        final ArrayAdapter<String> spinnerArrayAdapter4 = new ArrayAdapter<String>(
                this,R.layout.spinner_stylish_item,training_interest
        );
        spinnerArrayAdapter4.setDropDownViewResource(R.layout.spinner_stylish_item);
        spinner_training_interest.setAdapter(spinnerArrayAdapter4);
        spinner_training_interest.setOnItemSelectedListener(this);
        final ArrayAdapter<String> spinnerArrayAdapter5 = new ArrayAdapter<String>(
                this,R.layout.spinner_stylish_item,course
        );
        spinnerArrayAdapter5.setDropDownViewResource(R.layout.spinner_stylish_item);
        spinner_course.setAdapter(spinnerArrayAdapter5);
        spinner_course.setOnItemSelectedListener(this);
        final ArrayAdapter<String> spinnerArrayAdapter6 = new ArrayAdapter<String>(
                this,R.layout.spinner_stylish_item,add_more_name
        );
        spinnerArrayAdapter6.setDropDownViewResource(R.layout.spinner_stylish_item);
        //   spinner_add_more_name.setAdapter(spinnerArrayAdapter6);
        //   spinner_add_more_name.setOnItemSelectedListener(this);
        /////////////////////Fetching Data/////////////////////

        /////////////////////Fetching Data/////////////////////

        sbmt_youth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidateForm() == true) {
                    final App globalVariable = (App)getApplicationContext();
                    Log.d("test--", " auto_fill_flag" + auto_fill_flag);
                    if(auto_fill_flag==1){
                        Log.d("test--", "globalVariableauto_fill_flagIF" + auto_fill_flag);
                        globalVariable.submitted_forms--;;}
                        globalVariable.submitted_forms++;

                     numberOfYouths = getIntent().getIntExtra("number_of_youths",0);
                    if(globalVariable.submitted_forms== numberOfYouths){
                        globalVariable.completion_flag = 1;}
                    else{   globalVariable.completion_flag = 0; }
                    int  youth_position = getIntent().getIntExtra("youth_position",0);
                    try {

                        Log.d("test--", " DoorToDoorServeyActivity_Form_data-" + DoorformData);
                        try {
                            JSONArray jsonYouthArray=null;
                            jsonDataObjRoot = new JSONObject(DoorformData);
                            JSONObject jsonDataObjYouth = new JSONObject();
                            jsonDataObjYouth.put("form_type","2");
                            jsonDataObjYouth.put("youth_vill_name", globalVariable.VillageName);
                            jsonDataObjYouth.put("youth_vill_pin_code", globalVariable.VillagePincode);
                            jsonDataObjYouth.put("youth_name",youth_name.getText().toString());
                            jsonDataObjYouth.put("youth_gender",youth_gender);
                            jsonDataObjYouth.put("youth_contact", youth_contact.getText().toString());
                            jsonDataObjYouth.put("youth_age",  youth_age.getText().toString());
                            jsonDataObjYouth.put("youth_edu",youth_edu);
                            jsonDataObjYouth.put("youth_current_status", youth_current_status);
                            jsonDataObjYouth.put("interest_in_vocation_training", interest_in_vocation_training);
                            jsonDataObjYouth.put("youth_course", youth_course);
                            jsonDataObjYouth.put("youth_techedu",youth_techedu);
                            jsonDataObjYouth.put("youth_add_more_name", youth_add_more_name);
                            int y = youth_position+1;
                            if(youth_position==0) {
                                jsonYouthArray = new JSONArray();
                                jsonYouthArray.put(youth_position, jsonDataObjYouth);
                                jsonDataObjRoot.put("youth",jsonYouthArray);
                            }
                            else {
                                jsonYouthArray = jsonDataObjRoot.getJSONArray("youth");
                                jsonYouthArray.put(youth_position, jsonDataObjYouth);
                            }

                            Log.d("test--", "YouthInformationActivityjsonDataObjRoot-" + jsonDataObjRoot);
                            Log.d("test--", "YouthInformationActivitycompletion_flag-" + globalVariable.completion_flag);
                            cv =  new ContentValues();
                            if(globalVariable.completion_flag== 0){
                                cv.put("formCompletionStatus",0);
                                cv.put("youth_submitted_forms",globalVariable.submitted_forms);
                                cv.put("formData",jsonDataObjRoot.toString());

                          //    appDatabase.update("pratham_app_form", cv, "formCompletionStatus = 0,formID=2", null);
                              appDatabase.update("pratham_app_form", cv,  "formCompletionStatus=0 AND formID=2", null);
                                Toast.makeText(getApplicationContext(), "Saved Succesfully", Toast.LENGTH_LONG).show();
                               finish();
                            }
                            else if (globalVariable.completion_flag== 1){
                                cv.put("formCompletionStatus",1);
                                cv.put("formData",jsonDataObjRoot.toString());
                                cv.put("youth_submitted_forms",globalVariable.submitted_forms);

                                Log.d("test--", "YouthInformationActivitycompletion_flag-" + jsonDataObjRoot.toString());

                                new MyAsncTask().execute();

                            }



                        } catch (JSONException e) {
                            Log.d("test--", " auto_fill_flagCat_1" + auto_fill_flag);
                            if(auto_fill_flag>0){
                            if(youth_position==0){  globalVariable.submitted_forms = 0;}
                            else if (youth_position>0){

                            globalVariable.submitted_forms--;}

                            }
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        Log.d("test--", " auto_fill_flagCat_2" + auto_fill_flag);
                        if(auto_fill_flag>0){
                        if(youth_position==0){  globalVariable.submitted_forms = 0;}
                        else if (youth_position>0){

                            globalVariable.submitted_forms--;}}
                        e.printStackTrace();
                    }

                }
            }
        });





    }

    private void logOut() {
        final App globalVariable = (App)getApplicationContext();

        ////////////////////////////Call Api////////////////////////
        JSONObject jsonObjLog= new JSONObject();
        try {
            jsonObjLog.put("mobilizer_code",globalVariable.mobilizer_code );
            jsonObjLog.put("mobilizer_number",globalVariable.mobilizer_number);
            jsonObjLog.put("latitude", globalVariable.latitude);
            jsonObjLog.put("longitude", globalVariable.longitude);
            jsonObjLog.put("time",  globalVariable.current_date );
            jsonObjLog.put("status",0);
            Log.d("test--",  TAG+"--registrationlogout-jsonObjLog-" + jsonObjLog);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        cvLog = new ContentValues();
        cvLog.put("mobilizer_code", globalVariable.mobilizer_code);
        cvLog.put("mobilizer_number", globalVariable.mobilizer_number);
        cvLog.put("current_latitude", globalVariable.latitude);
        cvLog.put("current_longitude", globalVariable.longitude);
        cvLog.put("current_login_time", globalVariable.current_date);
        cvLog.put("status", 0);

        new Service_Call(getApplicationContext(), "/mobilizer/log", jsonObjLog, false, new MyServiceListener() {
            @Override
            public void getServiceData(Model_TLI_API_Response api_response) {
                String result = api_response.data;
                model_TLI_api_response = api_response;

                Log.d("test--", TAG + "--result data1 --" + result);

                Log.d("test--", TAG + "--responsecode1" + model_TLI_api_response.response_code);
                Log.d("test--", TAG + "--responsecode1HttpsURLConnection.HTTP_OK" + HttpsURLConnection.HTTP_OK);
                try {
                    if (model_TLI_api_response.response_code == HttpsURLConnection.HTTP_OK) {
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(result);

                        Log.d("test--", TAG + "--registrationlogoutstatus1--" + jsonObject.getString("status"));
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            jsonObject.getString("message");


                            Log.d("test--", TAG + "--registrationlogoutstatus2--" +   jsonObject.getString("status"));
                            Log.d("test--", TAG + "--registrationlogoutmessage- --" +   jsonObject.getString("message"));
                            //////////////////inserting registration data/////////////////
                            cvLog.put("syncStatus", 1);
                            row_log = appDatabase.insert("user_login_log", null, cvLog);
                        }
                        else {
                            cvLog.put("syncStatus", 0);
                            row_log = appDatabase.insert("user_login_log", null, cvLog);
                        }


                    }
                    else {
                        cvLog.put("syncStatus", 0);
                        row_log = appDatabase.insert("user_login_log", null, cvLog);

                    }
                    if(row_log>0) {
                        Toast.makeText(getApplicationContext(),"You have logged out successfully",Toast.LENGTH_LONG).show();

                        globalVariable.mobilizer_code= "";
                        globalVariable.mobilizer_number = "";

                        globalVariable.VillageName = "";
                        globalVariable.mobilizerName ="";
                        globalVariable.VillagePincode = 0;
                        globalVariable.family_owner_name = "";
                        globalVariable.number_of_youths = 0;
                        globalVariable.MY_UUID_SECURE = null;
                        globalVariable.submitted_forms = 0;
                        Intent intent = new Intent(getApplicationContext(), RegisterationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
                }
            }
        });
        ////////////////////////////Call Api////////////////////////


    }
    @Override
    public void onItemSelected(android.widget.AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {

            case R.id.spinner_gender:
                youth_gender  = genders[i];

                break;

            case R.id.spinner_education:
                youth_edu =  education[i];

                break;

            case R.id.spinner_tech_edu:
                youth_techedu =  tech_edu[i];

                break;
            case R.id.spinner_current_status:
                youth_current_status =   current_status[i];

                break;
            case R.id.spinner_training_interest:
                interest_in_vocation_training =  training_interest[i];

                break;

            case R.id.spinner_course:
                youth_course =  course[i];

                break;
       /*  case R.id.spinner_add_more_name:
           youth_add_more_name =   add_more_name[i];

            break;*/

        }


    }

    @Override
    public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

    }

    private boolean ValidateForm() {
        alertDialog = new AlertDialog.Builder(YouthInformationActivity.this);
        if(youth_name.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Youth Name.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    youth_name.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else   if(youth_gender.equalsIgnoreCase("Choose")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Select Youth Gender.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    spinner_gender.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(youth_contact.getText().toString().matches(mobilePattern) == false){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Valid Youth Contact details.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    youth_contact.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else  if(youth_age.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Youth Age.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    youth_age.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else   if(youth_edu.equalsIgnoreCase("Choose")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Select Education.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    spinner_education.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else   if(youth_techedu.equalsIgnoreCase("Choose")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Select Youth Technical Education.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    spinner_tech_edu.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else   if(youth_current_status.equalsIgnoreCase("Choose")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Select Youth Current Status.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    spinner_current_status.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else   if(interest_in_vocation_training.equalsIgnoreCase("Choose")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Select Youth Interest.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    spinner_training_interest.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
       /* else   if(youth_course.equalsIgnoreCase("Choose")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Select Course.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    spinner_course.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }*/

        else {
            return true;
        }
    }

    private class MyAsncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            savingData();
            return null;
        }


        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(YouthInformationActivity.this);
            progressDialog.setMessage("Please wait........");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
        }
    }

    private void savingData() {
        new Service_Call(getApplicationContext(), "/form/save", jsonDataObjRoot, false, new MyServiceListener() {
            @Override
            public void getServiceData(Model_TLI_API_Response api_response) {
                String result = api_response.data;
                model_TLI_api_response = api_response;

                Log.d("test--", TAG + "--result data1 --" + result);

                Log.d("test--", TAG + "--responsecode1" + model_TLI_api_response.response_code);
                Log.d("test--", TAG + "--responsecode1HttpsURLConnection.HTTP_OK" + HttpsURLConnection.HTTP_OK);
                try {
                    if (model_TLI_api_response.response_code == HttpsURLConnection.HTTP_OK) {

                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(result);

                        Log.d("test--", TAG + "--registrationyouthstatus1--" + jsonObject.getString("status"));
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            jsonObject.getString("message");
                            Log.d("test--", TAG + "--registrationyouthtatus2--" +   jsonObject.getString("status"));
                            Log.d("test--", TAG + "--registrationyouthmessage- --" +   jsonObject.getString("message"));
                            cv.put("syncStatus",1);
                            row =   appDatabase.update("pratham_app_form", cv, "formCompletionStatus=0 AND formID=2", null);
                            }



                        else {
                            cv.put("syncStatus", 0);
                            row = appDatabase.update("pratham_app_form", cv, "formCompletionStatus=0 AND formID=2", null);

                        }
                    }
                    else if ( model_TLI_api_response.response_code==409){
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(YouthInformationActivity.this);
                        alertDialog.setTitle("VALIDATION");
                        alertDialog.setMessage("Conflict, user sending data from a disabled device");

                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                                return;
                            }
                        });


                        alertDialog.show();


                    }
                    else {    cv.put("syncStatus",0);
                        row =   appDatabase.update("pratham_app_form", cv, "formCompletionStatus=0 AND formID=2", null);
                    }
                    if(row>0) {
                        Toast.makeText(getApplicationContext(), "All Youth FormsSaved Succesfully", Toast.LENGTH_LONG).show();
                        final App globalVariable = (App)getApplicationContext();
                        globalVariable.submitted_forms = 0;
                        numberOfYouths=0;

                        Intent intent = new Intent(YouthInformationActivity.this,DoorToDoorServeyActivity.class);
                      //  startActivity(intent);
                        finish();
                    }
                    else{
                        Log.d("test--", " auto_fill_flagCat_3" + auto_fill_flag);
                        final App globalVariable = (App)getApplicationContext();
                        if(auto_fill_flag>0){
                        if(youth_position==0){  globalVariable.submitted_forms = 0;}
                        else if (youth_position>0){
                            globalVariable.submitted_forms--;}}
                        Toast.makeText(getApplicationContext(), "Not Saved Succesfully", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.d("test--", " auto_fill_flagCat_3" + auto_fill_flag);
                    final App globalVariable = (App)getApplicationContext();
                    if(auto_fill_flag>0){
                    if(youth_position==0){  globalVariable.submitted_forms = 0;}
                    else if (youth_position>0){
                        globalVariable.submitted_forms--;}}
                    Toast.makeText(getApplicationContext(), "Something Goes Wrong.Please Try Again.", Toast.LENGTH_LONG).show();
                }
            }
        });

             finish();


    }


    private int  getElementPosition(String searchKey, String[] arr ) {
        size = arr.length;
        Log.d("test--", " DoorToDoorServeysearchKey" + searchKey);
        Log.d("test--", " spinnerarr" + arr);
        Log.d("test--", " spinnersize" + size);
        for ( i = 0; i < size; i++) {
            if (arr[i].equalsIgnoreCase(searchKey))
                break;
        }

        if(i==size)
            return  0;
        else
            return i;



    }

  public  void   prepareData(){
      final App globalVariable = (App)getApplicationContext();
      Cursor packcur = appDatabase.rawQuery("Select * from pratham_app_form where UID= '"+String.valueOf(globalVariable.MY_UUID_SECURE)+"' and formID = 2 and formCompletionStatus = 0  ", null);
      if(packcur.getCount()>0){

          /* globalVariable.submitted_forms--;*/
          while (packcur.moveToNext()) {
              DoorformData = packcur.getString(packcur.getColumnIndex("formData"));
              Log.d("test--", " DoorToDoorServeyCursorDoorformData-" + DoorformData);
              int  youth_position = getIntent().getIntExtra("youth_position",0);
              try {
                  JSONObject jsonObject = new JSONObject(DoorformData);
                  JSONArray jsonArray = jsonObject.getJSONArray("youth");
                  JSONObject youthObject = jsonArray.getJSONObject(youth_position);
                  youth_name.setText(youthObject.optString("youth_name"));
                  youth_contact.setText(youthObject.optString("youth_contact"));
                  youth_age.setText(youthObject.optString("youth_age"));
                  if(youthObject.optInt("youth_age")>0){ auto_fill_flag =1;
                    }
                  /////////////Setting Spinner Data/////////////////////
                  int youth_gender_pos   =  getElementPosition(youthObject.optString("youth_gender"),new String[]{"Choose", "Male", "Female"});
                  spinner_gender.setSelection(youth_gender_pos);
                  int youth_edu_pos   =  getElementPosition(youthObject.optString("youth_edu"),new String[]{"Choose", "Only till 5th Standard","Only till 8th Standard", "SSC","HSC", "College Dropout","Graduate", "Post Graduate","PHD"});
                  spinner_education.setSelection(youth_edu_pos);
                  int youth_techedu_pos   =  getElementPosition(youthObject.optString("youth_techedu"), new String[]{"Choose", "Yes", "No"});
                  spinner_tech_edu.setSelection(youth_techedu_pos);
                  int youth_current_status_pos   =  getElementPosition(youthObject.optString("youth_current_status"), new String[]{"Choose", "Studying", "School/College Dropout", "Working", "Unemployed"});
                  spinner_current_status.setSelection(youth_current_status_pos);
                  int interest_in_vocation_training_pos   =  getElementPosition(youthObject.optString("interest_in_vocation_training"), new String[]{"Choose", "Yes", "No"});
                  spinner_training_interest.setSelection(interest_in_vocation_training_pos);
                  int youth_course_pos   =  getElementPosition(youthObject.optString("youth_course"), new String[]{"Choose", "Automative", "Beauty", "Construction", "Electrical", "Healthcare", "Hospitality", "Plumbing", "Other"});
                  spinner_course.setSelection(youth_course_pos);

              } catch (JSONException e) {
                  e.printStackTrace();
              }
              /////////////Setting Spinner Data/////////////////////
          }
      }

  }

    @Override
    protected void onResume() {
        prepareData();
        super.onResume();
    }

}
