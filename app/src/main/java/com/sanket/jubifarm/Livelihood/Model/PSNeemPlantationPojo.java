package com.sanket.jubifarm.Livelihood.Model;

import java.util.ArrayList;

public class PSNeemPlantationPojo  {

    private String local_id;
    private String id;
    private String farmer_id;
    private String neem_plantation_image;
    private String land_id;
    private String neem_id;
    private String plantation_Date;
    private String flag;
    private String latitude;
    private String longitude;
    private String user_id;
    private String del_action;
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNeem_plantation_image() {
        return neem_plantation_image;
    }

    public void setNeem_plantation_image(String neem_plantation_image) {
        this.neem_plantation_image = neem_plantation_image;
    }


    public String getLand_id() {
        return land_id;
    }

    public void setLand_id(String land_id) {
        this.land_id = land_id;
    }

    public String getNeem_id() {
        return neem_id;
    }

    public void setNeem_id(String neem_id) {
        this.neem_id = neem_id;
    }

    public String getPlantation_Date() {
        return plantation_Date;
    }

    public void setPlantation_Date(String plantation_Date) {
        this.plantation_Date = plantation_Date;
    }

    private static final String TABLE_NAME = "add_neem_plant";
    private static final String COLUMN_LOCAL_ID="local_id";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_FARMER_ID="farmer_id";
    private static final String COLUMN_NEEM_PLANTATION_IMAGE= "neem_plantation_image";
    private static final String COLUMN_LAND_ID = "land_id";
    private static final String COLUMN_NEEM_ID = "neem_id";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_PLANTATION_DATE = "plantation_Date";
    private static final String COLUMN_FLAG = "flag";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_DEL_ACTION = "del_action";
    private static final String COLUMN_CREATED_AT = "created_at";



    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " INTEGER, "
            + COLUMN_NEEM_PLANTATION_IMAGE + " TEXT, "
            + COLUMN_FARMER_ID + " TEXT, "
            + COLUMN_LAND_ID + " TEXT, "
            + COLUMN_NEEM_ID + " TEXT, "
            + COLUMN_LATITUDE + " TEXT, "
            + COLUMN_DEL_ACTION + " TEXT, "
            + COLUMN_CREATED_AT + " TEXT, "
            + COLUMN_LONGITUDE + " TEXT, "
            + COLUMN_PLANTATION_DATE + " TEXT, "
            + COLUMN_USER_ID + " TEXT, "
            + COLUMN_FLAG + " TEXT "
            + ")";
}
