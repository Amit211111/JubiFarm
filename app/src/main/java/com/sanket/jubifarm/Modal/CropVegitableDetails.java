package com.sanket.jubifarm.Modal;

public class CropVegitableDetails {

    public static final String TABLE_NAME = "crop_vegetable_details";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "crop_name";
    public static final String COLUMN_crop_type_subcatagory_id = "crop_type_subcatagory_id";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_UNIT_ID = "unit_id";
    public static final String COLUMN_UNITS_ID = "units_id";
    public static final String COLUMN_SEASON_ID = "season_id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_DEL_ACTION = " del_action";
    public static final String COLUMN_FARMER_REGISTRATION_ID = "farmer_id";
    public static final String COLUMN_OFFLINE_SYNC = "offline_sync";
    public static final String COLUMN_FLAG = "flag";


    private String crop_name;
    private String area;
    private String season_id;
    private String quantity;
    private int offline_sync;

    public int getOffline_sync() {
        return offline_sync;
    }

    public void setOffline_sync(int offline_sync) {
        this.offline_sync = offline_sync;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCrop_type_subcatagory_id() {
        return crop_type_subcatagory_id;
    }

    public void setCrop_type_subcatagory_id(String crop_type_subcatagory_id) {
        this.crop_type_subcatagory_id = crop_type_subcatagory_id;
    }

    private String crop_type_subcatagory_id;

    public String getCrop_name() {
        return crop_name;
    }

    public void setCrop_name(String crop_name) {
        this.crop_name = crop_name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSeason_id() {
        return season_id;
    }

    public void setSeason_id(String season_id) {
        this.season_id = season_id;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    private String unit_id;
    private String units_id;

    public String getUnits_id() {
        return units_id;
    }

    public void setUnits_id(String units_id) {
        this.units_id = units_id;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_ID + " INTEGER,"
                    + COLUMN_NAME + " TEXT ,"
                    + COLUMN_crop_type_subcatagory_id + " TEXT ,"
                    + COLUMN_AREA +  " TEXT ,"
                    + COLUMN_UNIT_ID +  " TEXT ,"
                    + COLUMN_UNITS_ID +  " TEXT ,"
                    + COLUMN_SEASON_ID+ " TEXT ,"
                    + COLUMN_QUANTITY+ " TEXT ,"
                    + COLUMN_CREATED_AT+ " TEXT ,"
                    + COLUMN_FARMER_REGISTRATION_ID + " TEXT ,"
                    + COLUMN_UPDATED_AT + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_OFFLINE_SYNC + " INTEGER DEFAULT 0 ,"
                    + COLUMN_FLAG + " INTEGER DEFAULT 0 "
                    + ")";

}
