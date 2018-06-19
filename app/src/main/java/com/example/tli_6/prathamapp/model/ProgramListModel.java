package com.example.tli_6.prathamapp.model;

/**
 * Created by tli-6 on 5/18/17.
 */
public class ProgramListModel {
    String program_name;
    String program_code;
    public void setProgramName(String m){
        program_name=m;
    }
    public void setProgramCode(String m){
        program_code=m;
    }
    public String getProgramName(){
        return  program_name;
    }

    public String getProgramCode() {
        return program_code;
    }
}
