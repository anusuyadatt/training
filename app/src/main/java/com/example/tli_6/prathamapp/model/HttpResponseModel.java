package com.example.tli_6.prathamapp.model;

/**
 * Created by tli-6 on 5/17/17.
 */
public class HttpResponseModel {
    String resposne;
    int resposne_code;
    public HttpResponseModel(String response,int code){
        this.resposne=response;
        this.resposne_code=code;

    }
    public String getResposne(){
        return  resposne;
    }
    public int getResposneCode(){
        return  resposne_code;
    }
    public void setResposne(String m){
        resposne=m;
    }
    public void setResposneCode(int m){
        resposne_code=m;
    }
}
