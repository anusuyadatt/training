package com.example.tli_6.prathamapp.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import  android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
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

import static com.example.tli_6.prathamapp.application.App.appDatabase;

public class DoorToDoorServeyActivity extends AppCompatActivity   implements android.widget.AdapterView.OnItemSelectedListener {
    EditText family_owner_name,door_contact,total_family_size,num_earning_members,edit_16_to_18,num_of_youths;
    Button  sbmt_door ;

    String TAG = "DoorToDoorServeyActivity";
    android.content.ContentValues cv;
    int update_flag = 0;
    long row = 0 ;
    String DoorformData = "";
    private Model_TLI_API_Response model_TLI_api_response;
    ContentValues cvLog;
    Long row_log;
    AlertDialog.Builder alertDialog ;
    public  String   mobilePattern  = "^[0-9]{2}[0-9]{8}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_to_door_servey);
        final App globalVariable = (App)getApplicationContext();
        globalVariable.number_of_youths= 0;
        globalVariable.family_owner_name ="";
       /* if(String.valueOf(globalVariable.MY_UUID_SECURE).equalsIgnoreCase("") ){
            logOut();

        }*/
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (globalVariable.VillageName.equalsIgnoreCase("")) {
            toolbar.setTitle("VILLAGE : ABC ");
        }
        else {

            toolbar.setTitle("VILLAGE : " + globalVariable.VillageName);
        }
        setSupportActionBar(toolbar);
        AppDatabase dbhelper = AppDatabase.getInstance(DoorToDoorServeyActivity.this);
        if (appDatabase == null)
            appDatabase = dbhelper.getWritableDatabase();
        family_owner_name = (EditText)findViewById(R.id.family_owner_name);
        door_contact = (EditText)findViewById(R.id.door_contact);
        total_family_size = (EditText)findViewById(R.id.total_family_size);
        num_earning_members = (EditText)findViewById(R.id.num_earning_members);
        edit_16_to_18 = (EditText)findViewById(R.id.edit_16_to_18);
        num_of_youths = (EditText)findViewById(R.id.num_of_youths);
        sbmt_door = (Button)findViewById(R.id.sbmt_door);


        ///////////////////////////Fetching  Data/////////////////////////////////




        sbmt_door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateForm() == true){


                    JSONObject jsonDataObj1 = new JSONObject();
                    try {
                        jsonDataObj1.put("form_type","2");
                        jsonDataObj1.put("door_vill_name", globalVariable.VillageName);
                        jsonDataObj1.put("door_pincode", globalVariable.VillagePincode);

                        jsonDataObj1.put("family_owner_name",family_owner_name.getText().toString());
                        jsonDataObj1.put("contact_details",door_contact.getText().toString());
                        jsonDataObj1.put("family_size", total_family_size.getText().toString());
                        jsonDataObj1.put("earning_members", num_earning_members.getText().toString());
                        jsonDataObj1.put("member_16_to_18", edit_16_to_18.getText().toString());
                        jsonDataObj1.put("member_18_to_35", num_of_youths.getText().toString());
                        jsonDataObj1.put("num_of_youths", num_of_youths.getText().toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    cv = new ContentValues();
                    cv.put("formID", 2);
                    cv.put("UID", String.valueOf(globalVariable.MY_UUID_SECURE));
                    cv.put("formVersion", 1);
                    cv.put("villageName",  globalVariable.VillageName);
                    cv.put("familyOwner",  family_owner_name.getText().toString() );
                    cv.put("numOfYouths", num_of_youths.getText().toString());
                    cv.put("syncStatus",0);
                    cv.put("formCompletionStatus",0);

                    if(update_flag==0){
                        cv.put("formData",jsonDataObj1.toString());
                        row = appDatabase.insert("pratham_app_form", null, cv);
                    }
                    else if (update_flag==1){
                        try {
                            JSONObject jsonObjectUpadte = new JSONObject(DoorformData);
                            jsonObjectUpadte.put("form_type","2");
                            jsonObjectUpadte.put("door_vill_name", globalVariable.VillageName);
                            jsonObjectUpadte.put("door_pincode", globalVariable.VillagePincode);
                            jsonObjectUpadte.put("family_owner_name",family_owner_name.getText().toString());
                            jsonObjectUpadte.put("contact_details",door_contact.getText().toString());
                            jsonObjectUpadte.put("family_size", total_family_size.getText().toString());
                            jsonObjectUpadte.put("earning_members", num_earning_members.getText().toString());
                            jsonObjectUpadte.put("member_16_to_18", edit_16_to_18.getText().toString());
                            jsonObjectUpadte.put("member_18_to_35", num_of_youths.getText().toString());
                            jsonObjectUpadte.put("num_of_youths", num_of_youths.getText().toString());
                            cv.put("formData",jsonObjectUpadte.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        row=appDatabase.update("pratham_app_form", cv, "formCompletionStatus=0 AND formID=2", null);
                       // Toast.makeText(DoorToDoorServeyActivity.this, "row"+row, Toast.LENGTH_LONG).show();


                    }

                    if (row > 0) {

                        Toast.makeText(DoorToDoorServeyActivity.this, "Door To DoorServey Saved Succefully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DoorToDoorServeyActivity.this, YouthInfoLinksActivity.class);
                        globalVariable.number_of_youths = Integer.parseInt(num_of_youths.getText().toString());
                        globalVariable.family_owner_name = family_owner_name.getText().toString();
                        intent.putExtra("DoorformData",jsonDataObj1.toString());
                        startActivity(intent);


                    } else {
                        Toast.makeText(DoorToDoorServeyActivity.this, "Door To DoorServey is not  Saved Succefully", Toast.LENGTH_LONG).show();
                    }


                }



            }
        });
    }

    public  void prepareData(){
        final App globalVariable = (App)getApplicationContext();
        Cursor packcur = appDatabase.rawQuery("Select * from pratham_app_form where UID= '"+String.valueOf(globalVariable.MY_UUID_SECURE)+"' and formID = 2 and formCompletionStatus = 0 ", null);
        if(packcur.getCount()>0){
            while (packcur.moveToNext()) {
                update_flag = 1;
                DoorformData = packcur.getString(packcur.getColumnIndex("formData"));

                Log.d("test--", " DoorToDoorServeyActivity_Form_data-" + DoorformData);
                try {
                    JSONObject jsonObject = new JSONObject(DoorformData);
                    family_owner_name.setText(jsonObject.getString("family_owner_name"));
                    door_contact.setText(jsonObject.getString("contact_details"));
                    total_family_size.setText(jsonObject.getString("family_size"));
                    num_earning_members.setText(jsonObject.getString("earning_members"));
                    edit_16_to_18.setText(jsonObject.getString("member_16_to_18"));
                    num_of_youths.setText(jsonObject.getString("member_18_to_35"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }




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



        }



    }

    @Override
    public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

    }

    private boolean ValidateForm() {
        //  Toast.makeText(VillageInformationActivity.this,"getCheckedRadioButtonId."+radio_population.getCheckedRadioButtonId(),Toast.LENGTH_LONG).show();
        alertDialog = new AlertDialog.Builder(DoorToDoorServeyActivity.this);
        if(family_owner_name.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Family Owner  Name.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    family_owner_name.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(door_contact.getText().toString().matches(mobilePattern) == false){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  FILL  Valid    Contact  Number");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    door_contact.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else   if(total_family_size.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Family Size.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    total_family_size.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else   if(num_earning_members.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill  Number Of Earning Members.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    num_earning_members.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else   if(edit_16_to_18.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Member 16 To 18.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    edit_16_to_18.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else   if(num_of_youths.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill  Member 18 To 35.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    num_of_youths.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else {
            return true;
        }
    }

    @Override
    protected void onResume() {

        prepareData();
        super.onResume();
    }
}
