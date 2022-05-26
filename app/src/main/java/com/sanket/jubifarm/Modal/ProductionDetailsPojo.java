package com.sanket.jubifarm.Modal;

public class ProductionDetailsPojo {
    private String local_id;
    private String crop_type_catagory_id;
    private String crop_type_subcatagory_id;
    private String year;
    private String quantity;
    private String quanity_unit_id;
    private String user_id;
    private String farmer_id;
    private String role_id;
    private String id;
    private String planted_date;

    public String getPlanted_date() {
        return planted_date;
    }

    public void setPlanted_date(String planted_date) {
        this.planted_date = planted_date;
    }

    public String getFruited_date() {
        return fruited_date;
    }

    public void setFruited_date(String fruited_date) {
        this.fruited_date = fruited_date;
    }

    private String fruited_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    private String unique_id;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getCrop_type_catagory_id() {
        return crop_type_catagory_id;
    }

    public void setCrop_type_catagory_id(String crop_type_catagory_id) {
        this.crop_type_catagory_id = crop_type_catagory_id;
    }

    public String getCrop_type_subcatagory_id() {
        return crop_type_subcatagory_id;
    }

    public void setCrop_type_subcatagory_id(String crop_type_subcatagory_id) {
        this.crop_type_subcatagory_id = crop_type_subcatagory_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuanity_unit_id() {
        return quanity_unit_id;
    }

    public void setQuanity_unit_id(String quanity_unit_id) {
        this.quanity_unit_id = quanity_unit_id;
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

    public String getSeason_id() {
        return season_id;
    }

    public void setSeason_id(String season_id) {
        this.season_id = season_id;
    }

    private String season_id;



    public static final String TABLE_NAME = "production_details";
    public static final String COLUMN_ID = "local_id";
    public static final String COLUMN_PRODUCTION_ID = "id";
    public static final String COLUMN_crop_type_catagory_id = "crop_type_catagory_id";
    public static final String COLUMN_crop_type_subcatagory_id = "crop_type_subcatagory_id";
    public static final String COLUMN_year = "year";
    public static final String COLUMN_PRODUCTION_QUANTITY = "quantity";
    public static final String COLUMN_season_id= "season_id";
    public static final String COLUMN_QUANTITY_UNIT_ID = "quanity_unit_id";
    public static final String COLUMN_FARMER_ID = "farmer_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CREATED_ON = "created_at";
    public static final String COLUMN_MODIFIED_ON = "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_FLAG = "flag";
    public static final String COLUMN_unique_id = "unique_id";
    public static final String COLUMN_planted_date = "planted_date";
    public static final String COLUMN_fruited_date = "fruited_date";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PRODUCTION_ID + " INTEGER,"
                    + COLUMN_crop_type_catagory_id + " INTEGER,"
                    + COLUMN_crop_type_subcatagory_id + " INTEGER,"
                    + COLUMN_year + " INTEGER,"
                    + COLUMN_season_id + " INTEGER,"
                    + COLUMN_PRODUCTION_QUANTITY + " INTEGER,"
                    + COLUMN_QUANTITY_UNIT_ID + " TEXT ,"
                    + COLUMN_FARMER_ID + " INTEGER,"
                    + COLUMN_USER_ID + " INTEGER,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_unique_id + " TEXT ,"
                    + COLUMN_planted_date + " TEXT ,"
                    + COLUMN_fruited_date + " TEXT ,"
                    + COLUMN_FLAG + " TEXT"
                    + ")";


}
