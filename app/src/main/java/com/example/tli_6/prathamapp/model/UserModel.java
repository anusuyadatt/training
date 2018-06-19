package com.example.tli_6.prathamapp.model;

/**
 * Created by tli-6 on 5/18/17.
 */
public class UserModel {
    String device_id;
    String unit;
    String sub_unit;
    String state;
    String mob_center;
    String location;
    String coordinator;
    String name;
    String position;
    String regist_date;
    String appoint_date;
    String mobile_number;
    String mCode;
   public void setDeviceId(String m){
        this.device_id=m;
    }
    public void setUnit(String m){
        unit=m;
    }
    public  void setSubUnit(String m){
        sub_unit=m;
    }
    public void setState(String m){
        state=m;
    }
    public void setMobCenter(String m){
        mob_center=m;
    }
    public void setLocation(String m){
        location=m;
    }
    public void setCoordinator(String m){
        coordinator=m;
    }
    public void setName(String m){
        name=m;
    }
    public void setPosition(String m){
        position=m;
    }
    public void setRegistDate(String m){
        regist_date=m;
    }
    public void setAppointDate(String m){
        appoint_date=m;
    }
    public void setMobileNumber(String m){
        mobile_number=m;
    }
    public void setMCode(String m){
        mCode=m;
    }

    public String getAppointDate() {
        return appoint_date;
    }

    public String getCoordinator() {
        return coordinator;
    }

    public String getDeviceId() {
        return device_id;
    }

    public String getLocation() {
        return location;
    }

    public String getMobCenter() {
        return mob_center;
    }

    public String getMobileNumber() {
        return mobile_number;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getRegistDate() {
        return regist_date;
    }

    public String getState() {
        return state;
    }

    public String getSubUit() {
        return sub_unit;
    }

    public String getUnit() {
        return unit;
    }
    public String getMCode(){
        return mCode;
    }

}
