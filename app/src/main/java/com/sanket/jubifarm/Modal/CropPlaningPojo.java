package com.sanket.jubifarm.Modal;

public class CropPlaningPojo {


    private  String id;

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    private  String local_id;
    private  String plant_id;
    private  String plant_name;
    private  String   plant_image;
    private  String crop_type_id;
    private  String   land_id;
    private  String   total_plant;
    private  String farmer_id;
    private  String farmer;
    private  String land;
    private  int unit;

    public String getTotal_tree() {
        return total_tree;
    }

    public void setTotal_tree(String total_tree) {
        this.total_tree = total_tree;
    }

    private  String total_tree;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    private  String area;
    private  String  created_on;
    private  String modified_on;
    private  String   del_action;
    private  String  latitude ;
    private  String  longitude;
    private  String user_id;
    private  String crop_type_catagory_id;
    private  String role_id;
    private  String planted_date;
    private  String season;

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

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

    private  String fruited_date;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
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

    private  String crop_type_subcatagory_id;

    public String getFarmer() {
        return farmer;
    }

    public void setFarmer(String farmer) {
        this.farmer = farmer;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }
    public String getTotal_plant() {
        return total_plant;
    }

    public void setTotal_plant(String total_plant) {
        this.total_plant = total_plant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getPlant_image() {
        return plant_image;
    }

    public void setPlant_image(String plant_image) {
        this.plant_image = plant_image;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getCrop_type_id() {
        return crop_type_id;
    }

    public void setCrop_type_id(String crop_type_id) {
        this.crop_type_id = crop_type_id;
    }

    public String getLand_id() {
        return land_id;
    }

    public void setLand_id(String land_id) {
        this.land_id = land_id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public static final String TABLE_NAME = "crop_planning";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_local_id = "local_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PLANT_ID = "plant_id";
    public static final String COLUMN_LAND_ID = "land_id";
    public static final String COLUMN_PLANT_NAME = "plant_name";
    public static final String COLUMN_PLANT_IMAGE_URL = "plant_image";
    public static final String COLUMN_CROP_TYPE_ID = "crop_type_catagory_id";
    public static final String COLUMN_crop_type_subcatagory_id = "crop_type_subcatagory_id";
    public static final String COLUMN_FARMER_ID = "farmer_id";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CREATED_ON = "created_at";
    public static final String COLUMN_MODIFIED_ON= "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_FLAG = "flag";
    public static final String COLUMN_planted_date = "planted_date";
    public static final String COLUMN_fruited_date = "fruited_date";
    public static final String COLUMN_Season = "season";
    public static final String COLUMN_total_tree = "total_tree";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_local_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ID + " INTEGER ,"
                    + COLUMN_USER_ID + " INTEGER,"
                    + COLUMN_PLANT_ID + " INTEGER,"
                    + COLUMN_LAND_ID + " INTEGER,"
                    + COLUMN_CROP_TYPE_ID + " INTEGER,"
                    + COLUMN_AREA + " INTEGER,"
                    + COLUMN_UNIT + " INTEGER,"
                    + COLUMN_FARMER_ID + " INTEGER,"
                    + COLUMN_PLANT_NAME + " TEXT ,"
                    + COLUMN_crop_type_subcatagory_id + " TEXT ,"
                    + COLUMN_LATITUDE + " TEXT ,"
                    + COLUMN_LONGITUDE + " TEXT ,"
                    + COLUMN_PLANT_IMAGE_URL + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_planted_date + " TEXT ,"
                    + COLUMN_fruited_date + " TEXT ,"
                    + COLUMN_Season + " TEXT ,"
                    + COLUMN_total_tree + " TEXT ,"
                    + COLUMN_FLAG + " TEXT"
                    + ")";

}