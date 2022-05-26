package com.sanket.jubifarm.Modal;

public class SaleDetailsPojo {


    private String sale_details_id;
    private String user_id;
    private String farmer_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String
            id;
    private String local_id;
    private String crop_type_catagory_id;
    private String crop_type_subcatagory_id;
    private String season_id;
    private String quantity;
    private String role_id;
    private String quanity_unit_id;
    private String fruited_date;

    public String getFruited_date() {
        return fruited_date;
    }

    public void setFruited_date(String fruited_date) {
        this.fruited_date = fruited_date;
    }

    public String getPlanted_date() {
        return planted_date;
    }

    public void setPlanted_date(String planted_date) {
        this.planted_date = planted_date;
    }

    private String planted_date;

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    private String table_name;

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    private String unique_id;

    public String getQuanity_unit_id() {
        return quanity_unit_id;
    }

    public void setQuanity_unit_id(String quanity_unit_id) {
        this.quanity_unit_id = quanity_unit_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getSale_details_id() {
        return sale_details_id;
    }

    public void setSale_details_id(String sale_details_id) {
        this.sale_details_id = sale_details_id;
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

    public String getSeason_id() {
        return season_id;
    }

    public void setSeason_id(String season_id) {
        this.season_id = season_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCrop_type_price() {
        return crop_type_price;
    }

    public void setCrop_type_price(String crop_type_price) {
        this.crop_type_price = crop_type_price;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String crop_type_price;
    private String year;


    public static final String TABLE_NAME = "sale_details";
    public static final String COLUMN_ID = "local_id";
    public static final String COLUMN_SALE_DETAIL_ID = "id";
    public static final String COLUMN_SALE_CROP_ID = "crop_type_catagory_id";
    public static final String COLUMN_crop_type_subcatagory_id = "crop_type_subcatagory_id";
    public static final String COLUMN_SALE_YEAR = "year";
    public static final String COLUMN_SALE_SEASON = "season_id";
    public static final String COLUMN_SALE_QUANTITY = "quantity";
    public static final String COLUMN_QUANTITY_UNIT_ID = "quanity_unit_id";
    public static final String COLUMN_SALE_CROP_PRICE = "crop_type_price";
    public static final String COLUMN_FARMER_ID = "farmer_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CREATED_ON = "created_at";
    public static final String COLUMN_MODIFIED_ON = "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_planted_date = "planted_date";
    public static final String COLUMN_fruited_date = "fruited_date";
    public static final String COLUMN_FLAG = "flag";
    public static final String COLUMN_IS_CLOSE = "is_close";
    public static final String COLUMN_unique_id = "unique_id";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SALE_DETAIL_ID + " INTEGER,"
                    + COLUMN_SALE_CROP_ID + " INTEGER,"
                    + COLUMN_crop_type_subcatagory_id + " INTEGER,"
                    + COLUMN_SALE_SEASON + " INTEGER,"
                    + COLUMN_SALE_QUANTITY + " INTEGER,"
                    + COLUMN_QUANTITY_UNIT_ID + " INTEGER,"
                    + COLUMN_SALE_CROP_PRICE + " INTEGER,"
                    + COLUMN_SALE_YEAR + " INTEGER,"
                    + COLUMN_FARMER_ID + " INTEGER,"
                    + COLUMN_USER_ID + " INTEGER,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_unique_id + " TEXT ,"
                    + COLUMN_planted_date + " TEXT ,"
                    + COLUMN_fruited_date + " TEXT ,"
                    + COLUMN_IS_CLOSE + " TEXT ,"
                    + COLUMN_FLAG + " TEXT "
                    + ")";


}
