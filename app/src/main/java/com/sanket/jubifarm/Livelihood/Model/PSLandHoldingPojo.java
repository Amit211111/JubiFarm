package com.sanket.jubifarm.Livelihood.Model;

public class PSLandHoldingPojo
{
    private String local_id;

    private String farmer_id;
   // private String farmer_name;

    private String land_id;
    private String land_unit;

    private String land_image;

    private String land_area;
    private String land_name;

//    public String getFarmer_name() {
//        return farmer_name;
//    }
//
//    public void setFarmer_name(String farmer_name) {
//        this.farmer_name = farmer_name;
//    }

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

    private static final String COLUMN_FARMER_ID="farmer_id";
    private static final String COLUMN_LAND_UNIT="land_unit";

    private static final String COLUMN_LAND_ID="land_id";
    //private static final String COLUMN_FARMER_NAME="farmer_name";

    private static final String COLUMN_LAND_IMAGE= "land_image";
    private static final String COLUMN_LAND_AREA = "land_area";
    private static final String COLUMN_LAND_NAME = "land_name";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LAND_ID + " INTEGER,"
            + COLUMN_FARMER_ID + " TEXT, "
            + COLUMN_LAND_IMAGE + " TEXT, "
            + COLUMN_LAND_UNIT + " TEXT,"
           // + COLUMN_FARMER_NAME + " TEXT,"

            + COLUMN_LAND_AREA + " TEXT, "
            + COLUMN_LAND_NAME + " TEXT "
            + ")";
}
