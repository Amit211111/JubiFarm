package com.sanket.jubifarm.Modal;

public class PlantSubCategoryPojo {
   private String id;
    private String name ;
   private String crop_type_catagory_id ;

    public String getCrop_type_subcatagory_id() {
        return crop_type_subcatagory_id;
    }

    public void setCrop_type_subcatagory_id(String crop_type_subcatagory_id) {
        this.crop_type_subcatagory_id = crop_type_subcatagory_id;
    }

    private String crop_type_subcatagory_id ;
   private String created_at ;
   private String updated_at ;
   private String plant_id ;
   private String land_id ;
    private String latitude ;
    private String plant_image ;
    private String farmer_id ;
    private String longitude;
    private String del_action;

    public String getTotal_tree() {
        return total_tree;
    }

    public void setTotal_tree(String total_tree) {
        this.total_tree = total_tree;
    }

    private String total_tree;

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

    private String planted_date;
    private String fruited_date;

    public String getPlant_image() {
        return plant_image;
    }

    public void setPlant_image(String plant_image) {
        this.plant_image = plant_image;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getLand_id() {
        return land_id;
    }

    public void setLand_id(String land_id) {
        this.land_id = land_id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCrop_type_catagory_id() {
        return crop_type_catagory_id;
    }

    public void setCrop_type_catagory_id(String crop_type_catagory_id) {
        this.crop_type_catagory_id = crop_type_catagory_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }

    public static final String TABLE_NAME = "crop_type_sub_catagory_language";
    public static final String COLUMN_ID = "auto_id";
    public static final String COLUMN_AUTO_ID = "id";
    public static final String COLUMN_PLANT_ID = "plant_id";
    public static final String COLUMN_LAND_ID = "land_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CROP_TYPE_CATAGORY_ID = "crop_type_catagory_id";
    public static final String COLUMN_LANGUAGE_ID = "language_id ";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_DEL_ACTION = "del_action";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_AUTO_ID + " TEXT,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_CROP_TYPE_CATAGORY_ID + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_PLANT_ID + " TEXT ,"
                    + COLUMN_LAND_ID + " TEXT ,"
                    + COLUMN_NAME + " TEXT ,"
                    + COLUMN_LANGUAGE_ID + " TEXT ,"
                    + COLUMN_LATITUDE + " TEXT ,"
                    + COLUMN_LONGITUDE + " TEXT ,"
                    + COLUMN_UPDATED_AT + " TEXT"
                    + ")";

}
