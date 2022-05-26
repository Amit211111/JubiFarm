package com.sanket.jubifarm.Livelihood.Model;

public class PSLandHoldingPojo
{
    private String Local_id;
    private String id;
    private String farmer_id;
    private String Land_id;
    private String btn_upload_land;
    private String Farmer_Selection;
    private String Land_Area;
    private String Land_Name;

    public String getBtn_upload_land() {
        return btn_upload_land;
    }

    public void setBtn_upload_land(String btn_upload_land) {
        this.btn_upload_land = btn_upload_land;
    }

    public String getLocal_id() {
        return Local_id;
    }

    public void setLocal_id(String local_id) {
        Local_id = local_id;
    }

    public String getId() {
        return id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLand_id() {
        return Land_id;
    }

    public void setLand_id(String land_id) {
        Land_id = land_id;
    }

    public String getFarmer_Selection() {
        return Farmer_Selection;
    }

    public void setFarmer_Selection(String farmer_Selection) {
        Farmer_Selection = farmer_Selection;
    }

    public String getLand_Area() {
        return Land_Area;
    }

    public void setLand_Area(String land_Area) {
        Land_Area = land_Area;
    }

    public String getLand_Name() {
        return Land_Name;
    }

    public void setLand_Name(String land_Name) {
        Land_Name = land_Name;
    }
    private static final String TABLE_NAME = "Ps_Land_Holding";
    private static final String COLUMN_LOCAL_ID="Local_id";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_FARMER_ID="farmer_id";

    private static final String COLUMN_LAND_ID="Land_id";

    private static final String COLUMN_BTN_UPLOAD_LAND= "btn_upload_land";
    private static final String COLUMN_FARMER_SELECTION = "Farmer_Selection";
    private static final String COLUMN_LAND_AREA = "Land_Area";
    private static final String COLUMN_LAND_NAME = "Land_Name";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " INTEGER, "
            + COLUMN_FARMER_ID + " INTEGER, "
            + COLUMN_LAND_ID + " INTEGER,"
            + COLUMN_BTN_UPLOAD_LAND + " TEXT, "
            + COLUMN_FARMER_SELECTION +" TEXT, "
            + COLUMN_LAND_AREA + " TEXT, "
            + COLUMN_LAND_NAME + " TEXT "
            + ")";
}
