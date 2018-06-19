package com.example.tli_6.prathamapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.interfaces.MyServiceListener;
import com.example.tli_6.prathamapp.model.Model_TLI_API_Response;
import com.example.tli_6.prathamapp.service.Service_Call;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

import static com.example.tli_6.prathamapp.application.App.appDatabase;

public class PlanningSheetActivity extends AppCompatActivity implements View.OnClickListener ,android.widget.AdapterView.OnItemSelectedListener{
    String[] village_visit  = new String[]{"Choose", "Village Visit","Rozgaar Sathi Visit", "NGO Visit/Visit with Govt. Official","School/College/ITI Visit", "Village follow-up Visit"};
    Spinner  spinner_visit_village ;
    int villVisit = 0;
    TextView from_date,to_date;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button sbmt_planning_sheet;
    AlertDialog.Builder alertDialog ;
    ProgressDialog progressDialog;
    SimpleDateFormat simpledateformat;
    String fromDate="";String toDate="";
    ContentValues cv;
    private Model_TLI_API_Response model_TLI_api_response;
    String TAG = "PlanningSheetActivity";

    long row=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_sheet);
        final App globalVariable = (App) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppDatabase dbhelper = AppDatabase.getInstance(PlanningSheetActivity.this);
        if (appDatabase == null)
            appDatabase = dbhelper.getWritableDatabase();
        spinner_visit_village = (Spinner)findViewById(R.id.spinner_visit_village);
        from_date = (TextView)findViewById(R.id.from_date);
        to_date = (TextView)findViewById(R.id.to_date);
        sbmt_planning_sheet = (Button) findViewById(R.id.sbmt_planning_sheet);
        from_date.setOnClickListener(this);
        to_date.setOnClickListener(this);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_stylish_item,village_visit
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_stylish_item);
        spinner_visit_village.setAdapter(spinnerArrayAdapter);
        spinner_visit_village.setOnItemSelectedListener(this);
       /* simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fromDate = simpledateformat.format(from_date.getText().toString());
        toDate = simpledateformat.format(to_date.getText().toString());*/
        sbmt_planning_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(ValidateForm()==true)  {

                  new MyAsncTask().execute();
              }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.from_date:

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialogfrom_date = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                from_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                                fromDate =  year+"-"+ (monthOfYear + 1)+"-"+dayOfMonth;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogfrom_date.show();




                break;

            case R.id.to_date:
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialogto_date = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                to_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                                toDate =  year+"-"+ (monthOfYear + 1)+"-"+dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogto_date.show();


                break;




        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        villVisit = position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private class MyAsncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            savingData();
            return null;
        }


        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(PlanningSheetActivity.this);
            progressDialog.setMessage("Please wait........");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            super.onPostExecute(o);



        }
    }

    private void savingData() {
        Log.d("date--", "  fromDate" + fromDate);
        Log.d("date--", "  toDate" + toDate);
        final App globalVariable = (App) getApplicationContext();
        final JSONObject jsonDataObj1 = new JSONObject();
        try {
            jsonDataObj1.put("form_type",4);
            jsonDataObj1.put("village_visit",villVisit);
            jsonDataObj1.put("from_date", fromDate);
            jsonDataObj1.put("to_date", toDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cv = new ContentValues();
        cv.put("formID", 4);
        cv.put("UID", String.valueOf(globalVariable.MY_UUID_SECURE));
        cv.put("formVersion", 1);
        cv.put("villageName",  globalVariable.VillageName);
        cv.put("familyOwner",  "" );
        cv.put("numOfYouths",0);
        cv.put("formCompletionStatus",0);
        cv.put("formData",jsonDataObj1.toString());
       //////////////////////////////Sending Data To Server///////////////////////////////
        new Service_Call(getApplicationContext(), "/form/save", jsonDataObj1, false, new MyServiceListener() {
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

                        Log.d("test--", TAG + "--registrationvillagestatus1--" + jsonObject.getString("status"));
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            jsonObject.getString("message");

                            Log.d("test--", TAG + "--registrationvillagetatus2--" + jsonObject.getString("status"));
                            Log.d("test--", TAG + "--registrationvillagemessage- --" + jsonObject.getString("message"));
                            //////////////////inserting registration data/////////////////
                            cv.put("syncStatus",1);
                            row = appDatabase.insert("pratham_app_form", null, cv);

                        }



                        else {
                            cv.put("syncStatus",0);
                            row = appDatabase.insert("pratham_app_form", null, cv);}

                    }
                    else if ( model_TLI_api_response.response_code==409){
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(PlanningSheetActivity.this);
                        alertDialog.setTitle("VALIDATION");
                        alertDialog.setMessage("Conflict, user sending data from a disabled device");

                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                                return;
                            }
                        });


                        alertDialog.show();


                    }
                    else{
                        cv.put("syncStatus",0);
                        row = appDatabase.insert("pratham_app_form", null, cv);}
                    if (row > 0) {
                        Toast.makeText(PlanningSheetActivity.this, "Planning Sheet Saved Succefully", Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(PlanningSheetActivity.this, "Planning Sheet  is not  Saved Succefully", Toast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();

                }
            }
        });

        finish();

        //////////////////////////////Sending Data To Server///////////////////////////////

    }

    private boolean ValidateForm() {
        //  Toast.makeText(VillageInformationActivity.this,"getCheckedRadioButtonId."+radio_population.getCheckedRadioButtonId(),Toast.LENGTH_LONG).show();
        alertDialog = new AlertDialog.Builder(PlanningSheetActivity.this);
        if(villVisit==0){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  Select  Village Visit");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    spinner_visit_village.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(from_date.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  Select  From    Date");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    from_date.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(to_date.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  Fill  To   Date");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    to_date.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else {
            return true;
        }
    }
}
