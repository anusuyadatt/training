package com.example.tli_6.prathamapp.tasks;

import android.os.AsyncTask;

import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.model.HttpResponseModel;
import com.example.tli_6.prathamapp.logic.HttpCommunicationLayer;
import com.example.tli_6.prathamapp.utilities.WebUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tli-6 on 5/17/17.
 */
public class ServerInteractionTask extends AsyncTask<String,Void,HttpResponseModel> {
    HttpCommunicationLayer httpCommunicationLayer;
    String url;
    ServerInteractionTask task;
    protected HttpResponseModel doInBackground(String... req_data) {
        HttpResponseModel response=null;
        URL requsetUrl;
        try{

            requsetUrl=new URL(url);
            if(!task.isCancelled()) {
                response = doCommunication(requsetUrl, req_data[0]);

            }
        } catch (MalformedURLException e) {
            return null;

        }
        catch (Exception e){

            return new HttpResponseModel("url malformed exception",0);
        }

        return  response;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    HttpResponseModel doCommunication(URL url,String reqData){

        String response="Error while connecting to network so try after some time ";
        InputStream inputStream=null;
        int responseCode=0;
        try{

        HttpURLConnection connection  =   WebUtils.getHttpContext(url);;
           connection.setRequestProperty("Content-Length",
                  reqData.length() + "");
            connection.setRequestProperty("Accept", "text/csv");
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(reqData);
            out.flush();
            out.close();
            System.out.println("before connection");
            App.getInstance().setBckFlag(true);
            connection.connect();
            System.out.println("after connection "+connection);
            System.out.println("response code "+connection.getResponseCode());
            responseCode=connection.getResponseCode();
            if(responseCode==200) {
                inputStream = connection.getInputStream();
                System.out.println("inputstream " + inputStream);
                if (inputStream != null) {
                    response = WebUtils.getString(inputStream);

                }
            }
            else if(responseCode==401)
                return new HttpResponseModel("Either mcode or phone number is wrong ",0);
            else if(responseCode==403)
                return new HttpResponseModel("Account is inactive,please Contact Head Office",0);
            else if(responseCode==500)
                return new HttpResponseModel("Internal Server error",0);
            else
                return new HttpResponseModel("Network Error "+responseCode,0);

        }
        catch (IOException io){
            io.printStackTrace();
            System.out.println("IO catch error");
            if (inputStream != null) {
                try {
                    // ensure stream is consumed...
                    final long count = 1024L;
                    while (inputStream.skip(count) == count) ;
                    inputStream.close();
                } catch (Exception e) {
return new HttpResponseModel("error in network response",0);
                }

            }
            return new HttpResponseModel("error in network response",0);
        }
        catch (Exception e){
            System.out.print("general error ");
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    // ensure stream is consumed...
                    final long count = 1024L;
                    while (inputStream.skip(count) == count) ;
                    inputStream.close();
                } catch (Exception ee) {
                    return new HttpResponseModel("error in network response",0);
                }

            }

        }


       return new HttpResponseModel(response,responseCode);
    }

  public void  setCommunicationLayerListener(HttpCommunicationLayer comlist){
       this.httpCommunicationLayer=comlist;
   }
    @Override
    protected void onPostExecute(HttpResponseModel response) {
        //in  Thread interrupted exception this method never called as doc says
        System.out.println("post exe");
        App.getInstance().setBckFlag(false);
        if(httpCommunicationLayer!=null) {
            synchronized (this) {

                httpCommunicationLayer.returnResponse(response);
            }
        }

    }
    public void setTaskInstance(ServerInteractionTask task){
        this.task=task;
    }
    protected  void onCancelled(){
        App.getInstance().setBckFlag(false);
    }
}
