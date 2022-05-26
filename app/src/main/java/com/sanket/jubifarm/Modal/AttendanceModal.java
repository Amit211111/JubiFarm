package com.sanket.jubifarm.Modal;

public class AttendanceModal {

    private String training_id="0";
    private String farmer_id="0";
    private String user_id="0";

    public AttendanceModal(String training_id) {
        this.training_id = training_id;
    }

    public String getTraining_id() {
        return training_id;
    }

    public void setTraining_id(String training_id) {
        this.training_id = training_id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
