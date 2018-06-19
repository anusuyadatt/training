package com.example.tli_6.prathamapp.model;

public class DoorToDoorModel {
    private  String num_of_youths,family_owner_name;

    public DoorToDoorModel() {
    }

    public DoorToDoorModel(String num_of_youths,String family_owner_name) {

        this.num_of_youths = num_of_youths;
        this.family_owner_name = family_owner_name;
    }



    public String getNumOfYouths() {
        return num_of_youths;
    }

    public void setNumOfYouths(String num_of_youths) {
        this.num_of_youths = num_of_youths;
    }
    public String getFamilyOwnerName() {
        return family_owner_name;
    }

    public void setFamilyOwnerName(String family_owner_name) {
        this.family_owner_name = family_owner_name;
    }

}
