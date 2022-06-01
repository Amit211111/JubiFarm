package com.sanket.jubifarm.Livelihood.Model;

public class Neem_Monitoring_Pojo {

    private String local_id;
    private String id;
    private String land_id;
    private String neem_id;
    private String monitoring_date;
    private String neem_monitoring_image;
    private String remarks;
    private String farmer_id;
    private String flag;
    private String latitude;
    private String longitude;

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMonitoring_date() {
        return monitoring_date;
    }

    public void setMonitoring_date(String monitoring_date) {
        this.monitoring_date = monitoring_date;
    }

    public String getNeem_monitoring_image() {
        return neem_monitoring_image;
    }

    public void setNeem_monitoring_image(String neem_monitoring_image) {
        this.neem_monitoring_image = neem_monitoring_image;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    private static final String TABLE_NAME = "neem_monitoring";
    private static final String COLUMN_LOCAL_ID = "local_id";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LAND_ID = "land_id";
    private static final String COLUMN_NEEM_ID = "neem_id";
    private static final String COLUMN_MONITORING_DATE = "monitoring_date";
    private static final String COLUMN_NEEM_MONITORING_IMAGE = "neem_monitoring_image";
    private static final String COLUMN_REMARKS = "remarks";

    public static final String CREATE_TABLE = " CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " INTEGER, "
            + COLUMN_LAND_ID + " TEXT, "
            + COLUMN_NEEM_ID + " TEXT, "
            + COLUMN_MONITORING_DATE + " TEXT, "
            + COLUMN_NEEM_MONITORING_IMAGE + " TEXT, "
            + COLUMN_REMARKS + " TEXT "
            + ")";

}
