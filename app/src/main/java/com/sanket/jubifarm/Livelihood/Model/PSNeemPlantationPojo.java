package com.sanket.jubifarm.Livelihood.Model;

import java.util.ArrayList;

public class PSNeemPlantationPojo extends ArrayList<PSNeemPlantationPojo> {
    private String local_id;
    private String id;
    private String neem_plantation_image;
    private String land_id;
    private String neem_id;
    private String plantation_Date;
    private String flag;
    private String latitude;
    private String longitude;

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

    private static final String TABLE_NAME = "ps_neem_plantation";
    private static final String COLUMN_LOCAL_ID="local_id";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NEEM_PLANTATION_IMAGE= "neem_plantation_image";
    private static final String COLUMN_LAND_ID = "land_id";
    private static final String COLUMN_NEEM_ID = "neem_id";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_PLANTATION_DATE = "plantation_Date";
    private static final String COLUMN_GEO_COORDINATES = "geo_coordinates";
    private static final String COLUMN_FLAG = "flag";



    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_ID + " INTEGER, "
            + COLUMN_NEEM_PLANTATION_IMAGE + " TEXT, "
            + COLUMN_LAND_ID + " TEXT, "
            + COLUMN_NEEM_ID + " TEXT, "
            + COLUMN_LATITUDE + " TEXT, "
            + COLUMN_LONGITUDE + " TEXT, "
            + COLUMN_PLANTATION_DATE + " TEXT, "
            + COLUMN_FLAG + " TEXT, "
            + COLUMN_GEO_COORDINATES + " TEXT "
            + ")";
}
