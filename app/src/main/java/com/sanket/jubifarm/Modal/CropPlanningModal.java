package com.sanket.jubifarm.Modal;

public class CropPlanningModal {







    private String description;
    private String Location;
    private String Date;
    private String Mobile_no;
    private int imgId;

    public CropPlanningModal(String description, String location, String date, String mobile_no, int imgId) {
        this.description = description;
        Location = location;
        Date = date;
        Mobile_no = mobile_no;
        this.imgId = imgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMobile_no() {
        return Mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        Mobile_no = mobile_no;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }





}
