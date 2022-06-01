package com.sanket.jubifarm.Livelihood.Model;

public class SkillCenter_Pojo {
    private String id;
    private String center_name;
    private String state_id;
    private String district_id;
    private String block_id;
    private String village_id;
    private String pincode;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCenter_name() {
        return center_name;
    }

    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private static final String TABLE_NAME = "skill_center";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_CENTER_NAME="center_name";
    private static final String COLUMN_STATE_ID="state_id";
    private static final String COLUMN_DISTRICT_ID="district_id";
    private static final String COLUMN_BLOCK_ID= "block_id";
    private static final String COLUMN_VILLAGE_ID = "village_id";
    private static final String COLUMN_PINCODE = "pincode";
    private static final String COLUMN_STATUS = "status";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CENTER_NAME + " INTEGER,"
            + COLUMN_STATE_ID + " TEXT, "
            + COLUMN_DISTRICT_ID + " TEXT, "
            + COLUMN_BLOCK_ID + " TEXT,"
            + COLUMN_VILLAGE_ID + " TEXT,"
            + COLUMN_PINCODE + " TEXT,"
            + COLUMN_STATUS + " TEXT "
            + ")";

}
