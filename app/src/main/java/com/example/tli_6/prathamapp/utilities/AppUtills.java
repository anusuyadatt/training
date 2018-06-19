package com.example.tli_6.prathamapp.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;


import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.model.Model_TLI_API_Response;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;





public class AppUtills {

    public static Calendar resent_calender=null;
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static AlertDialog alertDialog = null;
    static AlertDialog.Builder alerBuilder = null;
    public static boolean showLogs = true;
    static String TAG="AppUtills";
    public static String WebURLBase = "http://pratham.fieldata.in/";
    public static String serviceUrlBase = "http://pratham.fieldata.in/pace/api/v1";
    public static String meetingUrlBase = "http://pratham.fieldata.in/api/meeting/save";
 /*    public static String WebURLBase = "http://yiidemo1.tlitech.net/";
     public static String serviceUrlBase = "http://yiidemo1.tlitech.net/pace/api/v1";
      public static String meetingUrlBase = "http://yiidemo1.tlitech.net/api/meeting/save";*/

    public static void requestFocus(View view, Activity activity) {

        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        boolean isConnect = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return isConnect = true;
                    }
        }
        return isConnect;
    }


    public static void showAlertDialogOKSpinner(final Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog_ok, null);
        TextView tv_alert_ok = (TextView) view.findViewById(R.id.tv_alert_ok);
        Button btn_ok_alert = (Button) view.findViewById(R.id.btn_ok_alert);
        btn_ok_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null && alerBuilder != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        });
        tv_alert_ok.setText(message);
        alerBuilder = new AlertDialog.Builder(context);
        alerBuilder.setView(view);
        alertDialog = alerBuilder.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public static Model_TLI_API_Response callWebService(Context context, String data, String serviceURL, String methodType) throws IOException {
        BufferedReader in = null;
        String result = "";
        App globalVariable = (App)context;
        Model_TLI_API_Response api_response = new Model_TLI_API_Response();
        String response = "";
        try {
            Log.d("test--","callWebServiceAppUtills--url--"+serviceURL+"-----data--"+data);
            URL url = new URL(serviceURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(methodType);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data); // getQuery(postParameters));
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            if (AppUtills.showLogs)
                Log.e("callWebService1--", "request type11111111" + conn.getRequestMethod());
            int responseCode = conn.getResponseCode();
            if (AppUtills.showLogs)
                Log.e("callWebService2--", "response code " + responseCode + "");
            if (AppUtills.showLogs)
                Log.e("callWebService3--", "Res before decode : " + "response data  " + response);
            api_response.response_code = conn.getResponseCode();
            api_response.response_msg = conn.getResponseMessage();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                InputStream inputStream_reader = null;
                try {
                    inputStream_reader = conn.getInputStream();
                } catch (IOException e) {
                    inputStream_reader = conn.getErrorStream();
                }
                in = new BufferedReader(new InputStreamReader(inputStream_reader, "UTF-8"));
                while ((line = in.readLine()) != null) {
                    response += line;
                }
              /*  byte[] decodedBytes = new byte[0];
                try {
                    decodedBytes = Base64Convert.decode(response);
                } catch (IOException e) {
                    AppUtills.showAlertDialogOKSpinner(context, response);
                    e.printStackTrace();
                }
                result = new String(decodedBytes);*/
                result = response;
                api_response.data = response;
                api_response.response_msg =  conn.getResponseMessage();
                if (AppUtills.showLogs)
                    Log.e("callWebService4--", "Decode Response4: " + "" + api_response.response_msg);
            } else {
                if (AppUtills.showLogs)
                    Log.e("callWebService5--", "getResponseMessage : " + "getResponseMessage  " + conn.getErrorStream());
                if (AppUtills.showLogs)
                    Log.e("callWebService6--", "getResponseMessage : " + "getResponseMessage  " + conn.getResponseMessage());


                /*VKC
                result = "" + conn.getErrorStream();
                String line;
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
                while ((line = in.readLine()) != null) {
                    response += line;
                }
                */
                result = conn.getResponseMessage();

                api_response.response_msg =  conn.getResponseMessage();
            }
            if (AppUtills.showLogs)
                Log.e("callWebService5--", "--Res after decode  " + response);
            if (AppUtills.showLogs)
                Log.e("callWebService6--", "--" + result);
        } catch (Exception e) {
            api_response.response_code = 0;
            api_response.data = e.getMessage();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (AppUtills.showLogs)
            Log.e("test--", "callWebServiceresult is ---> " +  api_response.data);
       // System.exit(0);
        return api_response;
    }

 public static String ParseData2Send(Context context,JSONObject jsonObj, String method_name, String pageName) {
       JSONObject jsonObjNew = new JSONObject();
        String data = "";
        String jsondata = "";
        App globalVariable = (App)context;
        try {

            jsonObjNew.put("data", jsonObj);
            jsonObjNew.put("app_id",  globalVariable.appID);
            Log.d("test--", "  globalVariable.imei_no4" +   globalVariable.imei_no);
            jsonObjNew.put("imei_no", globalVariable.imei_no);
            Log.d("callWeb form_type: ", "" + method_name + " : " + jsonObj.getString("form_type"));
            if(Integer.parseInt(jsonObj.getString("form_type"))!=0){
           jsonObjNew.put("form_version", 1);
           jsonObjNew.put("form_type", jsonObj.getString("form_type"));
           jsonObjNew.put("vuid",  globalVariable.MY_UUID_SECURE );}
            jsondata = jsonObjNew.toString();


            if (AppUtills.showLogs)
                Log.e("Request :", "" + method_name + " dataparsedata: " + jsondata);
            byte[] theByteArray = jsondata.getBytes();
            data = Base64Convert.encodeBytes(theByteArray);
            Log.d("test--", "j" + method_name + " dataparsedata:2: " + jsondata.toString());
            if (AppUtills.showLogs)
                Log.d("Encode Request : ", "" + method_name + " : " + data);
        } catch (Exception e) {
            Log.e("ParseData2Send.Second", "Encode Request : " + method_name + " : " + e);
        }
        Log.d("test--", "request dataparse--" + jsondata);
        return jsondata;//.toString();
        // return data;

    }

}
