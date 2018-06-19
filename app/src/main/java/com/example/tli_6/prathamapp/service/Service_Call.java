package com.example.tli_6.prathamapp.service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.interfaces.MyServiceListener;
import com.example.tli_6.prathamapp.model.Model_TLI_API_Response;
import com.example.tli_6.prathamapp.utilities.AppUtills;

import org.json.JSONObject;


public class Service_Call {

    private Context context;
    private String method_name;
    private JSONObject jsonObject;
    private MyServiceListener myServiceListener;
    private Dialog progressbar;

    public Service_Call(Context context, String method_name, JSONObject jsonObject, boolean is_show_dialog, MyServiceListener myServiceListener) {
        this.context = context;
        this.method_name = method_name;
        this.jsonObject = jsonObject;
        this.myServiceListener = myServiceListener;
        if(is_show_dialog) {
            try {
                progressbar = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
                progressbar.setCanceledOnTouchOutside(false);
                progressbar.setCancelable(false);
                progressbar.setContentView(R.layout.dialog_view);
                WebView mWebView = (WebView) progressbar.findViewById(R.id.web);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.setBackgroundColor(Color.TRANSPARENT);
              //  mWebView.loadUrl("file:///android_asset/index.html");
              //  hideProgressbar(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new callServiceApi().execute();
    }

    private class callServiceApi extends AsyncTask<String, Void, Model_TLI_API_Response> {
      /*  @Override
        protected Model_TLI_API_Response doInBackground(String... strings) {
            return null;
        }
*/
        protected void onPreExecute() {
            if (AppUtills.showLogs)
                Log.e("method_name", "" + method_name);
        }
           @Override
        protected Model_TLI_API_Response doInBackground(String... params) {
            String response = "";
            Model_TLI_API_Response api_response = new Model_TLI_API_Response();

            try {
                api_response = AppUtills.callWebService(context,AppUtills.ParseData2Send(context,jsonObject, "" + method_name, "" + method_name), AppUtills.serviceUrlBase+method_name, "" + "POST");


                if( 2==2)
                {
                    Log.d("Service calljson", "" + jsonObject);
                  //  api_response = AppUtills.callWebService(context, AppUtills.ParseData2Send(jsonObject, "" + method_name, "" + method_name), AppUtills.serviceUrlBase+method_name, "" + "POST");
                }
                else
                {
                   // api_response.response_code=404;
                   // api_response.response_msg ="Something goes wrong please try login again!";
                   // api_response.data = "{\"status\":\"0\",\"message\":\"Something goes wrong please try login again!\"}";
                }

              if (AppUtills.showLogs)

                    Log.e("test--response", response.toString());
            } catch (Exception ex) {
                Log.e("test--","Exception in response service_call Exception in response " + ex);
            }

               Log.d("api_response ", "api_response" + api_response);
             //  progressbar.dismiss();
            return api_response;
        }

        @Override
        protected void onPostExecute(Model_TLI_API_Response api_response) {
          //  progressbar.dismiss();
            try {
                if (progressbar != null && progressbar.isShowing()) {
                    progressbar.dismiss();
                }
             //   myServiceListener.getServiceData(result);
         myServiceListener.getServiceData(api_response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

        public void hideProgressbar(boolean b) {
            try {
                if (!b) {
                    if (!((Activity) context).isFinishing() && progressbar != null && !progressbar.isShowing()) {
                        progressbar.show();
                    }
                } else {
                    if (progressbar != null) {
                        if (progressbar.isShowing()) {
                            progressbar.dismiss();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}