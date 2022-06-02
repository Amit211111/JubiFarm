package com.sanket.jubifarm.Livelihood.Model;

public class PSLandHoldingPojo
{
    private String local_id;
    private String id;
    private String farmer_id;
    private String land_id;
    private String land_unit;
    private String land_image;
    private String land_area;
    private String land_name;
    private String flag;
    private String latitude;
    private String longitude;
    private String user_id;
    private String total_no_of_plant;
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

    public String getTotal_no_of_plant() {
        return total_no_of_plant;
    }

    public void setTotal_no_of_plant(String total_no_of_plant) {
        this.total_no_of_plant = total_no_of_plant;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getLand_id() {
        return land_id;
    }
    public void setLand_id(String land_id) {
        this.land_id = land_id;
    }

    public String getLand_unit() {
        return land_unit;
    }

    public void setLand_unit(String land_unit) {
        this.land_unit = land_unit;
    }

    public String getLand_image() {
        return land_image;
    }

    public void setLand_image(String land_image) {
        this.land_image = land_image;
    }

    public String getLand_area() {
        return land_area;
    }

    public void setLand_area(String land_area) {
        this.land_area = land_area;
    }

    public String getLand_name() {
        return land_name;
    }

    public void setLand_name(String land_name) {
        this.land_name = land_name;
    }

    private static final String TABLE_NAME = "ps_land_holding";
    private static final String COLUMN_LOCAL_ID="local_id";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_FARMER_ID="farmer_id";
    private static final String COLUMN_LAND_UNIT="land_unit";
    private static final String COLUMN_LAND_ID="land_id";
    private static final String COLUMN_LAND_IMAGE= "land_image";
    private static final String COLUMN_LAND_AREA = "land_area";
    private static final String COLUMN_LAND_NAME = "land_name";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_DEL_ACTION = "del_action";
    private static final String COLUMN_Flag = "flag";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_TOTAL_NO_OF_PLANT = "total_no_of_plant";
    private static final String COLUMN_USER_ID = "user_id";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " INTEGER,"
            + COLUMN_LAND_ID + " INTEGER,"
            + COLUMN_FARMER_ID + " TEXT, "
            + COLUMN_LAND_IMAGE + " TEXT, "
            + COLUMN_USER_ID + " TEXT, "
            + COLUMN_LAND_UNIT + " TEXT,"
            + COLUMN_LATITUDE + " TEXT,"
            + COLUMN_TOTAL_NO_OF_PLANT + " TEXT,"
            + COLUMN_LONGITUDE + " TEXT,"
            + COLUMN_LAND_AREA + " TEXT, "
            + COLUMN_Flag + " TEXT, "
            + COLUMN_DEL_ACTION + " TEXT, "
            + COLUMN_CREATED_AT + " TEXT, "
            + COLUMN_LAND_NAME + " TEXT "
            + ")";
}
